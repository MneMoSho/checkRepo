package com.example.checkrepo.service.impl;

import com.example.checkrepo.dto.UserDto;
import com.example.checkrepo.entities.Flight;
import com.example.checkrepo.entities.User;
import com.example.checkrepo.exception.ObjectNotFoundException;
import com.example.checkrepo.mapper.UserMapper;
import com.example.checkrepo.repository.FlightRep;
import com.example.checkrepo.repository.UserRepository;
import com.example.checkrepo.service.UserService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
        User newUser = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("not found"));
        Flight newFlight = flightRep.findById(flightId)
                .orElseThrow(() -> new ObjectNotFoundException("not found"));
        newUser.getFlights().add(newFlight);
        userRepository.save(newUser);
        return UserMapper.toUserDto(newUser);
    }

    @Override
    @Transactional
    public UserDto getUserById(Long id) {
        User newUser;
        if (userRepository.existsById(id)) {
            newUser = userRepository.findById(id).get();
            return UserMapper.toUserDto(newUser);
        } else {
            throw new ObjectNotFoundException("not found");
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        if (userRepository.findAll().isEmpty()) {
            throw new ObjectNotFoundException("nothing can be found");
        }
        return UserMapper.toDtoList(userRepository.findAll());
    }

    @Override
    public void deleteById(Long id) {
        userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("not found"));
        userRepository.deleteById(id);
    }
}
