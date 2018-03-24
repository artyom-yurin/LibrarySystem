package com.example.demo.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.FORBIDDEN, reason="Forbidden renew, because someone wants this book")
public class UnableRenewException extends RuntimeException {
}
