package com.example.security;

import javax.persistence.criteria.CriteriaBuilder;

public class ParserToken {
    final Integer id;
    final String username;
    final String role;

    public ParserToken(Integer id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }
}
