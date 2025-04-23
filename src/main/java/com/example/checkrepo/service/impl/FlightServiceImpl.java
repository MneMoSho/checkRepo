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

//    @Override
//    public void restartSequence(int restartIndex) {
//        String sql = "ALTER SEQUENCE flight_id_seq RESTART WITH " + restartIndex;
//        entityManager.createNativeQuery(sql).executeUpdate();
//    }

    @Override
    public void createDbFlight(FlightDto flightDto) {
        if (flightDto.getCompanyId() == null) {
            throw new IncorrectInputException("Incorrect input, Id cannot be null");
        }
        System.out.println(flightDto.getFlightCompany());
        Flight saveFlight = FlightMapper.toEntity(flightDto);
        flightRepository.save(saveFlight);
        company.addFlightToCompany(saveFlight.getId(), flightDto.getCompanyId());
         cache.putFlight(saveFlight.getId(), FlightMapper.toFlightDto(saveFlight));
    }

    @Override
    public List<FlightDto> displayAll() {
            List<FlightDto> flights = flightRepository
                    .findAll().stream().map(FlightMapper::toFlightDto).toList();
            return flights;
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
            List<Flight> flightList = flightRepository.findByStartDestinationJpql(startName);
            System.out.println("not from cache");
            if (flightList.isEmpty()) {
                throw new ObjectNotFoundException("List is empty");
            }
            flightList.forEach(flight ->
                    cache.putFlight(flight.getId(), FlightMapper.toFlightDto(flight)));
            return FlightMapper.toDtoList(flightRepository.findByStartDestinationJpql(startName));
        } else {
            System.out.println("from cache");
            return flights.stream().filter(
                    flight -> flight.getStartDestination().equals(startName)).toList();
        }
    }

    public List<FlightDto> bulkOperation(List<String> companies) {
        if(companies.isEmpty()) {
            throw new ObjectNotFoundException("You haven't entered anything");
        }
        List<Flight> flightList = flightRepository.findAll();
        List<Flight> sortedFlights = new ArrayList<>();
        for (String company : companies) {
            List<Flight> bufList = new ArrayList<>(flightList.stream().filter(newFlight ->
                    newFlight.getCompany().getCompanyName().equals(company)).toList());
            sortedFlights.addAll(bufList);
        }
        if (sortedFlights.isEmpty()) {
            throw new ObjectNotFoundException("Company you've chosen doesn't have any flights");
        }
        return FlightMapper.toDtoList(sortedFlights);
    }

    @Override
    public List<FlightDto> postFlights(List<FlightDto> flightDtos) {
        if(flightDtos.isEmpty()) {
            throw new ObjectNotFoundException("List is empty");
        }

        List<Flight> flightToSave = flightDtos.stream().map(FlightMapper::toEntity).toList();
        List<Flight> savedFlights = flightRepository.saveAll(flightToSave);

        for(int i = 0; i<savedFlights.size(); i++) {
            Flight companyFlight = savedFlights.get(i);
            FlightDto flightDto = flightDtos.get(i);
            company.addFlightToCompany(companyFlight.getId(), flightDto.getCompanyId());
            cache.putFlight(companyFlight.getId(), FlightMapper.toFlightDto(companyFlight));
        }
        return FlightMapper.toDtoList(savedFlights);
    }
}
