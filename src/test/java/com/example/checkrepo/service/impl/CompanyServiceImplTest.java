//package com.example.checkrepo.service.impl;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import com.example.checkrepo.dto.CompanyDto;
//import com.example.checkrepo.entities.Flight;
//import com.example.checkrepo.repository.CompanyRepository;
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
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class CompanyServiceImplTest {
//
//    @Mock
//    private CompanyRepository companyRepository;
//
//    @Mock
//    private FlightRep flightRepository;
//
//    @Mock
//    private Cache cache;
//
//    @InjectMocks
//    private CompanyServiceImpl companyService;
//
//    @Test
//    void addFlightCompany_ShouldSaveCompanyAndUpdateCache() {
//        CompanyDto dto = new CompanyDto(1L, "Test Company", new HashSet<>());
//
//        companyService.addFlightCompany(dto);
//
//        verify(companyRepository).save(any(Company.class));
//        verify(cache).putCompany(eq(1L), any(CompanyDto.class));
//    }
//
//    @Test
//    void addFlightToCompany_WhenCompanyNotFound_ShouldThrowException() {
//        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        assertThrows(ObjectNotFoundException.class,
//                () -> companyService.addFlightToCompany(1L, 1L));
//    }
//
//    @Test
//    void addFlightToCompany_WhenFlightNotFound_ShouldThrowException() {
//        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(new Company()));
//        when(flightRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        assertThrows(ObjectNotFoundException.class,
//                () -> companyService.addFlightToCompany(1L, 1L));
//    }
//
//    @Test
//    void addFlightToCompany_WhenValid_ShouldUpdateCompanyAndCache() {
//        Company newCompany = new Company(1L, "AAAAA", new HashSet<>());
//        Flight newFlight = new Flight(1L, 222, "Tokyo", "Berlin", newCompany, new HashSet<>());
//
//        when(companyRepository.findById(1L)).thenReturn(Optional.of(newCompany));
//        when(flightRepository.findById(1L)).thenReturn(Optional.of(newFlight));
//
//        Optional<CompanyDto> result = companyService.addFlightToCompany(1L, 1L);
//
//        assertTrue(result.isPresent());
//        verify(flightRepository).save(any(Flight.class));
//    }
//
//    @Test
//    void showAll_ShouldReturnAllCompaniesFromRepository() {
//        Company company1 = new Company(1L, "Company One", new HashSet<>());
//        Company company2 = new Company(2L, "Company Two", new HashSet<>());
//        List<Company> companies = List.of(company1, company2);
//        when(companyRepository.findAll()).thenReturn(companies);
//        List<CompanyDto> result = companyService.showAll();
//        assertEquals(2, result.size());
//        assertEquals("Company One", result.get(0).getCompanyName());
//        assertEquals("Company Two", result.get(1).getCompanyName());
//        verify(companyRepository).findAll(); // Убедиться, что метод findAll вызван
//    }
//
//    @Test
//    void deleteCompany_ShouldDeleteAllRelatedEntities() {
//        Company company = new Company();
//        Flight flight = new Flight();
//        flight.setUsers(Set.of(new User()));
//        company.setFlights(Set.of(flight));
//
//        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(company));
//
//        companyService.deleteCompany(1L);
//
//        verify(flightRepository).delete(flight);
//        verify(companyRepository).delete(company);
//    }
//
//    @Test
//    void getCompanyFlightsNative_ShouldGroupFlightsByCompany() {
//
//        Company newCompany = new Company(1L, "AAAAA", new HashSet<>());
//        Flight addFlight = new Flight(1L, 222, "Tokyo", "Berlin", newCompany, new HashSet<>());
//        Flight addFlightAnother = new Flight(2L, 333, "Tokyo", "Moscow", newCompany, new HashSet<>());
//
//        when(companyRepository.findByCompanyIdNative("Tokyo")).thenReturn(List.of(addFlight, addFlightAnother));
//        when(companyRepository.findById(1L)).thenReturn(Optional.of(new Company(1L, "Test Company", new HashSet<>())));
//
//        List<CompanyDto> result = companyService.getCompanyFlightsNative("Tokyo");
//
//        assertEquals(1, result.size());
//        assertEquals("Test Company", result.get(0).getCompanyName());
//        assertEquals(2, result.get(0).getFlights().size());
//
//        verify(companyRepository).findByCompanyIdNative("Tokyo");
//        verify(companyRepository).findById(1L);
//    }
//
//    @Test
//    void getCompanyFlightsJpql_ShouldGroupFlightsByCompany() {
//        Company newCompany = new Company(1L, "AAAAA", new HashSet<>());
//        Flight addFlight = new Flight(1L, 222, "Tokyo", "Berlin", newCompany, new HashSet<>());
//        Flight addFlightAnother = new Flight(2L, 333, "Tokyo", "Moscow", newCompany, new HashSet<>());
//        when(companyRepository.findByCompanyIdJpql("Tokyo")).thenReturn(List.of(addFlight, addFlightAnother));
//        when(companyRepository.findById(1L)).thenReturn(Optional.of(newCompany));
//        List<CompanyDto> result = companyService.getCompanyFlightsJpql("Tokyo");
//        assertEquals(1, result.size()); // Only one company should be present
//        assertEquals("AAAAA", result.get(0).getCompanyName()); // Verify the company name
//        assertEquals(2, result.get(0).getFlights().size()); // Verify two flights are grouped under the same company
//    }
//
//    @Test
//    void deleteCompany_WhenNotExists_ShouldThrowException() {
//        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        assertThrows(ObjectNotFoundException.class,
//                () -> companyService.deleteCompany(1L));
//    }
//}
//
