package com.example.demo.model.document;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Date;

//@Entity
public class JournalArticle{
    private Integer id;
    private String title;
    private ArrayList<Author> authors;
    private String editor;
    private Date dateOfPublishing;
    private ArrayList<Tag> tags;
    private int price;
    private int count;
}
