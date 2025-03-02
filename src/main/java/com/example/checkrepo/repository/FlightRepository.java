package com.example.checkrepo.repository;

import com.example.checkrepo.entities.Flight;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.stereotype.Repository;

@Getter
@Repository
public class FlightRepository {
    private List<Flight> flights = new ArrayList<>();

    public Flight getById(final int id) {
        return flights.get(id);
    }

    public List<Flight> getByName(final String name) {
        List<Flight> bufList = new ArrayList<>();
        for (Flight flight : flights) {
            if (flight.getEndDestination().equals(name)) {
                bufList.add(flight);
            }
        }
        return bufList;
    }

    public void addFlight(final Flight flight) {
       // flight.setId(flights.size() + 1);
       // flights.add(flight);
    }

    public List<Flight> deleteFlight(final int numberOfFlight) {
        flights.remove(numberOfFlight);
        return flights;
    }

    public List<Flight> getByRoute(String startPoint, String endPoint) {
        List<Flight> bufList = new ArrayList<>();
        for (Flight flight : flights) {
            if (flight.getStartDestination().equals(startPoint)
                    && flight.getEndDestination().equals(endPoint)) {
                bufList.add(flight);
            }
        }
        return bufList;
    }
}
