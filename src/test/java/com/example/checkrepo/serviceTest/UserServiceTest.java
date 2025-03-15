package com.example.checkrepo.serviceTest;

import com.example.checkrepo.dto.UserDto;
import com.example.checkrepo.entities.Company;
import com.example.checkrepo.entities.Flight;
import com.example.checkrepo.entities.User;
import com.example.checkrepo.mapper.UserMapper;
import com.example.checkrepo.repository.FlightRep;
import com.example.checkrepo.repository.UserRepository;
import com.example.checkrepo.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private FlightRep flightRep;
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserServiceImpl userService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userService).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldAddNewFlight() {
        Long userId = 1L;
        Long flightId = 1L;
        Company newCompany = new Company(1L, "AAAAA", new HashSet<>());
        Flight newFlight = new Flight(1L, 222, "DDDDDDDDD", "SSSSSSS", newCompany, new HashSet<>());
        Set<Flight> flightSet= new HashSet<>();
        flightSet.add(newFlight);
        User newUser = new User(userId, "AAAA", "DDDD", flightSet);
        when(userRepository.findById(flightId)).thenReturn(Optional.of(newUser));
        when(flightRep.findById(flightId)).thenReturn(Optional.of(newFlight));
        when(userRepository.save(newUser)).thenReturn(newUser);
        UserDto result = userService.addingNewFlight(flightId, userId);
        verify(userRepository, times(1)).save(newUser);
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertTrue(newUser.getFlights().contains(newFlight));
    }
}
