package org.defendev.flowable.demo.multipoc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import static org.springframework.core.env.Profiles.of;



@Component
public class ProfileFlags {

    private final boolean timeTravel;

    @Autowired
    public ProfileFlags(Environment environment) {
        this.timeTravel = environment.acceptsProfiles(of(DefendevAccountingProfile.timeTravel));
    }

    public boolean timeTravel() {
        return timeTravel;
    }

}
