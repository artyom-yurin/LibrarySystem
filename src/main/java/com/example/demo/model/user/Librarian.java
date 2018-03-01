package com.example.demo.model.user;

import javax.persistence.Entity;

@Entity
public  class Librarian{
    private Integer id;
    private String name;
    private String address;
    private String phoneNumber;

    public Librarian(Integer id, String name, String adress, String phoneNumber) {}

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
}
