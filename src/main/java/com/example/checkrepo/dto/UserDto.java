package com.example.checkrepo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    @JsonProperty("id")
    private int id;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("email")
    private String email;
}
