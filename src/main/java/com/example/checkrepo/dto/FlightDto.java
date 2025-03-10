package com.example.checkrepo.dto;

import com.example.checkrepo.entities.FlightCompany;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FlightDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("length")
    private int length;
    @JsonProperty("endDestination")
    private String endDestination;
    @JsonProperty("startDestination")
    private String startDestination;
    @JsonProperty("company")
    private String flightCompany;
    @JsonProperty("users")
    private Set<UserDto> userDtos;
}
