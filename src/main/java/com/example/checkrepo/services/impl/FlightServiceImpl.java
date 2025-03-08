package com.example.checkrepo.services.impl;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.entities.Flight;
import com.example.checkrepo.entities.User;
import com.example.checkrepo.mapper.FlightMapper;
import com.example.checkrepo.repository.FlightRep;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.checkrepo.services.FlightService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRep flightRepository;

    @Override
    public void createDbFlight(FlightDto FlightDto) {
        flightRepository.save(FlightMapper.toEntity(FlightDto));
    }

    @Override
    public List<FlightDto> displayAll() {
        return FlightMapper.toDtoList(flightRepository.findAll());
    }

    @Override
    public void deleteFlight(Long id) {
        for(Flight sourceFlight : flightRepository.findAll()) {
            if(sourceFlight.getId().equals(id)) {
                for(User sourceUser : sourceFlight.getUsers()) {
                    sourceUser.getFlights().remove(sourceFlight);
                    sourceFlight.getUsers().remove(sourceUser);
                }
            }
        }
        flightRepository.deleteById(id);
    }

    @Override
    public Optional<FlightDto> findById(Long id) {
        Optional<Flight> optionalById = flightRepository.findById(id);
        return optionalById.map(FlightMapper::toFlightDto);
    }

    @Override
    public List<FlightDto> getByStartDest(String startName) {
        List<Flight> startList = new ArrayList<>();
        for(Flight source : flightRepository.findAll()) {
            if(source.getStartDestination().equals(startName)) {
                startList.add(source);
            }
        }
        return FlightMapper.toDtoList(startList);
    }

    @Override
    public List<FlightDto> getByQueryParam(String companyName, Long maxLength) {

        System.out.println(companyName);

        List<Flight> flightList = flightRepository.findByCompany(companyName, maxLength);
        System.out.println(flightList.get(0).getFlightCompany());
        return FlightMapper.toDtoList(flightList);
    }
}
