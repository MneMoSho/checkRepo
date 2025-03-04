package com.example.checkrepo.mapper;

import com.example.checkrepo.dto.UserDto;
import com.example.checkrepo.entities.User;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@UtilityClass
public class UserMapper {
    public UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUserName(user.getUserName());
        userDto.setEmail(user.getEmail());
        System.out.println("I'm there");
        if(user.getFlights() != null) {
            userDto.setFlightDtos(user.getFlights().stream().map(FlightMapper::toDtoShallow).collect(Collectors.toSet()));
        }
        return userDto;
    }

    public UserDto toDtoShallow(User user) {
        UserDto dto = new UserDto();
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public User toUser(UserDto userDto) {
        User user= new User();
        user.setUserName(userDto.getUserName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public List<UserDto> toDtoList(List<User> userList) {
        List<UserDto> dtoList = new ArrayList<>();
        for(User source : userList) {
            dtoList.add(UserMapper.toUserDto(source));
        }
        return dtoList;
    }

    public List<User> toEntityList(List<UserDto> userDtoList) {
        List<User> list = new ArrayList<>();
        for(UserDto source : userDtoList) {
            list.add(UserMapper.toUser(source));
        }
        return list;
    }
}
