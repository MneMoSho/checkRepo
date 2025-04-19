package com.example.checkrepo.mapper;

import com.example.checkrepo.dto.UserDto;
import com.example.checkrepo.entities.User;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

@Getter
@Setter
@UtilityClass
public class UserMapper {
    public UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUserName(user.getUserName());
        userDto.setEmail(user.getEmail());
        if (user.getFlights() != null) {
            userDto.setFlightDtos(user.getFlights().stream()
                    .map(FlightMapper::toFlightDtoShallow).collect(Collectors.toSet()));
        }
        return userDto;
    }

    public UserDto toUserDtoShallow(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public User toUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUserName(userDto.getUserName());
        user.setEmail(userDto.getEmail());
        if (user.getFlights() != null) {
            System.out.println("not empty");
            user.setFlights(userDto.getFlights().stream()
                    .map(FlightMapper::toEntity).collect(Collectors.toSet()));
        } else {
            System.out.println("Emptuy");
        }
        return user;
    }

    public User toUserShallow(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUserName(userDto.getUserName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public List<UserDto> toDtoList(List<User> userList) {
        List<UserDto> dtoList = new ArrayList<>();
        for (User source : userList) {
            dtoList.add(UserMapper.toUserDto(source));
        }
        return dtoList;
    }

    public List<UserDto> toDtoListShallow(List<User> userList) {
        List<UserDto> dtoList = new ArrayList<>();
        for (User source : userList) {
            dtoList.add(UserMapper.toUserDtoShallow(source));
        }
        return dtoList;
    }

    public List<User> toEntityList(List<UserDto> userDtoList) {
        List<User> list = new ArrayList<>();
        for (UserDto source : userDtoList) {
            list.add(UserMapper.toUser(source));
        }
        return list;
    }
}
