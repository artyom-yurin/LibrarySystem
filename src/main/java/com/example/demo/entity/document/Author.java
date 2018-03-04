package com.example.demo.entity.document;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
public class Author {
    @Id
    @Column(name = "AUTHOR_ID")
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Integer id;
    @Size(max = 25)
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Size(max = 25)
    @Column(name = "LAST_NAME")
    private String lastName;

    public Author() {}

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
