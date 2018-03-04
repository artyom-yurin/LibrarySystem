package com.example.demo.controller;

import com.example.demo.repository.TagRepository;
import com.example.demo.service.TagService;
import org.springframework.stereotype.Controller;

@Controller
public class TagController {
    private TagService tagService;

    TagController(TagRepository tagRepository)
    {
        tagService = new TagService(tagRepository);
    }
}
