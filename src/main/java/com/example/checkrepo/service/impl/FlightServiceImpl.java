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
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRep flightRepository;
    private final FlightRep flightRep;
    private final FlightCache cache;
private final CompanyServiceImpl company;

    @Override
    public void createDbFlight(FlightDto flightDto) {
        if(flightDto.getCompanyId() == null) {
            throw new IncorrectInputException("Incorrect input, Id cannot be null");
        }
        flightRepository.save(FlightMapper.toEntity(flightDto));
        Flight newFlight = flightRepository.findAll().getLast();
        company.addFlightToCompany(newFlight.getId(), flightDto.getCompanyId());
        cache.putIfAbsent(newFlight.getId(), newFlight);
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

        List<Flight> flightList = flightRep.findByStartDestination(startName);
        if (flightList.isEmpty()) {
            throw new ObjectNotFoundException("List is empty");
        }
        return FlightMapper.toDtoList(flightRep.findByStartDestination(startName));
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
}
