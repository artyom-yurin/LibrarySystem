package com.example.demo.service;

import com.example.demo.entity.Notification;
import com.example.demo.entity.user.User;
import com.example.demo.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository){
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> findAll(){
        return notificationRepository.findAll();
    }

    public Notification getNotificationByUser(User user){
        return notificationRepository.getNotificationByUser(user);
    }

    public void save(Notification notification){
        notificationRepository.save(notification);
    }

    public Notification getNotificationById(Integer id){
        return notificationRepository.getNotificationById(id);
    }

    public void removeNotificationById(Integer id){
        notificationRepository.removeNotificationById(id);
    }
}
