package com.example.demo.model.document;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Entity
public class JournalArticle{
    @Id
    @Column(name = "JOURNAL_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Size(max = 25)
    @Column(name = "TITLE")
    private String title;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "AUTHOR_ID")
    private Set<Author> authors;

    @Size(max = 25)
    @Column(name = "EDITOR")
    private String editor;

    @Column(name = "PUBLISHING_DATE",columnDefinition = "DATETIME")
    private Date dateOfPublishing;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "TAG_ID")
    private Set<Tag> tags;

    @Column(name = "PRICE")
    private Integer price;
    @Column(name = "COUNT")
    private Integer count;

    public JournalArticle(Integer id, String title, Set<Author> authors, String editor, Date dateOfPublishing, Set<Tag> tags, Integer price, Integer count) {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Date getDateOfPublishing() {
        return dateOfPublishing;
    }

    public void setDateOfPublishing(Date dateOfPublishing) {
        this.dateOfPublishing = dateOfPublishing;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
