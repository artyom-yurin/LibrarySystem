package com.example.demo.repository;

import com.example.demo.entity.Notification;
import com.example.demo.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    Notification getNotificationByUser(User user);

    Notification getNotificationById(Integer id);

    void removeNotificationById(Integer id);
}
