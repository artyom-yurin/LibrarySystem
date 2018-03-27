package com.example.demo.model;

public class BookingModel {
    private String user_name;
    private Integer book_id;

    public BookingModel() {
    }

    public BookingModel(String user_name, Integer book_id) {
        this.user_name = user_name;
        this.book_id = book_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Integer getBook_id() {
        return book_id;
    }

    public void setBook_id(Integer book_id) {
        this.book_id = book_id;
    }
}
