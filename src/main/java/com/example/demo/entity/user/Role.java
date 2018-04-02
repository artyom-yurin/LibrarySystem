package com.example.demo.entity.user;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
public class Role {
    @Id
    @Column(name = "ROLE_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Size(max = 25)
    @Column(name = "NAME")
    private String name;
    @Size(max = 25)
    @Column(name = "POSITION")
    private String position;


    public Role() {
    }

    public Role(String name, String position) {
        this.name = name;
        this.position = position;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
