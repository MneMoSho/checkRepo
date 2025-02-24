package com.example.checkrepo.services.dataloader;

import com.example.checkrepo.entities.Flight;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightLoader {
    public List<Flight> createNewFlight(List<Flight> listFlight) {
        Flight flight1 = new Flight(1, 234, "Beijing", "Moscow");
        Flight flight2 = new Flight(2, 456, "Moscow", "Shanghai");
        Flight flight3 = new Flight(3, 123, "Tokyo", "Shanghai");

        listFlight.add(flight1);
        listFlight.add(flight2);
        listFlight.add(flight3);
        return listFlight;
    }
}
