package com.example.demo.service;


import com.example.demo.entity.document.Document;
import com.example.demo.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {

    private DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public void save(Document document) {
        this.documentRepository.save(document);
    }

    public void remove(Integer id) {
        this.documentRepository.removeDocumentById(id);
    }

    public Document findById(Integer id) {
        return this.documentRepository.findById(id);
    }

    public List<Document> getAllDocuments() {
        return this.documentRepository.findAll();
    }
}
