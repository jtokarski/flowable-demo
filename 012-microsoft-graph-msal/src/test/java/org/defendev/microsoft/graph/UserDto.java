package org.defendev.microsoft.graph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;



public class UserDto {

    private final String id;

    private final String givenName;

    private final String surname;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public UserDto(
        @JsonProperty("id") String id,
        @JsonProperty("givenName") String givenName,
        @JsonProperty("surname") String surname
    ) {
        this.id = id;
        this.givenName = givenName;
        this.surname = surname;
    }

    public String getId() {
        return id;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getSurname() {
        return surname;
    }
}
