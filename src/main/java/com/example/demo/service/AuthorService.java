package com.example.demo.service;

import com.example.demo.entity.document.Author;
import com.example.demo.repository.AuthorRepository;
import org.springframework.stereotype.Service;

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

    public Iterable<Author> findAll() {return findAll();}
}
