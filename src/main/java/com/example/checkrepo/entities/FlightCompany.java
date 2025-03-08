package com.example.checkrepo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "companies")
public class FlightCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "company_name")
    private String companyName;

    @OneToMany(mappedBy = "flightCompany", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
   // @JoinTable(name = "flight_company",
   //         joinColumns = {@JoinColumn(name = "company_id")},
   //         inverseJoinColumns = {@JoinColumn(name = "flight_id")})
    private Set<Flight> flights;
}
