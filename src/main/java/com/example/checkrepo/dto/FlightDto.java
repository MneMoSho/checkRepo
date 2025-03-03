package com.example.checkrepo.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id"
//)
public class FlightDto {
    private Long id;
    private int length;
    private String endDestination;
    private String startDestination;
    private Set<UserDto> userDtos;

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("length")
    public int getLength() {
        return length;
    }

    @JsonProperty("startDestination")
    public String getStartDestination() {
        return startDestination;
    }

    @JsonProperty("endDestination")
    public String getEndDestination() {
        return endDestination;
    }

    @JsonProperty("users")
    public Set<UserDto> getUsers() {
        return userDtos;
    }
}
