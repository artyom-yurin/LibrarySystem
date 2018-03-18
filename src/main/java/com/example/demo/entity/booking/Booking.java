package com.example.demo.entity.booking;


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

    @Column(name = "FINE")
    private Integer fine;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "TYPE_ID")
    private TypeBooking typeBooking;

    public Booking() {}

    public Booking(User user, Document document, Date returnDate, Integer fine, TypeBooking typeBooking) {
        this.user = user;
        this.document = document;
        this.returnDate = returnDate;
        this.fine = fine;
        this.typeBooking = typeBooking;
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

    public Integer getFine() {
        return fine;
    }

    public void setFine(Integer fine) {
        this.fine = fine;
    }

    public TypeBooking getTypeBooking() {
        return typeBooking;
    }

    public void setTypeBooking(TypeBooking typeBooking) {
        this.typeBooking = typeBooking;
    }
}
