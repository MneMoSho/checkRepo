package com.example.checkrepo.controllersTest;

import com.example.checkrepo.controller.UserController;
import com.example.checkrepo.dto.UserDto;
import com.example.checkrepo.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UniTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserServiceImpl userService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreateNewCompany() throws Exception {
        UserDto userDto = UserDto.builder().id(1L).userName("DDDDDD").email("GFGGGGg").flightDtos(null).build();
        String UserJson = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                .content(UserJson)).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testDisplayUser() throws Exception {
        Long userId = 1L;
        UserDto userDto = UserDto.builder()
                .id(userId)
                .userName("testUser")
                .email("test@example.com")
                .build();
        when(userService.getUserById(userId)).thenReturn(userDto);
        mockMvc.perform(get("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    public void getAllUsers() throws Exception {
        List<UserDto> newUserList = List.of(
                new UserDto(1L, "AAA", "BBB", new HashSet<>()),
                new UserDto(2L, "BBB", "YYYY", new HashSet<>())
        );
        when(userService.getAllUsers()).thenReturn(newUserList);
        mockMvc.perform(get("/api/users") // Adjust URL if necessary
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("AAA"))
                .andExpect(jsonPath("$[1].username").value("BBB"));
    }

    @Test
    public void deleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/2")).andExpect(status().is2xxSuccessful());
    }
}
