package com.example.demo.entity.information;

import com.example.demo.entity.user.User;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Log {
    @Id
    @Column(name = "NOTIFICATION_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "LIBRARIAN_ID")
    private User user;

    @Column(name = "LOG_DATE", columnDefinition = "DATETIME")
    private Date logDate;

    @Column(name = "MESSAGE")
    private String message;

    public Log(){}

    public Log(User user, Date logDate, String message) {
        this.user = user;
        this.logDate = logDate;
        this.message = message;
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

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}