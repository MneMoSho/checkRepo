package com.example.checkrepo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlightDto {
    private Long id;
    private int length;
    private String endDestination;
}
