package org.defendev.spring.security.oauth2.demo;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Map;



@Component
public class DefendevInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetails(Map.of("defendevApp", "005-spring-authz-server"));
    }

}
