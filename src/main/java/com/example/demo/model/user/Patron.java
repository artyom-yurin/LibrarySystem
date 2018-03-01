package com.example.demo.model.user;

import javax.persistence.Entity;

@Entity
public class Patron{
    private Integer id;
    private String name;
    private String address;
    private String phoneNumber;
    private boolean isFaculty;

    public Patron(Integer id, String name, String address, String phoneNumber, boolean isFaculty) {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isFaculty() {
        return isFaculty;
    }

    public void setFaculty(boolean faculty) {
        isFaculty = faculty;
    }
}
