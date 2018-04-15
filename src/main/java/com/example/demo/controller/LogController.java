package com.example.demo.controller;

import com.example.demo.entity.information.Log;
import com.example.demo.exception.AccessDeniedException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.service.LogService;
import com.example.security.ParserToken;
import com.example.security.TokenAuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@RestController
public class LogController {
    private LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/logId")
    public Iterable<Log> getLogs(@RequestParam(name = "id", defaultValue = "-1") Integer userId, HttpServletRequest request)
    {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();
        return logService.findAll()
                .stream()
                .filter(log -> log.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @GetMapping("/log")
    public Iterable<Log> getLogs(HttpServletRequest request)
    {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();
        return logService.findAll();
    }
}
