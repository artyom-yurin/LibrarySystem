package com.example.demo.model.user;

import javax.persistence.Entity;

@Entity
public  class Librarian extends User{

    public Librarian(long id, String name, String adress, String phoneNumber) {
        super(id, name, adress, phoneNumber);
    }

    /*public Patron createPatron(String name, String adress, String phoneNumber, long id, boolean isFaculty){
        Patron newPatron = new Patron(name, adress, phoneNumber, id, isFaculty);
        return newPatron;
    }

    public Librarian createLibrarian(String name, String adress, String phoneNumber, long id){
        Librarian newLibrarian = new Librarian(name, adress, phoneNumber, id);
        return newLibrarian;
    }

    public StringBuilder displayCard(){
        StringBuilder result = new StringBuilder();
        result.append(this.name);
        result.append(this.id);
        result.append(this.adress);
        result.append(this.phoneNumber);
        result.append("Librarian");
        return result;
    }*/
}
