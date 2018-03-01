package com.example.demo.model.document;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
public class Author {
    @Id
    @Column(name = "ID")
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Integer id;
    @Size(max = 25)
    @Column(name = "FIRST_NAME")
    private String FirstName;
    @Size(max = 25)
    @Column(name = "LAST_NAME")
    private String LastName;

    public Author(Integer id, String firstName, String lastName) {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }
}
