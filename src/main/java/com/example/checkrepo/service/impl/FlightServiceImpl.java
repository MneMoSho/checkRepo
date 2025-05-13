package com.example.checkrepo.service.impl;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.entities.Company;
import com.example.checkrepo.entities.Flight;
import com.example.checkrepo.entities.User;
import com.example.checkrepo.exception.IncorrectInputException;
import com.example.checkrepo.exception.ObjectNotFoundException;
import com.example.checkrepo.mapper.FlightMapper;
import com.example.checkrepo.repository.CompanyRepository;
import com.example.checkrepo.repository.FlightRep;
import com.example.checkrepo.service.CompanyService;
import com.example.checkrepo.service.FlightService;
import com.example.checkrepo.service.cache.Cache;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.*;

import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRep flightRepository;
    private final CompanyRepository companyRepository;
    private final Cache cache;
    private final CompanyServiceImpl company;
    private final CompanyService companyService;
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

    public int findMinPrice() {
        List<Flight> allFlights = flightRepository.findAll();
        Optional<Flight> minFlight = allFlights.stream().min(Comparator.comparing(flight -> flight.getPrice()));
        System.out.println(minFlight.get().getPrice());
        return minFlight.get().getPrice();
    }

    @Override
    public List<FlightDto> findByCountry() {
        List<Flight> allFlights = flightRepository.findAll();
        Map<String, Flight> countryToFlightMap = new HashMap<>();
        for (Flight flight : allFlights) {
            countryToFlightMap.putIfAbsent(flight.getCountry(), flight);
        }
        return FlightMapper.toDtoList(new ArrayList<>(countryToFlightMap.values()));
    }

    @Override
    public List<FlightDto> findFromFront(FlightDto destination) {
        List<Flight> allFlights = flightRepository.findAll();

        System.out.println(destination.getTimeLeaving());
        System.out.println(allFlights.stream().findFirst().get().getTimeLeaving());
        List<Flight> foundFlight = new ArrayList<>();
        foundFlight = allFlights.stream()
                .filter(flight -> flight.getStartDestination().equals(destination.getStartDestination()))
                .filter(flight -> flight.getEndDestination().equals(destination.getEndDestination()))
                .filter(flight -> flight.getTimeLeaving().equals(destination.getTimeLeaving()))
               .filter(flight -> flight.getTimeArriving().equals(destination.getTimeArriving()))
                .toList();
        for(Flight List : foundFlight ) {
            System.out.println(List.getLength());
            System.out.println(List.getEndDestination());
            System.out.println(List.getCountry());
        }
        return FlightMapper.toDtoList(foundFlight);
    }

    @Override
    public List<FlightDto> findByCities(FlightDto destination) {
        List<Flight> allFlights = flightRepository.findAll();
        List<Flight> foundFlight = new ArrayList<>();
        foundFlight = allFlights.stream()
                .filter(flight -> flight.getStartDestination().equals(destination.getStartDestination()))
                .filter(flight -> flight.getEndDestination().equals(destination.getEndDestination()))
                .filter(flight -> flight.getCountry().equals(destination.getCountry()))
                .toList();
        return FlightMapper.toDtoList(foundFlight);
    }

    @Override
    public List<FlightDto> uniqueFlightsByCountry(String country) {
        List<Flight> allFlights = flightRepository.findAll();
        List<Flight> foundFlights = new ArrayList<>();
        for(Flight flight : allFlights) {
            if(flight.getCountry().equals(country)) {
                foundFlights.add(flight);
            }
        }
        return FlightMapper.toDtoList(foundFlights);
    }

    @Override
    public void createFlightFromFront(FlightDto flight) {
        System.out.println("FFFFFFFFF");

        System.out.println(flight.getFlightCompany());

        List<Company> companyList= companyRepository.findAll();
        for(Company company : companyList) {
            if(company.getCompanyName().equals(flight.getFlightCompany())) {
                flight.setCompanyId(company.getId());
            }
        }
        System.out.println(flight.getCompanyId());
        createDbFlight(flight);
    }

}
