package com.example.checkrepo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String userName;
    private String email;
    private String password;
    private Set<FlightDto> flightDtos;

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("username")
    public String getUserName() {
        return userName;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("flights")
    public Set<FlightDto> getFlights() {
        return flightDtos;
    }

    @JsonProperty("password")
    public String getPassword() {return password;}
}
