package com.example.checkrepo.service;

import com.example.checkrepo.dto.FlightDto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface FlightService {
    void createDbFlight(FlightDto flightDto);

    Optional<FlightDto> findById(Long id);

    void deleteFlight(Long id);

    List<FlightDto> displayAll();

    List<FlightDto> getByStartDest(String startName);

    List<FlightDto> getByQueryParam(String companyName, Long maxLength);

    void getFromExcel() throws IOException;
}
