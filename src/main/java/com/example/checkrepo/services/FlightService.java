package com.example.checkrepo.services;

import com.example.checkrepo.dto.FlightDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface FlightService {
   void createDbFlight(FlightDto FlightDto);

   Optional<FlightDto> findById(int id);

 //  FlightDto getFlightById(int id);
}
