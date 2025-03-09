package com.example.checkrepo.services.impl;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.entities.Flight;
import com.example.checkrepo.entities.User;
import com.example.checkrepo.exception.GlobalExceptionHandler;
import com.example.checkrepo.exception.IncorrectInputException;
import com.example.checkrepo.exception.ObjectNotFoundException;
import com.example.checkrepo.exception.ObjectOutOfRangeException;
import com.example.checkrepo.mapper.FlightMapper;
import com.example.checkrepo.repository.FlightRep;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.checkrepo.services.FlightService;
import com.example.checkrepo.services.cache.FlightCache;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRep flightRepository;
    private final FlightRep flightRep;
    private final FlightCache cache;
    private final GlobalExceptionHandler erroHandler;

    @Override
    public void createDbFlight(FlightDto FlightDto) {
        flightRepository.save(FlightMapper.toEntity(FlightDto));
        Flight newFlight = flightRepository.findAll().getLast();
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
            Long maxValue = flightRepository.findAll().getLast().getId();
            if (maxValue > id) {
                Flight sourceFlight = flightRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("cannot be found"));
                for (User sourceUser : sourceFlight.getUsers()) {
                    sourceUser.getFlights().remove(sourceFlight);
                    sourceFlight.getUsers().remove(sourceUser);
                }
            } else
                throw new ObjectOutOfRangeException("Out of range");
        } else {
            for (User sourceUser : deleteFlight.getUsers()) {
                sourceUser.getFlights().remove(deleteFlight);
                deleteFlight.getUsers().remove(sourceUser);
            }
            cache.remove(id, deleteFlight);
        }
        flightRepository.deleteById(id);
    }

    @Override
    public Optional<FlightDto> findById(Long id) {
        Flight foundFlight = cache.get(id);
        if (foundFlight == null) {
            flightRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Flight cannot be found"));
            cache.putIfAbsent(id, flightRepository.findById(id).get());
            return flightRepository.findById(id).map(FlightMapper::toFlightDto);
        } else
            return Optional.ofNullable(FlightMapper.toFlightDto(foundFlight));
    }

    @Override
    public List<FlightDto> getByStartDest(String startName) {
        return FlightMapper.toDtoList(flightRep.findByStartDestination(startName));
    }

    @Override
    public List<FlightDto> getByQueryParam(String companyName, Long maxLength) {

        if (maxLength < 0)
            throw new IncorrectInputException("incorrect value in length");
        List<Flight> flightList = flightRepository.findByCompany(companyName, maxLength);
        if (flightList.isEmpty()) {
            throw new ObjectNotFoundException("Company or Flight is entered wrong");
        }
        return FlightMapper.toDtoList(flightList);
    }
}
