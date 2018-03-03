package com.example.demo.entity.document;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
public class Publisher{
    @Id
    @Column(name = "PUBLISHER_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Size(max = 25)
    @Column(name = "PUBLISHER_NAME")
    private String publisherName;

    public Publisher(Integer id, String publisherName) {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }
}
