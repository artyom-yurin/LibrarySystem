package com.example.demo.entity;


import com.example.demo.entity.document.Document;
import com.example.demo.entity.user.User;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Booking {
    @Id
    @Column(name = "BOOKING_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "DOCUMENT_ID")
    private Document document;

    @Column(name = "RETURN_DATE",columnDefinition = "DATETIME")
    private Date returnDate;

    @Column(name = "HAS_BACK_REQUEST")
    private boolean hasBackRequest;

    @Column(name = "FINE")
    private Integer fine;

    @Column(name = "IS_CLOSE")
    private boolean isClose;

    public Booking(Integer id, User user, Document document, Date returnDate, boolean hasBackRequest, Integer fine, boolean isClose) {}

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

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isHasBackRequest() {
        return hasBackRequest;
    }

    public void setHasBackRequest(boolean hasBackRequest) {
        this.hasBackRequest = hasBackRequest;
    }

    public Integer getFine() {
        return fine;
    }

    public void setFine(Integer fine) {
        this.fine = fine;
    }

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean close) {
        isClose = close;
    }
}
