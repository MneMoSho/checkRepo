package com.example.checkrepo.service.impl;

import com.example.checkrepo.dto.CompanyDto;
import com.example.checkrepo.dto.UserDto;
import com.example.checkrepo.entities.Flight;
import com.example.checkrepo.entities.User;
import com.example.checkrepo.exception.ObjectNotFoundException;
import com.example.checkrepo.mapper.CompanyMapper;
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
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FlightRep flightRep;
    private final Cache cache;

    @Override
    public void createUser(UserDto userDto) {
       User saveUser = UserMapper.toUser(userDto);
       userRepository.save(saveUser);
       cache.putUser(saveUser.getId(), UserMapper.toUserDto(saveUser));
    }

    @Override
    public UserDto addingNewFlight(Long flightId, Long userId) {
        User newUser = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User was not found"));
        Flight newFlight = flightRep.findById(flightId)
                .orElseThrow(() -> new ObjectNotFoundException("Flight was not found"));
        newUser.getFlights().add(newFlight);
        userRepository.save(newUser);
        cache.updateUser(newUser.getId(), UserMapper.toUserDto(newUser));
        return UserMapper.toUserDto(newUser);
    }

    @Override
    @Transactional
    public UserDto getUserById(Long id) {
        UserDto newUser =cache.getUser(id);
        if (newUser == null) {
            if(userRepository.existsById(id)) {
                System.out.println("not From cache");
                newUser = UserMapper.toUserDto(userRepository.findById(id).get());
                cache.putUser(id, newUser);
                return newUser;
            } else {
                throw new ObjectNotFoundException("User is not found");
            }
        } else {
            System.out.println("From cache");
            return newUser;
        }
    }

    @Override
    public List<UserDto> getAllUsers() {


        Collection<UserDto> allUsers = cache.getAllUsers();
        if (!allUsers.isEmpty() && allUsers.size() == userRepository.count()) {
            return new ArrayList<>(allUsers);
        } else {
            List<UserDto> users = userRepository.findAll().stream().map(UserMapper::toUserDto).toList();
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
    public void deleteById(Long id) {
        userRepository.findById(id).orElseThrow(()
                -> new ObjectNotFoundException("wrong enter, user can't be deleted"));
        userRepository.deleteById(id);
        if(cache.getUser(id) != null) {
            cache.deleteUser(id);
        }
    }

    @Override
    public List<UserDto> findByEndDest(List<String> endDestinations) {
       // List<User> findByEnd = userRepository.findAll();

        List<User> findByEnd = UserMapper.toEntityList(cache.getAllUsers().stream().toList());

        if(findByEnd.isEmpty()) {
            System.out.println("AAAAAAAAAAA");
            return null;
        }
//#######################################################################################
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
    public List<UserDto> findByStartDestNative(String startDest) {
        return UserMapper.toDtoListShallow(userRepository.findByDestNative(startDest));
    }

    @Override
    public List<UserDto> findByStartDestJPQL(String startDest) {
        return UserMapper.toDtoListShallow(userRepository.findByDestJPQL(startDest));
    }
}
