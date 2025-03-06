package com.example.checkrepo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "flight")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "length")
    private int length;
    @Column(name = "startdestination")
    private String startDestination;
    @Column(name = "enddestination")
    private String endDestination;

    @ManyToOne
    @JoinColumn(name = "flightComp_id", nullable = false)
    private FlightCompany flightCompany;

    @ManyToMany(mappedBy = "flights", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();
}
