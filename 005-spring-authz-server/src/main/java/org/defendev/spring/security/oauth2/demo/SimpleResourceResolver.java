package org.defendev.spring.security.oauth2.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;



public class SimpleResourceResolver extends PathResourceResolver {

    private static final Logger log = LogManager.getLogger();

    @Override
    protected Resource getResource(String resourcePath, Resource location) throws IOException {

        // todo: AllowedResourcePathMatchers (which is mainly the purpose of having this class...)

        return super.getResource(resourcePath, location);
    }

}
