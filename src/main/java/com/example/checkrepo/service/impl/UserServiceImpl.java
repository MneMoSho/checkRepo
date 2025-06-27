package com.example.checkrepo.service.impl;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.dto.UserDto;
import com.example.checkrepo.entities.Flight;
import com.example.checkrepo.entities.User;
import com.example.checkrepo.exception.ObjectNotFoundException;
import com.example.checkrepo.mapper.FlightMapper;
import com.example.checkrepo.mapper.UserMapper;
import com.example.checkrepo.repository.FlightRep;
import com.example.checkrepo.repository.UserRepository;
import com.example.checkrepo.service.UserService;
import com.example.checkrepo.service.cache.Cache;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FlightRep flightRep;
    private final Cache cache;


    @CachePut(value="USER_CACHE", key ="#result.id")
    @Override
    public UserDto createUser(UserDto userDto) {
        User saveUser = UserMapper.toUserShallow(userDto);
        userRepository.save(saveUser);
        cache.putUser(saveUser.getId(), UserMapper.toUserDto(saveUser));
        return UserMapper.toUserDto(saveUser);
    }

    @Override
    @Transactional
    public UserDto addingNewFlight(Long flightId, Long userId) {
        User newUser = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User was not found"));
        Flight newFlight = flightRep.findById(flightId)
                .orElseThrow(() -> new ObjectNotFoundException("Flight was not found"));
        newUser.getFlights().add(newFlight);
        newFlight.getUsers().add(newUser);

        flightRep.save(newFlight);
        userRepository.save(newUser);

        cache.updateUser(newUser.getId(), UserMapper.toUserDto(newUser));
        cache.updateFlight(newFlight.getId(), FlightMapper.toFlightDto(newFlight));
        return UserMapper.toUserDto(newUser);
    }

    @Override
    @Transactional
    @Cacheable(value="USER_CACHE", key ="#id")
    public UserDto getUserById(Long id) {
        UserDto newUser = cache.getUser(id);
        if (newUser == null) {
            if (userRepository.existsById(id)) {
                newUser = UserMapper.toUserDto(userRepository.findById(id).get());
                cache.putUser(id, newUser);
                return newUser;
            } else {
                throw new ObjectNotFoundException("User is not found");
            }
        } else {
            return newUser;
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        Collection<UserDto> allUsers = cache.getAllUsers();
        if (!allUsers.isEmpty() && allUsers.size() == userRepository.count()) {
            return new ArrayList<>(allUsers);
        } else {
            List<UserDto> users = userRepository.findAll()
                    .stream().map(UserMapper::toUserDto).toList();
            if (!allUsers.isEmpty()) {
                for (UserDto source : users) {
                    if (users.stream().noneMatch(user -> user.getId().equals(source.getId()))) {
                        cache.putUser(source.getId(), source);
                    }
                }
            } else {
                users.forEach(user -> cache.putUser(user.getId(), user));
            }
            return users;
        }
    }

    @Override
    @CacheEvict(value="USER_CACHE", key ="#id")
    public void deleteById(Long id) {
        userRepository.findById(id).orElseThrow(()
                -> new ObjectNotFoundException("wrong enter, user can't be deleted"));
        userRepository.deleteById(id);
        if (cache.getUser(id) != null) {
            cache.deleteUser(id);
        }
    }

    @Override
    @Cacheable(value="USER_CACHE", key ="#endDestinations")
    public List<UserDto> findByEndDest(List<String> endDestinations) {
        List<User> findByEnd = UserMapper.toEntityList(cache.getAllUsers().stream().toList());

        if (findByEnd.isEmpty() || findByEnd.size() != userRepository.count()) {
            findByEnd = userRepository.findAll();
        }
        List<User> foundUsers = new ArrayList<>();
        for (String endDestination : endDestinations) {
            List<User> bufList = findByEnd.stream().filter(
                    user -> user.getFlights().stream().anyMatch(
                            flight -> flight.getEndDestination()
                                    .equals(endDestination))).toList();
            foundUsers.addAll(bufList);
        }
        return UserMapper.toDtoList(foundUsers);
    }

    @Override
    @Cacheable(value="USER_CACHE", key ="#searchUser.id")
    public UserDto findExistingUser(UserDto searchUser) {
        List<User> allUsers = userRepository.findAll();
        for(User user: allUsers) {
            if(user.getPassword().equals(searchUser.getPassword()) && user.getUserName().equals(searchUser.getUserName())) {
                return UserMapper.toUserDto(user);
            }
        }
        throw new ObjectNotFoundException("User doesn't exist");
    }

    @Override
    public List<FlightDto> findUserFlights(UserDto user) {
        System.out.println(user.getUserName());
        User foundUser = userRepository.findAll().stream()
                .filter(userSearch -> userSearch.getUserName().equals(user.getUserName()))
                .filter(userSearch -> userSearch.getPassword().equals(user.getPassword()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Flight> userFlights = foundUser.getFlights().stream().toList();

        for(Flight flight : userFlights) {
            System.out.println(flight.getStartDestination());
        }

        return FlightMapper.toDtoList(userFlights);
    }

    @Override
    @Transactional
    public UserDto detachFlightFromUser(Long flightId, UserDto user) {
        User foundUser = userRepository.findAll().stream()
                .filter(userSearch -> userSearch.getUserName().equals(user.getUserName()))
                .filter(userSearch -> userSearch.getPassword().equals(user.getPassword()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println(foundUser.getUserName());

        Flight flightToRemove = flightRep.findById(flightId)
                .orElseThrow(() -> new ObjectNotFoundException("Flight not found"));

        System.out.println(flightToRemove.getStartDestination());
        foundUser.getFlights().remove(flightToRemove);
        flightToRemove.getUsers().remove(foundUser);
        userRepository.save(foundUser);
        flightRep.save(flightToRemove);
        cache.updateUser(user.getId(), UserMapper.toUserDto(foundUser));
        cache.updateFlight(flightToRemove.getId(), FlightMapper.toFlightDto(flightToRemove));
        return UserMapper.toUserDto(foundUser);
    }

    @Override
    public void bookFlightToUser(UserDto user, Long flightId) {
        User foundUser = userRepository.findAll().stream()
                .filter(userSearch -> userSearch.getUserName().equals(user.getUserName()))
                .filter(userSearch -> userSearch.getPassword().equals(user.getPassword()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found"));

       addingNewFlight(flightId, foundUser.getId());
    }
}
