package com.example.demo.model.user;

import javax.persistence.Entity;

@Entity
public class Patron extends User {
    public boolean isFaculty;

    public Patron(long id, String name, String adress, String phoneNumber, boolean isFaculty) {
        super(id, name, adress, phoneNumber);
        this.isFaculty = isFaculty;
    }

    /*public void addDocument(Book document){
        if(document.isReference){
            return;
        }
        if(document.getCount() == 0){
            return;
        }
        if(documents.contains(document)){
            return;
        }
        Constants.checkOut(document);
        documents.add(document);
        document.setReturnTime(21);
        if(document.isBestseller){
            document.setReturnTime(14);
        }
        if(this.isFaculty){
            document.setReturnTime(28);
        }
        this.lastAdded = document;
    }

    public void returnDocument(Document document){
        //request to Library
        documents.remove(document);
    }

    public StringBuilder displayCard(){
        StringBuilder result = new StringBuilder();
        result.append(this.name);
        result.append(this.id);
        result.append(this.adress);
        result.append(this.phoneNumber);
        if(isFaculty){
            result.append("Faculty");
        }
        else{
            result.append("Student");
        }
        return result;
    }*/
}
