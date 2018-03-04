package com.example.demo.repository;


import com.example.demo.entity.document.TypeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TypeDocumentRepository extends JpaRepository<TypeDocument, Integer> {
}
