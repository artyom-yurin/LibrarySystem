package com.example.demo.service;

import com.example.demo.entity.document.Author;
import com.example.demo.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    private AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author findById(Integer id) {
        return this.authorRepository.findById(id);
    }

    public Author findByFirstName(String firstName) {
        return this.authorRepository.findByFirstName(firstName);
    }

    public Author findByLastName(String lastName) {
        return this.authorRepository.findByLastName(lastName);
    }

    public List<Author> findAll() {
        return authorRepository.findAll();
    }


    public void save(Author author) { authorRepository.save(author);
    }
}
