package com.example.checkrepo.services;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.entities.Flight;
import com.example.checkrepo.mapper.FlightMapper;
import com.example.checkrepo.repository.FlightRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FlightServiceImpl implements FlightService {

    private FlightRepository flightRepository;

    @Override
    public FlightDto createFlight(FlightDto flightDto) {
        Flight flight = FlightMapper.mapToFlight(flightDto);
        Flight saved = flightRepository.save(flight);
        return FlightMapper.mapToFlightDto(saved);
    }
}
