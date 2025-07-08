package org.defendev.spring.security.oauth2.demo;



public class DeepDto {

    private final String deepInsight;

    private final String authorizationHeaderLength;

    public DeepDto(String deepInsight, String authorizationHeaderLength) {
        this.deepInsight = deepInsight;
        this.authorizationHeaderLength = authorizationHeaderLength;
    }

    public String getDeepInsight() {
        return deepInsight;
    }

    public String getAuthorizationHeaderLength() {
        return authorizationHeaderLength;
    }
}
