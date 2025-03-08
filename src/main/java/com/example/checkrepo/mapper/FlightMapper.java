package com.example.checkrepo.mapper;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.entities.Flight;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class FlightMapper {

    public FlightDto toFlightDto(Flight flight) {
        FlightDto dto = new FlightDto();
        dto.setId(flight.getId());
        dto.setLength(flight.getLength());
        dto.setStartDestination(flight.getStartDestination());
        dto.setEndDestination(flight.getEndDestination());
        System.out.println(flight.getId());
        if(flight.getUsers() != null) {
            dto.setUserDtos(flight.getUsers().stream().map(UserMapper::toUserDtoShallow).collect(Collectors.toSet()));
        }
        return dto;
    }

    public FlightDto toFlightDtoShallow(Flight flight) {
        FlightDto dto = new FlightDto();
        dto.setId(flight.getId());
        dto.setStartDestination(flight.getStartDestination());
        dto.setEndDestination(flight.getEndDestination());
        dto.setLength(flight.getLength());
        return dto;
    }

    public Flight toEntity(FlightDto flightDto) {
        Flight flight= new Flight();
        flight.setLength(flightDto.getLength());
        flight.setStartDestination(flightDto.getStartDestination());
        flight.setEndDestination(flightDto.getEndDestination());
        flight.setId(flightDto.getId());
        return flight;
    }

    public List<FlightDto> toDtoList(List<Flight> flightsList) {
        List<FlightDto> dtoList = new ArrayList<>();
        for(Flight source : flightsList) {
            dtoList.add(FlightMapper.toFlightDto(source));
        }
        return dtoList;
    }

    public List<Flight> toEntityList(List<FlightDto> flightDtoList) {
        List<Flight> list = new ArrayList<>();
        for(FlightDto source : flightDtoList) {
            list.add(FlightMapper.toEntity(source));
        }
        return list;
    }
}
