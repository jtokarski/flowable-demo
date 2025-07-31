package org.defendev.microsoft.graph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;



public class UserResponseDto {

    private final List<UserDto> value;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public UserResponseDto(
        @JsonProperty("value") List<UserDto> value
    ) {
        this.value = value;
    }

    public List<UserDto> getValue() {
        return value;
    }

}
