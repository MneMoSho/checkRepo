package com.example.checkrepo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "flights")
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companies_id")
    private Company company;
    @ManyToMany(mappedBy = "flights", fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();
    @Column(name = "country")
    private String country;
    @Column(name = "price")
    private int price;
    @Column(name = "timeleaving")
    private String timeLeaving;
    @Column(name = "timearriving")
    private String timeArriving;

}
