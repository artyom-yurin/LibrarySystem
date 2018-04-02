package com.example.demo.service;

import com.example.demo.entity.document.Tag;
import com.example.demo.repository.TagRepository;
import org.springframework.stereotype.Service;

@Service
public class TagService {
    private TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    Tag findById(Integer id) {
        return tagRepository.findById(id);
    }
}
