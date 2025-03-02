package com.example.checkrepo.mapper;

import com.example.checkrepo.dto.UserDto;
import com.example.checkrepo.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "flightDtos", source="flights")
    UserDto toUserDto(User user);
    @Mapping(target = "flights", ignore = true)
    User toUser(UserDto userDto);
}
