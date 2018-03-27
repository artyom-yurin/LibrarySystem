package com.example.demo.entity.document;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Entity
public class Document {
    @Id
    @Column(name = "DOCUMENT_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Size(max = 100)
    @Column(name = "TITLE")
    private String title;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "AUTHOR_ID")
    private Set<Author> authors;

    @Column(name = "PRICE")
    private Integer price;
    @Column(name = "COUNT")
    private Integer count;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "TAG_ID")
    private Set<Tag> tags;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "PUBLISHER_ID")
    private Publisher publisher;

    @Column(name = "EDITION")
    private Integer edition;

    @Column(name = "BESTSELLER", columnDefinition = "TINYINT(1)")
    private boolean isBestseller;

    @Column(name = "REFERENCE", columnDefinition = "TINYINT(1)")
    private boolean isReference;

    @Column(name = "PUBLISHING_DATE", columnDefinition = "DATETIME")
    private Date publishingDate;

    @Size(max = 100)
    @Column(name = "EDITOR")
    private String editor;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "TYPE_ID")
    private TypeDocument type;

    public Document() {
    }

    public Document(String title, Set<Author> authors, Integer price, Integer count, Set<Tag> tags, Publisher publisher, Integer edition, boolean isBestseller, boolean isReference, Date publishingDate, String editor, TypeDocument type) {
        this.title = title;
        this.authors = authors;
        this.price = price;
        this.count = count;
        this.tags = tags;
        this.publisher = publisher;
        this.edition = edition;
        this.isBestseller = isBestseller;
        this.isReference = isReference;
        this.publishingDate = publishingDate;
        this.editor = editor;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Integer getEdition() {
        return edition;
    }

    public void setEdition(Integer edition) {
        this.edition = edition;
    }

    public boolean isBestseller() {
        return isBestseller;
    }

    public void setBestseller(boolean bestseller) {
        isBestseller = bestseller;
    }

    public boolean isReference() {
        return isReference;
    }

    public void setReference(boolean reference) {
        isReference = reference;
    }

    public Date getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(Date publishingDate) {
        this.publishingDate = publishingDate;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public TypeDocument getType() {
        return type;
    }

    public void setType(TypeDocument type) {
        this.type = type;
    }
}