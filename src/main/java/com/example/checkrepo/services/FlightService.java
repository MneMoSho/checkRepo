package com.example.checkrepo.services;

import com.example.checkrepo.dto.FlightDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface FlightService {
   void createDbFlight(FlightDto FlightDto);

   Optional<FlightDto> findById(Long id);

   void deleteFlight(Long id);

   List<FlightDto> displayAll();

 //  FlightDto getFlightById(int id);
}
