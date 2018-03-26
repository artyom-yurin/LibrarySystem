package com.example.demo.entity.booking;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
public class TypeBooking {
    @Id
    @Column(name = "TYPE_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Size(max = 25)
    @Column(name = "TYPE_NAME")
    private String typeName;

    public TypeBooking(){}

    public TypeBooking(String typeName) {
        this.typeName = typeName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
