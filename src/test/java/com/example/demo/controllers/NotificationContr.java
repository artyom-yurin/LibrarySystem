package com.example.demo.controllers;

import com.example.demo.entity.information.Notification;
import com.example.demo.service.NotificationService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class NotificationContr {
    private NotificationService notificationService;

    public NotificationContr(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public List<Notification> findMyNotifications(Integer id){
        return notificationService.findAll()
                .stream()
                .filter(booking -> booking.getUser().getId().equals(id))
                .collect(Collectors.toList());
    }
}
