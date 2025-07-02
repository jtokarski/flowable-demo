package org.defendev.spring.cloud.gateway.demo;

import org.apache.logging.log4j.core.pattern.NameAbbreviator;


/*
 * Todo: move this tool to defendev log4j lib.
 *
 *
 */
public class ClassNameAbbreviation {

    /*
     * I'm not sure if NameAbbreviator, but assume it is.
     */
    private static final NameAbbreviator abbreviator = NameAbbreviator.getAbbreviator("1.");

    public static final String abbreviatedFqcn(Object o) {
        final StringBuilder stringBuilder = new StringBuilder();
        abbreviator.abbreviate(o.getClass().getCanonicalName(), stringBuilder);
        return stringBuilder.toString();
    }

}
