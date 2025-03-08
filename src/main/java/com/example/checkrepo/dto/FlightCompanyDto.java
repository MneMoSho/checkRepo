package com.example.checkrepo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("id")
    private Long companyId;
    @JsonProperty("companyName")
    private String companyName;
    @JsonProperty("flightsCompany")
    private Set<FlightDto> flights;
}
