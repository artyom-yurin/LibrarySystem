package com.example.demo.entity.information;

import com.example.demo.entity.user.User;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Notification {

    @Id
    @Column(name = "NOTIFICATION_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "NOTIFICATION_DATE", columnDefinition = "DATETIME")
    private Date notificationDate;

    @Column(name = "MESSAGE")
    private String message;

    public Notification(){}

    public Notification(User user, Date notificationDate, String message) {
        this.user = user;
        this.notificationDate = notificationDate;
        this.message = message;
    }

    public Date getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
