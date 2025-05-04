package com.example.checkrepo.service;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.dto.UserDto;

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

   // void getFromExcel() throws IOException;

    //void restartSequence(int restartIndex);

    List<FlightDto> bulkOperation(List<String> companies);

    List<FlightDto> postFlights(List<FlightDto> flightDtos);

    int findMinPrice();

    List<FlightDto> findByCountry();

    List<FlightDto> findFromFront(FlightDto destination);
}
