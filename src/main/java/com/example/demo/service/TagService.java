package com.example.demo.service;

import com.example.demo.entity.document.Tag;
import com.example.demo.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    private TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag findTag(String tagName) {
        return tagRepository.findByTagName(tagName.toLowerCase().replace(" ", ""));
    }

    public void save(Tag tag){
        tag.setTagName(tag.getTagName().toLowerCase().replace(" ", ""));
        tagRepository.save(tag);}
}
