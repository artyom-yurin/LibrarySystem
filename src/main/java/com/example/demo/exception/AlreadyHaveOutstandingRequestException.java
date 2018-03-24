package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.FORBIDDEN, reason="Already have outstanding request")
public class AlreadyHaveOutstandingRequestException extends RuntimeException{
}
