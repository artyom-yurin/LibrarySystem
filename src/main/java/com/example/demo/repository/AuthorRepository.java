package com.example.demo.repository;

import com.example.demo.entity.document.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    Author findById(Integer id);

    Author findByFirstName(String firstName);

    Author findByLastName(String lastName);
}
