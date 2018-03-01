package com.example.demo.model.document;

import javax.persistence.Entity;

@Entity
public class AVMaterials extends Document {

    public AVMaterials(long id, String title, String[] authors, int price, int count) {
        super(id, title, authors, price, count);
    }

}
