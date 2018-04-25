package com.example.demo.controllers;


import com.example.demo.entity.document.Document;
import com.example.demo.entity.document.Tag;
import com.example.demo.model.SearchModel;
import com.example.demo.service.DocumentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class SearchContr{
    private DocumentService documentService;

    public SearchContr(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * Method for searching the document
     * @param searchModel Model for the search (type of search and search request
     * @return List of all documents found based on search request
     */
    public ArrayList<Document> search(SearchModel searchModel){
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
                        for(Tag tag : document.getTags())
                        {
                            if(tag.getTagName().toLowerCase().equals(substring.toLowerCase().replace(" ", "")))
                            {
                                toReturn.add(document);
                                break;
                            }
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
