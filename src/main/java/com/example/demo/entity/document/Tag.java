package com.example.demo.entity.document;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
public class Tag {
    @Id
    @Column(name = "TAG_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Size(max = 25)
    @Column(name = "TAG_NAME")
    private String tagName;

    public Tag(Integer id, String tagName) {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
