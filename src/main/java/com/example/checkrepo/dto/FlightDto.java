package com.example.checkrepo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FlightDto {
    private int id;
    private int length;
    private String endDestination;
    private String startDestination;

    public FlightDto(int id, int length,
                     String startDestination,
                     String endDestination) {
        this.id = id;
        this.length = length;
        this.startDestination = startDestination;
        this.endDestination = endDestination;
    }

    @JsonProperty("id")
    public int getId() {
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
}
