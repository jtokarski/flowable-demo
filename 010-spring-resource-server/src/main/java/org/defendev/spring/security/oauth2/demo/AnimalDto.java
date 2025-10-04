package org.defendev.spring.security.oauth2.demo;



public record AnimalDto(
    String species,
    String name,
    Integer age,
    Boolean isPet
) { }
