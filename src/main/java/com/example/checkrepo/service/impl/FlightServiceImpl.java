package com.example.checkrepo.service.impl;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.entities.Flight;
import com.example.checkrepo.entities.User;
import com.example.checkrepo.exception.IncorrectInputException;
import com.example.checkrepo.exception.ObjectNotFoundException;
import com.example.checkrepo.mapper.FlightMapper;
import com.example.checkrepo.repository.FlightRep;
import com.example.checkrepo.service.FlightService;
import com.example.checkrepo.service.cache.FlightCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRep flightRepository;
    private final FlightCache cache;
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
        cache.putIfAbsent(saveFlight.getId(), saveFlight);
    }

    @Override
    public List<FlightDto> displayAll() {
        return FlightMapper.toDtoList(flightRepository.findAll());
    }

    @Override
    public void deleteFlight(Long id) {
        Flight deleteFlight = cache.get(id);
        if (deleteFlight == null) {
            Flight sourceFlight = flightRepository.findById(id)
                    .orElseThrow(() -> new ObjectNotFoundException("cannot be found"));
            for (User sourceUser : sourceFlight.getUsers()) {
                sourceUser.getFlights().remove(sourceFlight);
                sourceFlight.getUsers().remove(sourceUser);
            }

        } else {
            for (User sourceUser : deleteFlight.getUsers()) {
                sourceUser.getFlights().remove(deleteFlight);
                deleteFlight.getUsers().remove(sourceUser);
            }
            cache.remove(id);
        }
        flightRepository.deleteById(id);
        restartSequence(flightRepository.findAll().getLast().getId().intValue());
    }

    @Override
    public Optional<FlightDto> findById(Long id) {
        Flight foundFlight = cache.get(id);
        if (foundFlight == null) {
            Flight flightById = flightRepository.findById(id)
                    .orElseThrow(() -> new ObjectNotFoundException("Flight cannot be found"));
            cache.putIfAbsent(id, flightById);
            return Optional.ofNullable(FlightMapper.toFlightDto(flightById));
        } else {
            return Optional.ofNullable(FlightMapper.toFlightDto(foundFlight));
        }
    }

    @Override
    public List<FlightDto> getByStartDest(String startName) {

        List<Flight> flightList = flightRepository.findByStartDestination(startName);
        if (flightList.isEmpty()) {
            throw new ObjectNotFoundException("List is empty");
        }
        return FlightMapper.toDtoList(flightRepository.findByStartDestination(startName));
    }

    @Override
    public List<FlightDto> getByQueryParam(String companyName, Long maxLength) {

        if (maxLength < 0) {
            throw new IncorrectInputException("incorrect value in length");
        }
        List<Flight> flightList = flightRepository.findByCompany(companyName, maxLength);
        if (flightList.isEmpty()) {
            throw new ObjectNotFoundException("Company or Flight is entered wrong");
        }
        return FlightMapper.toDtoList(flightList);
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
            }
        });
    }

    public List<FlightDto> bulkOperation(List<String> companies) {
        List<Flight> flightList = flightRepository.findAll();
        List<Flight> sortedFlights = new ArrayList<>();
        for(String company : companies) {
            List<Flight> bufList = new ArrayList<>(flightList.stream().filter(newFlight ->
                    newFlight.getCompany().getCompanyName().equals(company)).toList());
            if(bufList.isEmpty()) {
                System.out.println("not found");
            }
            sortedFlights.addAll(bufList);
        }
        return FlightMapper.toDtoList(sortedFlights);
    }
}
