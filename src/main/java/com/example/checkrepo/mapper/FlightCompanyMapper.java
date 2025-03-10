package com.example.checkrepo.mapper;

import com.example.checkrepo.dto.FlightCompanyDto;
import com.example.checkrepo.entities.FlightCompany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FlightCompanyMapper {

    public FlightCompanyDto toFlightCompanyDto(FlightCompany flightCompany) {
        FlightCompanyDto dto = new FlightCompanyDto();
        dto.setCompanyId(flightCompany.getId());
        dto.setCompanyName(flightCompany.getCompanyName());
        if (flightCompany.getFlights() != null) {
            dto.setFlights(flightCompany.getFlights().stream()
                            .map(FlightMapper::toFlightDto).collect(Collectors.toSet()));
        }
        return dto;
    }

    public FlightCompanyDto toDtoShallow(FlightCompany flightCompany) {
        FlightCompanyDto dto = new FlightCompanyDto();
        dto.setCompanyId(flightCompany.getId());
        dto.setCompanyName(flightCompany.getCompanyName());
        return dto;
    }

    public FlightCompany toEntity(FlightCompanyDto flightCompanyDto) {
        FlightCompany flightCompany = new FlightCompany();
        flightCompany.setCompanyName(flightCompanyDto.getCompanyName());
        flightCompany.setId(flightCompanyDto.getCompanyId());
        return flightCompany;
    }

    public List<FlightCompanyDto> toDtoList(List<FlightCompany> flightCompanysList) {
        List<FlightCompanyDto> dtoList = new ArrayList<>();
        for (FlightCompany source : flightCompanysList) {
            dtoList.add(FlightCompanyMapper.toFlightCompanyDto(source));
        }
        return dtoList;
    }

    public List<FlightCompany> toEntityList(List<FlightCompanyDto> flightCompanyDtoList) {
        List<FlightCompany> list = new ArrayList<>();
        for (FlightCompanyDto source : flightCompanyDtoList) {
            list.add(FlightCompanyMapper.toEntity(source));
        }
        return list;
    }
}