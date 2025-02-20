package com.example.checkrepo.mapper;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.entities.Flight;

public class FlightMapper {//конвертирует из FlightDTO в Flight
    public static FlightDto mapToFlightDto(Flight flight) {
        return new FlightDto(
                flight.getId(),
                flight.getLength(),
                flight.getEndDestination()
        );
    };
    public static Flight mapToFlight(FlightDto flightDto) {
        return new Flight(
                flightDto.getId(),
                flightDto.getLength(),
                flightDto.getEndDestination()
        );
    };
}


