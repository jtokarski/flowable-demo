package org.defendev.spring.cloud.gateway.demo;



public class CurioDto {

    private String accessToken;

    public CurioDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
