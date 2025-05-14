//package com.example.checkrepo.service.impl;
//
//import com.example.checkrepo.dto.UserDto;
//import com.example.checkrepo.entities.Company;
//import com.example.checkrepo.entities.Flight;
//import com.example.checkrepo.entities.User;
//import com.example.checkrepo.exception.ObjectNotFoundException;
//import com.example.checkrepo.mapper.UserMapper;
//import com.example.checkrepo.repository.FlightRep;
//import com.example.checkrepo.repository.UserRepository;
//import com.example.checkrepo.service.cache.Cache;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import java.util.*;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class UserServiceImplTest {
//    @Mock
//    private FlightRep flightRep;
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private Cache cache;
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//    private MockMvc mockMvc;
//    private ObjectMapper objectMapper;
//
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        this.mockMvc = MockMvcBuilders.standaloneSetup(userService).build();
//        objectMapper = new ObjectMapper();
//    }
//
//    @Test
//    void createUser_shouldSaveUserAndPutInCache() {
//        // Arrange
//        UserDto userDto = new UserDto(1L, "Test User", "test@example.com", "AAA" null);
//        User userToSave = new User();
//        userToSave.setId(1L);
//        userToSave.setUserName("Test User");
//        userToSave.setEmail("test@example.com");
//        when(userRepository.save(userToSave)).thenReturn(userToSave);
//        userService.createUser(userDto);
//        verify(userRepository, times(1)).save(userToSave);
//    }
//
//    @Test
//    public void shouldAddNewFlight() {
//        // Arrange
//        Long userId = 1L;
//        Long flightId = 1L;
//        Company newCompany = new Company(1L, "AAAAA", new HashSet<>());
//        Flight newFlight = new Flight(1L, 222, "DDDDDDDDD", "SSSSSSS", newCompany, new HashSet<>());
//        Set<Flight> flightSet = new HashSet<>();
//        User newUser = new User(userId, "AAAA", "DDDD", flightSet);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(newUser));
//        when(flightRep.findById(flightId)).thenReturn(Optional.of(newFlight));
//        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        // Act
//        UserDto result = userService.addingNewFlight(flightId, userId);
//
//        // Assert
//        verify(userRepository, times(1)).save(newUser);
//        assertNotNull(result);
//        assertEquals(userId, result.getId());
//        assertTrue(newUser.getFlights().contains(newFlight));
//    }
//
//    @Test
//    public void shouldReturnById() {
//        // Arrange
//        Long userId = 1L;
//        when(cache.getUser(userId)).thenReturn(null);
//        when(userRepository.existsById(userId)).thenReturn(true);
//        User userFromRepo = new User(userId, "TestName", "email", new HashSet<>());
//        UserDto userDtoFromMapper = new UserDto(userId, "TestName", "email", new HashSet<>());
//        when(userRepository.findById(userId)).thenReturn(Optional.of(userFromRepo));
//        UserDto result = userService.getUserById(userId);
//        assertNotNull(result);
//        assertEquals(userId, result.getId());
//        assertEquals("TestName", result.getUserName());
//        assertEquals("email", result.getEmail());
//        verify(userRepository, times(1)).existsById(userId);
//        verify(userRepository, times(1)).findById(userId);
//    }
//
//    @Test
//    public void shouldThrowObjectNotFoundExceptionWhenUserNotFound() {
//        Long userId = 1L;
//        when(cache.getUser(userId)).thenReturn(null);
//        when(userRepository.existsById(userId)).thenReturn(false);
//        ObjectNotFoundException thrown = assertThrows(
//                ObjectNotFoundException.class,
//                () -> userService.getUserById(userId),
//                "Expected getUserById() to throw ObjectNotFoundException, but it didn't"
//        );
//        assertEquals("User is not found", thrown.getMessage());
//        verify(userRepository, times(1)).existsById(userId);
//        verify(userRepository, never()).findById(userId);
//    }
//
//    @Test
//    public void getAllUsers_cacheMiss_emptyCache() {
//        // Arrange
//        when(cache.getAllUsers()).thenReturn(Collections.emptyList());
//        when(userRepository.count()).thenReturn(2L); // Corrected the return type
//        List<User> usersFromRepo = Arrays.asList(
//                new User(1L, "AAA", "BBB", new HashSet<>()),
//                new User(2L, "BBB", "YYYY", new HashSet<>())
//        );
//        when(userRepository.findAll()).thenReturn(usersFromRepo);
//
//        List<UserDto> result = userService.getAllUsers();
//        assert(result.size() == 2);
//        assert(result.get(0).getUserName()).equals("AAA");
//    }
//
//    @Test
//    public void getAllUsers_cacheHit() {
//        // Arrange
//        List<UserDto> cachedUsers = Arrays.asList(
//                new UserDto(1L, "AAA", "BBB", new HashSet<>()),
//                new UserDto(2L, "BBB", "YYYY", new HashSet<>())
//        );
//        when(cache.getAllUsers()).thenReturn(cachedUsers);
//        when(userRepository.count()).thenReturn((long) cachedUsers.size());
//
//        // Act
//        List<UserDto> result = userService.getAllUsers();
//
//        // Assert
//        verify(cache, times(1)).getAllUsers();
//        verify(userRepository, times(1)).count();
//        verify(userRepository, never()).findAll(); // Ensure findAll is not called
//        assert(result).equals(cachedUsers);
//    }
//
//    @Test
//    public void deleteById_userExists_cacheHit() {
//        // Arrange
//        Long userId = 1L;
//        User user = new User(userId, "testUser", "password", new HashSet<>());
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(cache.getUser(userId)).thenReturn(new UserDto(userId, "testUser", "password", null)); // Return a non-null value
//
//        userService.deleteById(userId);
//
//        verify(userRepository, times(1)).findById(userId);
//        verify(userRepository, times(1)).deleteById(userId);
//        verify(cache, times(1)).getUser(userId);
//        verify(cache, times(1)).deleteUser(userId);
//    }
//
//    @Test
//    public void deleteById_userExists_cacheMiss() {
//        // Arrange
//        Long userId = 1L;
//        User user = new User(userId, "testUser", "password", new HashSet<>());
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(cache.getUser(userId)).thenReturn(null); // Return null for cache miss
//
//        // Act
//        userService.deleteById(userId);
//
//        // Assert
//        verify(userRepository, times(1)).findById(userId);
//        verify(userRepository, times(1)).deleteById(userId);
//        verify(cache, times(1)).getUser(userId);
//        verify(cache, never()).deleteUser(userId); // Ensure deleteUser is not called
//    }
//
//    @Test
//    public void deleteById_userNotFound() {
//        // Arrange
//        Long userId = 1L;
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(ObjectNotFoundException.class, () -> userService.deleteById(userId));
//        verify(userRepository, times(1)).findById(userId);
//        verify(userRepository, never()).deleteById(anyLong()); // Ensure deleteById is not called
//        verify(cache, never()).getUser(anyLong());
//        verify(cache, never()).deleteUser(anyLong());
//    }
//
//    @Test
//    void shouldFindByEndDestWithEmptyCache() {
//        // Arrange
//        List<String> endDestinations = List.of("London");
//        when(cache.getAllUsers()).thenReturn(Collections.emptyList());
//        when(userRepository.count()).thenReturn(2L);
//        Flight flight1 = new Flight(1L, 100, "Berlin", "London", new Company(), new HashSet<>());
//        Set<Flight> flights1 = Set.of(flight1);
//        User user1 = new User(1L, "Alice", "alice@example.com", flights1);
//        List<User> usersFromRepo = List.of(user1, new User(2L, "Bob", "bob@example.com", new HashSet<>()));
//        List<UserDto> expectedDto = List.of(new UserDto(1L, "Alice", "alice@example.com", Set.of()));
//        when(userRepository.findAll()).thenReturn(usersFromRepo);
//        // Act
//        List<UserDto> result = userService.findByEndDest(endDestinations);
//        // Assert
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals("Alice", result.getFirst().getUserName());
//
//        // Ожидаем, что count() НЕ будет вызван, а findAll() будет вызван
//        verify(userRepository, never()).count();
//        verify(userRepository, times(1)).findAll();
//        verify(cache, times(1)).getAllUsers();
//    }
//
//    @Test
//    void findByEndDest_cacheHit_notEmptyCache() {
//        // Arrange
//        List<String> endDestinations = List.of("London");
//        Flight flight1 = new Flight(1L, 100, "Berlin", "London", new Company(), new HashSet<>());
//        Set<Flight> flights1 = Set.of(flight1);
//        User cachedUser1 = new User(1L, "Alice", "alice@example.com", flights1);
//        List<User> cachedUsers = List.of(cachedUser1, new User(2L, "Bob", "bob@example.com", new HashSet<>()));
//        List<UserDto> expectedDto = List.of(new UserDto(1L, "Alice", "alice@example.com", Set.of()));
//        when(cache.getAllUsers()).thenReturn(UserMapper.toDtoList(cachedUsers));
//        when(userRepository.count()).thenReturn((long) cachedUsers.size());
//
//        // Act
//        List<UserDto> result = userService.findByEndDest(endDestinations);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals("Alice", result.getFirst().getUserName());
//
//        verify(cache, times(1)).getAllUsers();
//        verify(userRepository, times(1)).count();
//        verify(userRepository, never()).findAll();
//    }
//
//}