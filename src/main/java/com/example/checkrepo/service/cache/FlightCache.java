package com.example.checkrepo.service.cache;

import com.example.checkrepo.entities.Flight;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class FlightCache {
    private final ConcurrentHashMap<Long, Flight> cache = new ConcurrentHashMap<>();

    public Flight get(Long key) {
        return cache.get(key);
    }

    public void putIfAbsent(Long key, Flight flight) {
        cache.putIfAbsent(key,flight);
    }

    public void remove(Long key, Flight flight) {
        cache.remove(key);
    }

    public void replace(Long key, Flight oldFlight, Flight newFlight) {
        cache.replace(key, oldFlight, newFlight);
    }
}
