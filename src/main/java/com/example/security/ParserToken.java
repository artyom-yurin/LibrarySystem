package com.example.security;

import javax.persistence.criteria.CriteriaBuilder;

public class ParserToken {
    public final Integer id;
    public final String username;
    public final String role;
    public final String position;

    public ParserToken(Integer id, String username, String role, String position) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.position = position;
    }
}
