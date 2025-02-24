package com.example.checkrepo.mapper;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.entities.Flight;
import java.util.ArrayList;
import java.util.List;

public class FlightMapper {
    public static FlightDto mapToFlightDto(Flight flight) {
        return new FlightDto(
                flight.getId(),
                flight.getLength(),
                flight.getEndDestination(),
                flight.getStartDestination());
    }

    public static Flight mapToFlight(FlightDto flightDto) {
        return new Flight(
                flightDto.getId(),
                flightDto.getLength(),
                flightDto.getEndDestination(),
                flightDto.getStartDestination()
        );
    }

    public static List<FlightDto> convertDto(List<Flight> list) {
        List<FlightDto> flightDtoList = new ArrayList<>();
        for (Flight source : list) {
            flightDtoList.add(FlightMapper.mapToFlightDto(source));
        }
        return flightDtoList;
    }
}


