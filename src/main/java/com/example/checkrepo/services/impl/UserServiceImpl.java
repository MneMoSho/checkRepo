package com.example.checkrepo.services.impl;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.dto.UserDto;
import com.example.checkrepo.entities.Flight;
import com.example.checkrepo.entities.User;
import com.example.checkrepo.mapper.UserMapper;
import com.example.checkrepo.repository.FlightRep;
import com.example.checkrepo.repository.UserRepository;
import com.example.checkrepo.services.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FlightRep flightRep;

    @Override
    public void createUser(UserDto userDto) {
        userRepository.save(UserMapper.toUser(userDto));
    }

    @Override
    public UserDto addingNewFlight(Long flightId, Long userId) {
        User newUser = userRepository.findById(userId).get();
        Flight newFlight = flightRep.findById(flightId).get();
        newUser.getFlights().add(newFlight);
        userRepository.save(newUser);
        return UserMapper.toUserDto(newUser);
    }

    @Override
    @Transactional
    public UserDto getUserById(Long id) {
        User newUser;
        if(userRepository.existsById(id)) {
            newUser = userRepository.findById(id).get();
            return UserMapper.toUserDto(newUser);
        }
        return null;
    }

    @Override
    public List<UserDto> getAllUsers() {
       return UserMapper.toDtoList(userRepository.findAll());
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
