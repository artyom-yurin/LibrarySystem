package com.example.demo.model.document;

import javax.persistence.Entity;

@Entity
public class Book extends Document {
    protected String publisher;
    protected int edition;
    protected boolean isBestseller;
    protected boolean isReference;
    protected int publishingYear;

    public Book(long id, String title, String[] authors, int price, int count, String publisher, int publishingYear, int editionYear, boolean isBestseller, boolean isReference) {
        super(id, title, authors, price, count);
        this.publisher = publisher;
        this.edition = editionYear;
        this.isReference = isReference;
        this.isBestseller = isBestseller;
        this.publishingYear = publishingYear;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getEdition() {
        return edition;
    }

    public void setEdition(int edition) {
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

    public int getPublishingYear() {
        return publishingYear;
    }

    public void setPublishingYear(int publishingYear) {
        this.publishingYear = publishingYear;
    }
}