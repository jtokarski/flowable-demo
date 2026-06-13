package org.defendev.restassured.demo.spring;

import org.defendev.restassured.demo.ProfileFlags;
import org.defendev.restassured.demo.RestAssuredProfile;
import org.defendev.restassured.demo.testmemory.TestMemory;

import static org.defendev.restassured.demo.testmemory.TestMemoryUtil.printBar;
import static org.defendev.restassured.demo.testmemory.TestMemoryUtil.printLabel;



public class DemoMemory extends TestMemory {

    private final ProfileFlags profileFlags;

    /*
     *
     * Could also have some *UriBuilders field for generation of deeplinks
     *
     */

    public String generatedId = "";

    public DemoMemory(ProfileFlags profileFlags) {
        this.profileFlags = profileFlags;
    }

    @Override
    public void print() {
        printBar();
        printBar();
        printLabel(getProfile(), "profile");
        printLabel(generatedId, "generatedId");
        printBar();
        printBar();
    }

    private String getProfile() {
        final String profile;
        if (profileFlags.prod()) {
            profile = RestAssuredProfile.prod;
        } else if (profileFlags.qa()) {
            profile = RestAssuredProfile.qa;
        } else if (profileFlags.local()) {
            profile = RestAssuredProfile.local;
        } else {
            profile = ".?.";
        }
        return profile;
    }

}
