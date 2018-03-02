package com.example.demo.model.document;

import javax.persistence.Entity;
import java.util.ArrayList;

//@Entity
public class Book{
    private Integer id;
    private String title;
    private ArrayList<Author> authors;
    private int price;
    private int count;
    private ArrayList<Tag> tags;
    private String publisher;
    private int edition; // TODO: add edition
    private boolean isBestseller;
    private boolean isReference;
    private int publishingYear;
}