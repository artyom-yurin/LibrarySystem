package com.example.demo.repository;

import com.example.demo.entity.information.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    Notification getNotificationById(Integer id);

    void removeNotificationById(Integer id);
}
