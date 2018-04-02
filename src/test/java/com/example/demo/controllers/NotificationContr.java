package com.example.demo.controllers;

import com.example.demo.entity.Notification;
import com.example.demo.exception.AccessDeniedException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.service.NotificationService;
import com.example.security.ParserToken;
import com.example.security.TokenAuthenticationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@RestController
public class NotificationContr {
    private NotificationService notificationService;

    public NotificationContr(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public Iterable<Notification> findMyNotifications(Integer id){
        return notificationService.findAll()
                .stream()
                .filter(booking -> booking.getUser().getId().equals(id))
                .collect(Collectors.toList());
    }
}
