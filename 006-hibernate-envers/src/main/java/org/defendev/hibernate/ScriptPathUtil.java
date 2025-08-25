package org.defendev.hibernate;

import java.nio.file.Files;
import java.nio.file.Path;



public class ScriptPathUtil {

    public static String createScriptPath(String rawPath) {
        final Path path = Path.of(rawPath);
        final String pathString = path.toString();
        System.out.println("-----------------------------------------");
        System.out.println("--- Path parent exists: " + Files.exists(path.getParent()));
        System.out.println("--- Writing DDL script to: " + pathString);
        System.out.println("-----------------------------------------");
        final String verifiedPath = pathString;
        return verifiedPath;
    }

}
