package com.example.demo.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Count of copies must be positive")
public class InvalidCountException extends RuntimeException{
}
