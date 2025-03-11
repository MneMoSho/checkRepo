package com.example.checkrepo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {
    @JsonProperty("id")
    private Long companyId;
    @JsonProperty("name")
    private String companyName;
    @JsonProperty("flights")
    private Set<FlightDto>
            flights;
}
