package com.example.checkrepo.entities;


import com.example.checkrepo.dto.FlightDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "usertable")
public class User {
    @Id
    private int id;
    private String userName;
    private String email;

    @ManyToMany
    @JoinTable(
            name = "user_flights",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "flight_id")}
    )
    private List<Flight> flights = new ArrayList<>();
}
