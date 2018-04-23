package com.example.demo.service;

import com.example.demo.entity.document.Publisher;
import com.example.demo.repository.PublisherRepository;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {
    private PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public Publisher findById(Integer id) {
        return publisherRepository.findById(id);
    }

    public Publisher findByPublisherName(String publisherName) {
        return publisherRepository.findByPublisherName(publisherName);
    }

    public void save(String publisherName) { publisherRepository.save(new Publisher(publisherName.toLowerCase()));}
}
