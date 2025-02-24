package com.example.checkrepo.entities;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "flights")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true)
    private int length;
    @Column(nullable = false)
    private String startDestination;
    @Column(nullable = false)
    private String endDestination;
}
