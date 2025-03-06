package com.example.checkrepo.dto;

import com.example.checkrepo.entities.Flight;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlightCompanyDto {
    int id;
    String companyName;

    @JsonProperty("flights")
    private Set<FlightDto> flights;
}
