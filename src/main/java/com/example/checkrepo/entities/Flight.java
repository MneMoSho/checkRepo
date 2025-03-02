package com.example.checkrepo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "flight")
public class Flight {

   // public Flight(Long id, int length, String startDestination, String endDestination) {
   //     this.id = id;
   //     this.length = length;
   //     this.startDestination = startDestination;
   //     this.endDestination = endDestination;
   // }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "length")
    private int length;
    @Column(name = "startdestination")
    private String startDestination;
    @Column(name = "enddestination")
    private String endDestination;

    @ManyToMany(mappedBy = "flights")
    private Set<User> users;
}
