package com.example.checkrepo.service;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.dto.UserDto;
import com.example.checkrepo.entities.User;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto addingNewFlight(Long flightId, Long userId);

    UserDto getUserById(Long id);

    List<UserDto> getAllUsers();

    void deleteById(Long id);

    List<UserDto> findByEndDest(List<String> endDestinations);

    UserDto findExistingUser(UserDto searchUser);

    List<FlightDto> findUserFlights(UserDto user);

    UserDto detachFlightFromUser(Long flightId, UserDto user);

    void bookFlightToUser(UserDto user, Long flightId);
}
