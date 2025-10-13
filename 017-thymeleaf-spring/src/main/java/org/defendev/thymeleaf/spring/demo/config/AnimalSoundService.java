package org.defendev.thymeleaf.spring.demo.config;

import org.springframework.stereotype.Service;



@Service("animalSoundService")
public class AnimalSoundService {

    public String getCatSound() {
        return "Meow meow";
    }

    public String getOwlSound() {
        return "Hoot hoot";
    }

    public String getCowSound() {
        return "Mooooo";
    }

}
