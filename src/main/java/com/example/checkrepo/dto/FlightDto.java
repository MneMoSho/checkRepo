package com.example.checkrepo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightDto {
    @Min(value = 1, message = "id must not be less than 1")
    @JsonProperty("id")
    private Long id;
    @JsonProperty("length")
    private int length;
    @JsonProperty("endDestination")
    private String endDestination;
    @JsonProperty("startDestination")
    private String startDestination;
    @JsonProperty("companyId")
    private Long companyId;
    @JsonProperty("company")
    private String flightCompany;
    @JsonProperty("users")
    private Set<UserDto> userDtos;
}
