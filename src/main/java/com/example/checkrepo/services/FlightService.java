package com.example.checkrepo.services;

import com.example.checkrepo.dto.FlightDto;

import java.util.List;
import java.util.Optional;

public interface FlightService {
   void createDbFlight(FlightDto FlightDto);

   Optional<FlightDto> findById(Long id);

   void deleteFlight(Long id);

   List<FlightDto> displayAll();

   List<FlightDto> getByStartDest(String startName);


   List <FlightDto> getByQueryParam(String companyName, Long maxLength);
}
