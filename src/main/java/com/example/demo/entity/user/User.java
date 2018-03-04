package com.example.demo.entity.user;



import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
public class User{
    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Size(max = 25)
    @Column(name = "FIRST_NAME")
    private String name;
    @Size(max = 25)
    @Column(name = "LAST_NAME")
    private String surname;
    @Size(max = 25)
    @Column(name = "ADDRESS")
    private String address;
    @Size(max = 25)
    @Column(name = "PHONE")
    private String phoneNumber;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ROLE_ID")
    private Role role;
    @Size(max = 25)
    @Column(name = "LOGIN", unique = true)
    private String username;
    @Size(max = 25)
    @Column(name = "PASSWORD")
    private String password;

    public User() {}

    public User(String name, String surname, String address, String phoneNumber, Role role, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.username = username;
        this.password = password;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
