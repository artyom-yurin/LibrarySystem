package com.example.demo.model.document;

import javax.persistence.Entity;

@Entity
public class AVMaterial {
    private Integer id;
    private String title;
    private String[] authors;
    private String[] tags;
    private int price;
    private int count;
}
