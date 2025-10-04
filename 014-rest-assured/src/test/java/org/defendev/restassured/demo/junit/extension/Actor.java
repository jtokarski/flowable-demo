package org.defendev.restassured.demo.junit.extension;



public enum Actor {

    user_1("9eef4ff3-19cb-4ebd-821d-80019f6b42e1", "Isaac", "Newton", "isaac_newton"),
    user_2("e779211d-3e93-4ddf-86d1-f513075b0e4e", "Albert", "Einstein", "Alberto_Ein5");

    private final String oid;

    private final String givenName;

    private final String familyName;

    private final String subject;

    Actor(String oid, String givenName, String familyName, String subject) {
        this.oid = oid;
        this.givenName = givenName;
        this.familyName = familyName;
        this.subject = subject;
    }

    public String getOid() {
        return oid;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getSubject() {
        return subject;
    }
}
