package org.defendev.flowable.demo.multipoc;

import static java.lang.String.format;



public class TestMemoryUtil {

    public static void printLabel(Object value, String label) {
        System.out.println(format("--- --- %s : %s", label, value));
    }

    public static void printBar() {
        System.out.println("--- -------------------------------------------- ---");
    }

}
