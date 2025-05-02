package com.example.checkrepo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
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
    @JsonProperty("country")
    private String country;
    @JsonProperty("price")
    private int price;
}
