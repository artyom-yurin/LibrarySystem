package com.example.demo.model.document;

import javax.persistence.Entity;

@Entity
public abstract class Document {
    protected String title;
    protected String[] authors;
    protected long id;
    protected int price;
    protected int count;
    protected String[] tags;
    protected int returnTime; //in days

    public Document(long id, String title, String[] authors, int price, int count) {
        this.title = title;
        this.authors = authors;
        this.id = id;
        this.price = price;
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void updateCount() {this.count = this.count - 1;}

    public void setCount(int count) {
        this.count = count;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public int getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(int returnTime) {
        this.returnTime = returnTime;
    }
}
