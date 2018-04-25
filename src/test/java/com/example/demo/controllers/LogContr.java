package com.example.demo.controllers;

import com.example.demo.entity.information.Log;
import com.example.demo.exception.AccessDeniedException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.service.LogService;
import com.example.security.ParserToken;
import com.example.security.TokenAuthenticationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LogContr{
    private LogService logService;

    public LogContr(LogService logService) {
        this.logService = logService;
    }

    public List<Log> getLogs(Integer userId)
    {
        return logService.findAll()
                .stream()
                .filter(log -> log.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Log> getLogs()
    {
        return logService.findAll();
    }
}
