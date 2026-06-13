package org.defendev.restassured.demo.testmemory;

import static java.lang.String.format;



public class TestMemoryUtil {

    public static void printLabel(Object value, String label) {
        System.out.println(format("--- --- %s : %s", label, value));
    }

    /**
     * To be used in lower-level test memory classes to separate sections of top-level test memory.
     *
     */
    public static void printSpacer() {
        System.out.println("--- .  .  .  .  .  .  .  .  .  .  .  .  .  .  .  .    ---");
    }

    /**
     * For enclosing output of the top-level test memory.
     *
     */
    public static void printBar() {
        System.out.println("--- ------------------------------------------------- ---");
    }

}
