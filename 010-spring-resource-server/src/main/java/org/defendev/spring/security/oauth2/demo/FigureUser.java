package org.defendev.spring.security.oauth2.demo;



public class FigureUser {

    private final String userId;

    public FigureUser(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
