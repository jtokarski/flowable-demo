package org.defendev.restassured.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import static org.springframework.core.env.Profiles.of;



@Component
public class ProfileFlags {

    private final boolean local;

    private final boolean qa;

    private final boolean prod;

    @Autowired
    public ProfileFlags(Environment environment) {
        this.local = environment.acceptsProfiles(of(RestAssuredProfile.local));
        this.qa = environment.acceptsProfiles(of(RestAssuredProfile.qa));
        this.prod = environment.acceptsProfiles(of(RestAssuredProfile.prod));
    }

    public boolean local() {
        return local;
    }

    public boolean qa() {
        return qa;
    }

    public boolean prod() {
        return prod;
    }

}
