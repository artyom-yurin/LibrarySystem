package com.example.demo.service;

import com.example.demo.entity.Notification;
import com.example.demo.entity.user.User;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private UserRepository userRepository;
    private NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    public void save(Notification notification) {
        notificationRepository.save(notification);
    }

    public Notification getNotificationById(Integer id) {
        return notificationRepository.getNotificationById(id);
    }

    public void removeNotificationById(Integer id) {
        notificationRepository.removeNotificationById(id);
    }

    public void newNotification(Integer userId, String message) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new UserNotFoundException();
        }
        Notification notification = new Notification(user, message);
        notificationRepository.save(notification);
    }
}
