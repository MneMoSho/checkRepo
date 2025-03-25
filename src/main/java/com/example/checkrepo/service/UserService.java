package com.example.checkrepo.service;

import com.example.checkrepo.dto.UserDto;
import java.util.List;

public interface UserService {
    void createUser(UserDto userDto);

    UserDto addingNewFlight(Long flightId, Long userId);

    UserDto getUserById(Long id);

    List<UserDto> getAllUsers();

    void deleteById(Long id);

    List<UserDto> findByEndDest(List<String> endDestinations);

    List<UserDto> findByStartDestNative(String startDest);

    List<UserDto> findByStartDestJpql(String startDest);
}
