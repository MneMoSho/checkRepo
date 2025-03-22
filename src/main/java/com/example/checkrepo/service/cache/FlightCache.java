package com.example.checkrepo.service.cache;

import com.example.checkrepo.dto.CompanyDto;
import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Collection;

@Component
public class FlightCache {
    private static  final int MAX_FLIGHT_CACHE_SIZE = 100;
    private static  final int MAX_USER_CACHE_SIZE = 100;
    private static  final int MAX_COMPANY_CACHE_SIZE = 100;

    private final LRUCache<Long, FlightDto> flightCache = new LRUCache<>(MAX_FLIGHT_CACHE_SIZE);
    private final LRUCache<Long, UserDto> userCache = new LRUCache<>(MAX_USER_CACHE_SIZE);
    private final LRUCache<Long, CompanyDto> companyCache = new LRUCache<>(MAX_COMPANY_CACHE_SIZE);
    private static final Logger logger = LoggerFactory.getLogger(FlightCache.class);

    public FlightDto getFlight(Long id) {
        if(flightCache.get(id) == null) {
            return null;
        } else {
            logger.info("flight access, with id: {}", id);
            return flightCache.get(id);
        }
    }

    public UserDto getUser(Long id) {
        logger.info("user access, with id: {}", id);
        return userCache.get(id);
    }

    public CompanyDto getCompany(Long id) {
        logger.info("company access, with id: {}", id);
        return companyCache.get(id);
    }

    public void deleteFlight(Long id) {
        logger.info("flight removed from cache with id: {}", id);
        flightCache.remove(id);
    }

    public void putFlight(Long id, FlightDto value) {
        logger.info("flight is added into cache, with id: {}", id);
        flightCache.putIfAbsent(id, value);
    }

    public void updateFlight(Long id, FlightDto oldValue, FlightDto newValue) {
        logger.debug("flight is changed in cache, with id: {}", id);
        flightCache.replace(id, oldValue, newValue);
    }

    public Collection<FlightDto> getAllFlights() {
        logger.info("flights were loaded from cache");
        return flightCache.getAll();
    }

    public void deleteUser(Long id) {
        logger.debug("user removed from cache with id: {}", id);
        userCache.remove(id);
    }

    public void putUser(Long id, UserDto value) {
        logger.debug("user is added into cache, with id: {}", id);
        userCache.putIfAbsent(id, value);
    }

    public void updateUser(Long id, UserDto oldValue, UserDto newValue) {
        logger.debug("User is changed in cache, with id: {}", id);
        userCache.replace(id, oldValue, newValue);
    }

    public Collection<UserDto> getAllUsers() {
        logger.debug("all users from cache");
        return userCache.getAll();
    }

    public void deleteCompany(Long id) {
        logger.debug("company removed from cache with id: {}", id);
        userCache.remove(id);
    }

    public void putCompany(Long id, CompanyDto value) {
        logger.debug("company is added into cache, with id: {}", id);
        companyCache.putIfAbsent(id, value);
    }

    public void deleteCompany(Long id, CompanyDto oldValue, CompanyDto newValue) {
        logger.debug("company is changed in cache, with id: {}", id);
        companyCache.replace(id, oldValue, newValue);
    }

    public Collection<CompanyDto> getAllCompanies() {
        logger.debug("all company from cache");
        return companyCache.getAll();
    }
}
