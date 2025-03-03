package com.example.checkrepo.mapper;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.dto.UserDto;
import com.example.checkrepo.entities.Flight;
import com.example.checkrepo.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FlightMapper {
    @Mapping(target="users", ignore = true)
    Flight toFlight(FlightDto flightDto);
    @Mapping(target="userDtos", source="users")
    FlightDto toFlightDto(Flight flight);
    List<UserDto> toDtoList(List<User> users);
    List<User> toList(List<UserDto> userDtos);
}


