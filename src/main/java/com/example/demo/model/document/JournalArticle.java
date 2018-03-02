package com.example.demo.model.document;

import javax.persistence.Entity;
import java.util.Date;
import java.util.Set;

//@Entity
public class JournalArticle{
    private Integer id;
    private String title;
    private Set<Author> authors;
    private String editor;
    private Date dateOfPublishing;
    private Set<Tag> tags;
    private int price;
    private int count;
}
