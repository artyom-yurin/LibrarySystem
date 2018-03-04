package com.example.demo.service;

import com.example.demo.entity.document.TypeDocument;
import com.example.demo.repository.TypeDocumentRepository;
import org.springframework.stereotype.Service;

@Service
public class TypeDocumentService {
    private TypeDocumentRepository typeDocumentRepository;

    public TypeDocumentService(TypeDocumentRepository typeDocumentRepository) {
        this.typeDocumentRepository = typeDocumentRepository;
    }
}
