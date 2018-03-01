package com.example.demo.model.document;

import javax.persistence.Entity;

@Entity
public class JournalArticle extends  Document {

    protected String journalTitle;
    protected String editor;
    protected String dateOfPublishing;

    public JournalArticle(long id, String title, String[] authors, int price, int count, String journalTitle, String editor, String dateOfPublishing) {
        super(id, title, authors, price, count);
        this.journalTitle = journalTitle;
        this.editor = editor;
        this.dateOfPublishing = dateOfPublishing;
    }

    public String getJournalTitle() {
        return journalTitle;
    }

    public void setJournalTitle(String journalTitle) {
        this.journalTitle = journalTitle;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getDateOfPublishing() {
        return dateOfPublishing;
    }

    public void setDateOfPublishing(String dateOfPublishing) {
        this.dateOfPublishing = dateOfPublishing;
    }
}
