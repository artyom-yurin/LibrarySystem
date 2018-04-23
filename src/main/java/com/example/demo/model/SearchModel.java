package com.example.demo.model;

public class SearchModel {
    private String searchType;
    private String searchQuery;

    SearchModel() {
    }

    public SearchModel(String searchType, String searchQuery) {
        this.searchType = searchType;
        this.searchQuery = searchQuery;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }
}
