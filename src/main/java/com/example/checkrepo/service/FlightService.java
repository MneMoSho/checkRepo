package com.example.checkrepo.service;

import com.example.checkrepo.dto.FlightDto;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface FlightService {
    void createDbFlight(FlightDto flightDto);

    Optional<FlightDto> findById(Long id);

    void deleteFlight(Long id);

    List<FlightDto> displayAll();

    List<FlightDto> getByStartDestNative(String startName);

    List<FlightDto> getByStartDestJpql(String startName);

    void getFromExcel() throws IOException;

    void restartSequence(int restartIndex);

    List<FlightDto> bulkOperation(List<String> companies);

}
