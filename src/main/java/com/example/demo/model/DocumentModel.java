package com.example.demo.model;

import com.example.demo.entity.document.Author;
import com.example.demo.entity.document.Publisher;
import com.example.demo.entity.document.Tag;
import com.example.demo.entity.document.TypeDocument;

import java.util.Date;
import java.util.Set;

public class DocumentModel {

    private Integer id;
    private String title;
    private Set<Author> authors;
    private Integer price;
    private Integer count;
    private Set<Tag> tags;
    private String publisher;
    private Integer edition;
    private boolean isBestseller;
    private boolean isReference;
    private Date publishingDate;
    private String editor;
    private TypeDocument type;

    public DocumentModel() {
    }

    public DocumentModel(Integer id, String title, Set<Author> authors, Integer price, Integer count, Set<Tag> tags, String publisher, Integer edition, boolean isBestseller, boolean isReference, Date publishingDate, String editor, TypeDocument type) {
        this.id = id;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
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

    public java.util.Date getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(java.sql.Date publishingDate) {
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
