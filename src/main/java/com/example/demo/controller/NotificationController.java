package com.example.demo.controller;

import com.example.demo.entity.Notification;
import com.example.demo.exception.AccessDeniedException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.service.BookingService;
import com.example.demo.service.DocumentService;
import com.example.demo.service.NotificationService;
import com.example.demo.service.UserService;
import com.example.security.ParserToken;
import com.example.security.TokenAuthenticationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@RestController
public class NotificationController {
    private NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/notification/findself")
    public Iterable<Notification> findMyNotifications(HttpServletRequest request){
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (token.role.equals("admin")) throw new AccessDeniedException();
        return notificationService.findAll()
                .stream()
                .filter(booking -> booking.getUser().equals(token.id))
                .collect(Collectors.toList());
    }
}