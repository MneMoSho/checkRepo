package com.example.checkrepo.service.impl;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.entities.Flight;
import com.example.checkrepo.entities.User;
import com.example.checkrepo.exception.IncorrectInputException;
import com.example.checkrepo.exception.ObjectNotFoundException;
import com.example.checkrepo.mapper.FlightMapper;
import com.example.checkrepo.repository.FlightRep;
import com.example.checkrepo.service.FlightService;
import com.example.checkrepo.service.cache.Cache;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRep flightRepository;
    private final Cache cache;
    private final CompanyServiceImpl company;
    private EntityManager entityManager;

    @Override
    public void restartSequence(int restartIndex) {
        String sql = "ALTER SEQUENCE flight_id_seq RESTART WITH " + restartIndex;
        entityManager.createNativeQuery(sql).executeUpdate();
    }

    @Override
    public void createDbFlight(FlightDto flightDto) {
        if (flightDto.getCompanyId() == null) {
            throw new IncorrectInputException("Incorrect input, Id cannot be null");
        }
        Flight saveFlight = FlightMapper.toEntity(flightDto);
        flightRepository.save(saveFlight);
        company.addFlightToCompany(saveFlight.getId(), flightDto.getCompanyId());
        System.out.println(saveFlight.getId());
        cache.putFlight(saveFlight.getId(), FlightMapper.toFlightDto(saveFlight));
    }

    @Override
    public List<FlightDto> displayAll() {
        Collection<FlightDto> allFlights = cache.getAllFlights();
        if (!allFlights.isEmpty() && allFlights.size() == flightRepository.count()) {
            return new ArrayList<>(allFlights);
        } else {
            List<FlightDto> flights = flightRepository
                    .findAll().stream().map(FlightMapper::toFlightDto).toList();
            if (!allFlights.isEmpty()) {
                for (FlightDto source : allFlights) {
                    if (flights.stream().noneMatch(
                            flight -> flight.getId().equals(source.getId()))) {
                        cache.putFlight(source.getId(), source);
                    }
                }
            } else {
                flights.forEach(flight -> cache.putFlight(flight.getId(), flight));
            }
            return flights;
        }
    }

    @Transactional
    @Override
    public void deleteFlight(Long id) {
        Flight newFlight = flightRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("cannot be found"));
        for (User sourceUser : newFlight.getUsers()) {
            sourceUser.getFlights().remove(newFlight);
            newFlight.getUsers().remove(sourceUser);
        }
        if (cache.getFlight(id) != null) {
            cache.deleteFlight(id);
        }
        flightRepository.delete(newFlight);
    }

    @Override
    @Transactional
    public Optional<FlightDto> findById(Long id) {
        FlightDto foundFlight = cache.getFlight(id);
        if (foundFlight == null) {
            Flight flightById = flightRepository.findById(id)
                    .orElseThrow(() -> new ObjectNotFoundException("Flight cannot be found"));
            cache.putFlight(id, FlightMapper.toFlightDto(flightById));
            return Optional.ofNullable(FlightMapper.toFlightDto(flightById));
        } else {
            return Optional.of(foundFlight);
        }
    }

    @Override
    public List<FlightDto> getByStartDestNative(String startName) {
        List<Flight> flightList = flightRepository.findByStartDestinationNative(startName);
        if (flightList.isEmpty()) {
            throw new ObjectNotFoundException("List is empty");
        }
        return FlightMapper.toDtoList(flightRepository.findByStartDestinationNative(startName));
    }

    @Override
    public List<FlightDto> getByStartDestJpql(String startName) {
        Collection<FlightDto> flights = cache.getAllFlights();
        if (flights.stream().noneMatch(flight -> flight.getStartDestination().equals(startName))) {
            List<Flight> flightList = flightRepository.findByStartDestinationJPQL(startName);
            System.out.println("not from cache");
            if (flightList.isEmpty()) {
                throw new ObjectNotFoundException("List is empty");
            }
            flightList.forEach(flight ->
                    cache.putFlight(flight.getId(), FlightMapper.toFlightDto(flight)));
            return FlightMapper.toDtoList(flightRepository.findByStartDestinationJPQL(startName));
        } else {
            System.out.println("from cache");
            return flights.stream().filter(
                    flight -> flight.getStartDestination().equals(startName)).toList();
        }
    }

    @Override
    @Transactional
    public void getFromExcel() throws IOException {
        String fileName = "Flights.xlsx";
        Path excelFilepath = Paths.get("ExcelFiles", fileName);
        FileInputStream excelFile = new FileInputStream(new File(excelFilepath.toString()));
        Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet sheet = workbook.getSheetAt(0);
        sheet.forEach(row -> {
            FlightDto newFlightDto = new FlightDto();
            if (row.getRowNum() != 0) {
                newFlightDto.setLength((int) row.getCell(0).getNumericCellValue());
                newFlightDto.setEndDestination(row.getCell(1).getStringCellValue());
                newFlightDto.setStartDestination(row.getCell(2).getStringCellValue());
                newFlightDto.setCompanyId((long) row.getCell(3).getNumericCellValue());
                createDbFlight(newFlightDto);
                cache.putFlight(newFlightDto.getId(), newFlightDto);
            }
        });
    }

    public List<FlightDto> bulkOperation(List<String> companies) {
        List<Flight> flightList = flightRepository.findAll();
        List<Flight> sortedFlights = new ArrayList<>();
        for (String company : companies) {
            List<Flight> bufList = new ArrayList<>(flightList.stream().filter(newFlight ->
                    newFlight.getCompany().getCompanyName().equals(company)).toList());
            if (bufList.isEmpty()) {
                System.out.println("not found");
            }
            sortedFlights.addAll(bufList);
        }
        return FlightMapper.toDtoList(sortedFlights);
    }
}
