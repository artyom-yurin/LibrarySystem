package com.example.demo.entity;

import javax.persistence.*;

@Entity
public class Notification {

    @Id
    @Column(name = "NOTIFICATION_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "USER_ID")
    private Integer user;

    @Column(name = "MESSAGE")
    private String message;

    public Notification(){}

    public Notification(Integer user, String message){
        this.user = user;
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
