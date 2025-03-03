package com.example.checkrepo.services;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void createUser(UserDto userDto);

    UserDto addingNewFlight(Long flightId, Long userId);

    Optional<UserDto> getUserById(Long id);
}
