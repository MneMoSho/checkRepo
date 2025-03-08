package com.example.checkrepo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

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

    @JsonProperty("users")
    private Set<UserDto> userDtos;
}
