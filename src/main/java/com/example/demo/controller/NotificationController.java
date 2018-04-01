package com.example.demo.controller;

import com.example.demo.entity.Notification;
import com.example.demo.entity.booking.Booking;
import com.example.demo.exception.AccessDeniedException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.service.BookingService;
import com.example.demo.service.DocumentService;
import com.example.demo.service.NotificationService;
import com.example.demo.service.UserService;
import com.example.security.ParserToken;
import com.example.security.TokenAuthenticationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
public class NotificationController {
    private NotificationService notificationService;
    private BookingService bookingService;
    private UserService userService;
    private DocumentService documentService;

    public NotificationController(NotificationService notificationService, BookingService bookingService, UserService userService, DocumentService documentService) {
        this.notificationService = notificationService;
        this.bookingService = bookingService;
        this.userService = userService;
        this.documentService = documentService;
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

    @Scheduled(fixedRate = 6000)
    private void refresh(){
        ArrayList<Booking> toNotify = new ArrayList<>();
        bookingService.findAll();
    }
}
