package com.example.demo.controller;


import com.example.demo.entity.document.Document;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.model.SearchModel;
import com.example.demo.service.DocumentService;
import com.example.demo.service.UserService;
import com.example.security.ParserToken;
import com.example.security.TokenAuthenticationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
public class SearchController {
    private DocumentService documentService;
    private UserService userService;

    public SearchController(DocumentService documentService, UserService userService) {
        this.documentService = documentService;
        this.userService = userService;
    }

    @GetMapping("/search")
    public Iterable<Document> search(@RequestBody SearchModel searchModel, HttpServletRequest request){
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();

        if(searchModel == null){
            return null;
        }

        ArrayList<Document> toReturn = new ArrayList<>();

        switch(searchModel.getSearchType()){
            case "title":
                String[] toSearch = searchModel.getSearchQuery().split(" ");
                for (String substring : toSearch) {
                    for (Document document : documentService.getAllDocuments()){
                        if(document.getTitle().toLowerCase().contains(substring.toLowerCase()) && !toReturn.contains(document)){
                            toReturn.add(document);
                        }
                    }
                }
                break;
            case "author":
                for (Document document : documentService.getAllDocuments()){
                    if(document.getAuthors().toString().toLowerCase().contains(searchModel.getSearchQuery().toLowerCase()) && !toReturn.contains(document)){
                        toReturn.add(document);
                    }
                }
                break;
            case "tags":
                String[] tags = searchModel.getSearchQuery().split(" ");
                for (String substring : tags) {
                    for (Document document : documentService.getAllDocuments()){
                        if(document.getTags().toString().toLowerCase().contains(substring.toLowerCase()) && !toReturn.contains(document)){
                            toReturn.add(document);
                        }
                    }
                }
                break;
            case "edition":
                Integer edition = Integer.parseInt(searchModel.getSearchQuery());
                for (Document document : documentService.getAllDocuments()){
                    if(edition.equals(document.getEdition()) && !toReturn.contains(document)){
                        toReturn.add(document);
                    }
                }
                break;
            case "editor":
                for (Document document : documentService.getAllDocuments()){
                    if(searchModel.getSearchQuery().equals(document.getEditor()) && !toReturn.contains(document)){
                        toReturn.add(document);
                    }
                }
                break;
            case "publisher":
                for (Document document : documentService.getAllDocuments()){
                    if(searchModel.getSearchQuery().equals(document.getPublisher().getPublisherName()) && !toReturn.contains(document)){
                        toReturn.add(document);
                    }
                }
                break;
        }

        return toReturn;
    }
}
