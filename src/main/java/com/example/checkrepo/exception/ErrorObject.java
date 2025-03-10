package com.example.checkrepo.exception;

import java.util.Date;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ErrorObject {
    private Integer statusCode;
    private String message;
    private Date timestamp;
}
