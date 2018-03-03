package com.example.demo.model.document;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.Set;

@Entity
public class Document{
    @Id
    @Column(name = "DOCUMENT_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Size(max = 25)
    @Column(name = "TITLE")
    private String title;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "AUTHOR_ID")
    private Set<Author> authors;

    @Column(name = "PRICE")
    private Integer price;
    @Column(name = "COUNT")
    private Integer count;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "TAG_ID")
    private Set<Tag> tags;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "PUBLISHER_ID")
    private Publisher publisher;

    @Column(name = "EDITION")
    private Integer edition;

    @Column(name = "BESTSELLER")
    private boolean isBestseller;

    @Column(name = "REFERENCE")
    private boolean isReference;

    @Column(name = "PUBLISHING_DATE",columnDefinition = "DATETIME")
    private Date publishingDate;

    @Size(max = 25)
    @Column(name = "EDITOR")
    private String editor;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "TYPE_ID")
    private  TypeDocument type;
}