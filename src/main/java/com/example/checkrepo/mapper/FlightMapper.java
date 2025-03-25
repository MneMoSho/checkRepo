package com.example.checkrepo.mapper;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.entities.Company;
import com.example.checkrepo.entities.Flight;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FlightMapper {

    public FlightDto toFlightDto(Flight flight) {
        FlightDto dto = new FlightDto();
        dto.setId(flight.getId());
        dto.setLength(flight.getLength());
        dto.setStartDestination(flight.getStartDestination());
        dto.setEndDestination(flight.getEndDestination());
        dto.setCompanyId(flight.getCompany().getId());
        if (flight.getCompany() != null) {
            dto.setFlightCompany(flight.getCompany().getCompanyName());
        }
        if (flight.getUsers() != null) {
            dto.setUserDtos(flight.getUsers().stream()
                    .map(UserMapper::toUserDtoShallow).collect(Collectors.toSet()));
        }
        return dto;
    }

    public FlightDto toFlightDtoShallow(Flight flight) {
        FlightDto dto = new FlightDto();
        dto.setId(flight.getId());
        dto.setStartDestination(flight.getStartDestination());
        dto.setEndDestination(flight.getEndDestination());
        dto.setCompanyId(flight.getCompany().getId());
        if (flight.getCompany() != null) {
            dto.setFlightCompany(flight.getCompany().getCompanyName());
        }
        dto.setLength(flight.getLength());
        return dto;
    }

    public Flight toEntity(FlightDto flightDto) {
        Flight flight = new Flight();
        flight.setId(flightDto.getId());
        flight.setLength(flightDto.getLength());
        flight.setStartDestination(flightDto.getStartDestination());
        flight.setEndDestination(flightDto.getEndDestination());
        Company newCompany = new Company();
        newCompany.setCompanyName(flightDto.getFlightCompany());
        newCompany.setId(flightDto.getCompanyId());
        if (flightDto.getUserDtos() != null) {
            flight.setUsers(flightDto.getUserDtos().stream()
                    .map(UserMapper::toUserShallow).collect(Collectors.toSet()));
        }
        return flight;
    }

    public Flight toEntityShallow(FlightDto flightDto) {
        Flight flight = new Flight();
        flight.setId(flightDto.getId());
        flight.setLength(flightDto.getLength());
        flight.setStartDestination(flightDto.getStartDestination());
        flight.setEndDestination(flightDto.getEndDestination());
        Company newCompany = new Company();
        newCompany.setCompanyName(flightDto.getFlightCompany());
        newCompany.setId(flightDto.getCompanyId());
        flight.setCompany(newCompany);
        return flight;
    }

    public List<FlightDto> toDtoList(List<Flight> flightsList) {
        List<FlightDto> dtoList = new ArrayList<>();
        for (Flight source : flightsList) {
            dtoList.add(FlightMapper.toFlightDto(source));
        }
        return dtoList;
    }

    public List<FlightDto> toDtoListShallow(List<Flight> flightsList) {
        List<FlightDto> dtoList = new ArrayList<>();
        for (Flight source : flightsList) {
            dtoList.add(FlightMapper.toFlightDtoShallow(source));
        }
        return dtoList;
    }

    public List<Flight> toEntityList(List<FlightDto> flightDtoList) {
        List<Flight> list = new ArrayList<>();
        for (FlightDto source : flightDtoList) {
            list.add(FlightMapper.toEntity(source));
        }
        return list;
    }
}
