package com.example.checkrepo.services.impl;

import com.example.checkrepo.dto.UserDto;
import com.example.checkrepo.entities.Flight;
import com.example.checkrepo.entities.User;
import com.example.checkrepo.mapper.UserMapper;
import com.example.checkrepo.repository.FlightRep;
import com.example.checkrepo.repository.UserRepository;
import com.example.checkrepo.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FlightRep flightRep;

    @Override
    public void createUser(UserDto userDto) {
        userRepository.save(userMapper.toUser(userDto));
    }

    @Override
    public UserDto addingNewFlight(int flightId, int userId) {
        User newUser = userRepository.findById(userId).get();
        Flight newFlight = flightRep.findById(flightId).get();
        newUser.getFlights().add(newFlight);
        userRepository.save(newUser);
        return userMapper.toUserDto(newUser);
    }

    @Override
    public UserDto getUserById(int id) {
       return userMapper.toUserDto(userRepository.findById(id).get());
    }
}
