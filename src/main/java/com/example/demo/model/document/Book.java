package com.example.demo.model.document;

import javax.persistence.Entity;

@Entity
public class Book{
    private Integer id;
    private String title;
    private String[] authors;
    private int price;
    private int count;
    private String[] tags;
    private String publisher;
    private int edition;
    private boolean isBestseller;
    private boolean isReference;
    private int publishingYear;
}