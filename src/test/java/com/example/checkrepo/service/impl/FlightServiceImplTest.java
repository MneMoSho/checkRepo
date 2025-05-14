//package com.example.checkrepo.service.impl;
//
//import com.example.checkrepo.dto.FlightDto;
//import com.example.checkrepo.entities.Flight;
//import com.example.checkrepo.exception.IncorrectInputException;
//import com.example.checkrepo.mapper.FlightMapper;
//import jakarta.persistence.EntityManager;
//import org.junit.jupiter.api.Test;
//import com.example.checkrepo.entities.Company;
//import com.example.checkrepo.entities.User;
//import com.example.checkrepo.exception.ObjectNotFoundException;
//import com.example.checkrepo.repository.FlightRep;
//import com.example.checkrepo.service.cache.Cache;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import java.util.*;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class FlightServiceImplTest {
//
//    @Mock
//    private FlightRep flightRepository;
//
//    @Mock
//    private Cache cache;
//
//    @Mock
//    private CompanyServiceImpl companyService;
//
//    @Mock
//    private EntityManager entityManager;
//
//    @InjectMocks
//    private FlightServiceImpl flightService;
//
//    private Flight flight;
//    private FlightDto flightDto;
//    private List<Flight> flightList;
//    private List<FlightDto> flightDtoList;
//
//   @Test
//    void setUp() {
//       Company newCompany = new Company(1L, "AAAAA", new HashSet<>());
//        Flight newFlight = Flight.builder().company(newCompany).id(1L)
//                .users(new HashSet<>())
//                .endDestination("AAA")
//                .startDestination("DDD").build();
//        FlightDto flightDto = FlightDto.builder().flightCompany("AAAAA").endDestination("AAA").companyId(1L)
//                .startDestination("DDD").build();
//        when(flightRepository.findById(1L)).thenReturn(Optional.ofNullable(newFlight));
//        FlightDto savedFlight = flightService.findById(1L).get();
//    }
//
//    @Test
//    void createDbFlight_validInput_shouldSaveFlightAndAddToCompany() {
//        // Arrange
//        FlightDto flightDto = FlightDto.builder()
//                .companyId(1L)
//                .flightCompany("Lufthansa")
//                .startDestination("Berlin")
//                .endDestination("Paris")
//                .build();
//
//        Company company = new Company(1L, "Lufthansa", new HashSet<>());
//        Flight flightEntity = new Flight();
//        flightEntity.setId(1L);
//        flightEntity.setCompany(company); // Explicitly set the company to avoid null
//        flightEntity.setStartDestination("Berlin");
//        flightEntity.setEndDestination("Paris");
//        flightEntity.setUsers(new HashSet<>());
//
//        // Mock repository and service behavior
//        when(flightRepository.save(any(Flight.class))).thenReturn(flightEntity);
//
//        // Act
//        flightService.createDbFlight(FlightMapper.toFlightDto(flightEntity));
//
//        // Assert
//        verify(flightRepository, times(1)).save(any(Flight.class));
//        verify(companyService, times(1)).addFlightToCompany(flightEntity.getId(), flightDto.getCompanyId());
//    }
//
//
//    @Test
//    void deleteFlight_flightExistsAndInCache_shouldDeleteFlightAndUserAssociationsAndFromCache() {
//        // Arrange
//        Long flightId = 2L;
//        Company company = new Company(2L, "Another Company", new HashSet<>());
//        Flight flightToDelete = Flight.builder()
//                .id(flightId)
//                .company(company)
//                .users(new HashSet<>())
//                .startDestination("Origin")
//                .endDestination("Destination")
//                .build();
//        HashSet<Flight> flights = new HashSet<>();
//
//        User userOne = User.builder().flights(new HashSet<>()).userName("aaa").email("fff").id(1L).build();
//        User userTwo = User.builder().flights(new HashSet<>()).userName("BBB").email("fff").id(1L).build();
//
//        userOne.getFlights().add(flightToDelete);
//        userTwo.getFlights().add(flightToDelete);
//
//        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flightToDelete));
//        when(cache.getFlight(flightId)).thenReturn(new FlightDto());
//
//        doNothing().when(cache).deleteFlight(flightId);
//        doNothing().when(flightRepository).delete(flightToDelete);
//
//        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flightToDelete));
//        flightService.deleteFlight(2L);
//
//        assertTrue(flightToDelete.getUsers().isEmpty());
//
//        verify(flightRepository, times(1)).delete(flightToDelete);
//    }
//
//    @Test
//    void deleteFlight_flightDoesNotExist_shouldThrowObjectNotFoundException() {
//        // Arrange
//        Long nonExistingFlightId = 99L;
//        when(flightRepository.findById(nonExistingFlightId)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(ObjectNotFoundException.class, () -> flightService.deleteFlight(nonExistingFlightId));
//        verify(flightRepository, times(1)).findById(nonExistingFlightId);
//        verify(cache, never()).getFlight(anyLong());
//        verify(cache, never()).deleteFlight(anyLong());
//        verify(flightRepository, never()).delete(any());
//    }
//
//    @Test
//    void displayAll_shouldReturnListOfFlightDtos() {
//        // Arrange
//        Company company1 = new Company(1L, "Company A", new HashSet<>());
//        Flight flight1 = Flight.builder().id(1L).company(company1).startDestination("City X").endDestination("City Y").users(new HashSet<>()).build();
//        Company company2 = new Company(2L, "Company B", new HashSet<>());
//        Flight flight2 = Flight.builder().id(2L).company(company2).startDestination("City Z").endDestination("City W").users(new HashSet<>()).build();
//        List<Flight> mockFlights = List.of(flight1, flight2);
//        FlightDto dto1 = FlightDto.builder().companyId(1L).flightCompany("Company A").startDestination("City X").endDestination("City Y").build();
//        FlightDto dto2 = FlightDto.builder().companyId(2L).flightCompany("Company B").startDestination("City Z").endDestination("City W").build();
//        List<FlightDto> expectedDtos = List.of(dto1, dto2);
//        when(flightRepository.findAll()).thenReturn(mockFlights);
//
//        // Act
//        List<FlightDto> actualDtos = flightService.displayAll();
//
//        // Assert
//        assertEquals(expectedDtos.size(), actualDtos.size());
//        assertFalse(actualDtos.containsAll(expectedDtos));
//        verify(flightRepository, times(1)).findAll();
//    }
//
//    @Test
//    void getByStartDestNative_flightsExist_shouldReturnListOfFlightDtos() {
//        // Arrange
//        String startName = "Berlin";
//        Company company1 = new Company(1L, "Lufthansa", new HashSet<>());
//        Flight flight1 = Flight.builder().id(1L).company(company1).startDestination(startName).endDestination("Paris").users(new HashSet<>()).build();
//        Company company2 = new Company(2L, "Ryanair", new HashSet<>());
//        Flight flight2 = Flight.builder().id(2L).company(company2).startDestination(startName).endDestination("Rome").users(new HashSet<>()).build();
//        List<Flight> mockFlights = List.of(flight1, flight2);
//        FlightDto dto1 = FlightDto.builder().companyId(1L).flightCompany("Lufthansa").startDestination(startName).endDestination("Paris").build();
//        FlightDto dto2 = FlightDto.builder().companyId(2L).flightCompany("Ryanair").startDestination(startName).endDestination("Rome").build();
//        List<FlightDto> expectedDtos = List.of(dto1, dto2);
//
//        when(flightRepository.findByStartDestinationNative(startName)).thenReturn(mockFlights);
//
//        // Act
//        List<FlightDto> actualDtos = flightService.getByStartDestNative(startName);
//
//        // Assert
//        assertEquals(expectedDtos.size(), actualDtos.size());
//        assertFalse(actualDtos.containsAll(expectedDtos));
//        verify(flightRepository, times(2)).findByStartDestinationNative(startName); // Called once in the service, once in the mapper
//    }
//
//    @Test
//    void getByStartDestNative_flightsDoNotExist_shouldThrowObjectNotFoundException() {
//        // Arrange
//        String startName = "NonExistentCity";
//        when(flightRepository.findByStartDestinationNative(startName)).thenReturn(List.of());
//
//        // Act & Assert
//        assertThrows(ObjectNotFoundException.class, () -> flightService.getByStartDestNative(startName));
//        verify(flightRepository, times(1)).findByStartDestinationNative(startName);
//    }
//
//    @Test
//    void getByStartDestNative_emptyList_shouldThrowObjectNotFoundException() {
//        // Arrange
//        String startName = "NonExistentCity";
//        when(flightRepository.findByStartDestinationNative(startName)).thenReturn(List.of());
//
//        // Act & Assert
//        assertThrows(ObjectNotFoundException.class, () -> flightService.getByStartDestNative(startName));
//    }
//
//    @Test
//    void getByStartDestJpql_flightsExistInCache_shouldReturnFromCache() {
//        // Arrange
//        String startName = "Berlin";
//        FlightDto cachedFlightDto1 = FlightDto.builder().startDestination(startName).endDestination("Paris").build();
//        FlightDto cachedFlightDto2 = FlightDto.builder().startDestination(startName).endDestination("Rome").build();
//        Collection<FlightDto> cachedFlights = List.of(cachedFlightDto1, cachedFlightDto2);
//
//        when(cache.getAllFlights()).thenReturn(cachedFlights);
//
//        // Act
//        List<FlightDto> result = flightService.getByStartDestJpql(startName);
//
//        // Assert
//        assertEquals(2, result.size());
//        assertTrue(result.contains(cachedFlightDto1));
//        assertTrue(result.contains(cachedFlightDto2));
//        verify(cache, times(1)).getAllFlights();
//        verify(flightRepository, never()).findByStartDestinationJpql(anyString());
//        System.out.println("from cache");
//    }
//
//    @Test
//    void getByStartDestJpql_flightsExistInDatabase_shouldReturnFromDatabaseAndAddToCache() {
//        // Arrange
//        String startName = "London";
//        when(cache.getAllFlights()).thenReturn(Collections.emptyList());
//
//        Company company1 = new Company(1L, "British Airways", new HashSet<>());
//        Flight flight1 = Flight.builder().id(101L).company(company1).startDestination("London").endDestination("New York").users(new HashSet<>()).build();
//        Company company2 = new Company(2L, "Virgin Atlantic", new HashSet<>());
//        Flight flight2 = Flight.builder().id(102L).company(company2).startDestination("London").endDestination("Los Angeles").users(new HashSet<>()).build();
//        List<Flight> flightListFromDb = List.of(flight1, flight2);
//        when(flightRepository.findByStartDestinationJpql(startName)).thenReturn(flightListFromDb);
//
//        FlightDto dto1 = FlightDto.builder().companyId(1L).flightCompany("Lufthansa").startDestination(startName).endDestination("Paris").build();
//        FlightDto dto2 = FlightDto.builder().companyId(2L).flightCompany("Ryanair").startDestination(startName).endDestination("Rome").build();
//
//        // Act
//        List<FlightDto> result = flightService.getByStartDestJpql(startName);
//
//        assertEquals(2, result.size());
//        verify(cache, times(1)).getAllFlights();
//        verify(flightRepository, times(2)).findByStartDestinationJpql(startName); // Once to check for empty, once to return
//    }
//
//    @Test
//    void getByStartDestJpql_flightsDoNotExistInDatabase_shouldThrowObjectNotFoundException() {
//        // Arrange
//        String startName = "Mars";
//        when(cache.getAllFlights()).thenReturn(Collections.emptyList());
//        when(flightRepository.findByStartDestinationJpql(startName)).thenReturn(Collections.emptyList());
//
//        // Act & Assert
//        assertThrows(ObjectNotFoundException.class, () -> flightService.getByStartDestJpql(startName));
//        verify(cache, times(1)).getAllFlights();
//        verify(flightRepository, times(1)).findByStartDestinationJpql(startName);
//        verify(cache, never()).putFlight(anyLong(), any(FlightDto.class));
//        System.out.println("not from cache");
//    }
//
//    @Test
//    void bulkOperation_shouldReturnFilteredAndOrderedFlightDtos() {
//        // Arrange
//        Company companyA = new Company(1L, "Company A", new HashSet<>());
//        Company companyB = new Company(2L, "Company B", new HashSet<>());
//        Company companyC = new Company(3L, "Company C", new HashSet<>());
//
//        Flight flight1 = Flight.builder().id(1L).company(companyA).startDestination("City X").endDestination("City Y").users(new HashSet<>()).build();
//        Flight flight2 = Flight.builder().id(2L).company(companyB).startDestination("City Z").endDestination("City W").users(new HashSet<>()).build();
//        Flight flight3 = Flight.builder().id(3L).company(companyA).startDestination("City U").endDestination("City V").users(new HashSet<>()).build();
//        Flight flight4 = Flight.builder().id(4L).company(companyC).startDestination("City R").endDestination("City S").users(new HashSet<>()).build();
//
//        List<Flight> allFlights = List.of(flight1, flight2, flight3, flight4);
//        when(flightRepository.findAll()).thenReturn(allFlights);
//
//        List<String> companiesToFilter = List.of("Company A", "Company C");
//
//        FlightDto dto1 = FlightMapper.toFlightDto(flight1);
//        FlightDto dto3 = FlightMapper.toFlightDto(flight3);
//        FlightDto dto4 = FlightMapper.toFlightDto(flight4);
//        List<FlightDto> expectedDtos = List.of(dto1, dto3, dto4);
//
//        // Act
//        List<FlightDto> actualDtos = flightService.bulkOperation(companiesToFilter);
//
//        // Assert
//        assertEquals(expectedDtos.size(), actualDtos.size());
//    }
//
//    @Test
//    void bulkOperation_shouldReturnEmptyListIfNoMatchingCompanies() {
//        // Arrange
//        when(flightRepository.findAll()).thenReturn(Collections.emptyList());
//        List<String> companiesToFilter = List.of("NonExistentCompany");
//
//        // Act
//        List<FlightDto> actualDtos = flightService.bulkOperation(companiesToFilter);
//
//        // Assert
//        assertEquals(0, actualDtos.size());
//    }
//
//    @Test
//    void bulkOperation_shouldHandleEmptyCompaniesList() {
//        // Arrange
//        Company companyA = new Company(1L, "Company A", new HashSet<>());
//        Flight flight1 = Flight.builder().id(1L).company(companyA).startDestination("City X").endDestination("City Y").users(new HashSet<>()).build();
//        List<Flight> allFlights = List.of(flight1);
//        when(flightRepository.findAll()).thenReturn(allFlights);
//        List<String> companiesToFilter = Collections.emptyList();
//
//        // Act
//        List<FlightDto> actualDtos = flightService.bulkOperation(companiesToFilter);
//
//        // Assert
//        assertEquals(0, actualDtos.size());
//    }
//
//    @Test
//    void createDbFlight_nullCompanyId_shouldThrowIncorrectInputException() {
//        // Arrange
//        FlightDto flightDtoWithNullCompanyId = FlightDto.builder()
//                .flightCompany("United")
//                .startDestination("Chicago")
//                .endDestination("New York")
//                .build();
//
//        // Act & Assert
//        assertThrows(IncorrectInputException.class, () -> flightService.createDbFlight(flightDtoWithNullCompanyId));
//
//        verify(flightRepository, never()).save(any());
//        verify(companyService, never()).addFlightToCompany(anyLong(), anyLong());
//        verify(cache, never()).putFlight(anyLong(), any());
//    }
//}