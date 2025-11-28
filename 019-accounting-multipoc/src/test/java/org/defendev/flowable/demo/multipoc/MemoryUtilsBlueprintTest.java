package org.defendev.flowable.demo.multipoc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;



public class MemoryUtilsBlueprintTest {

    @BeforeEach
    public void setUp() {

    }

    @AfterEach
    public void tearDown() {
        /*
         * "Test Memory" Utilities
         *
         * Intended for tests that serve more as a bot to get into particular stage of some long business
         * process in the application.
         * The goal is to have - at the end of test execution, regardless of whether success or fail - some
         * inventory of crucial ids, and (deep)links. And so that it's accessible with the least amount of scrolling.
         *
         * So @AfterEach uses shutdown hook to delay output print as much as possible.
         *
         *
         * Guidelines and conventions:
         *   - the *Memory classes to have non-final, public fields, always initialized with non-null value
         *   - a field of a *Memory class can be a collection of other *Memory class
         *   - possibly extract interface from *Memory classes with print() method
         *   - a variable or field of some *Memory class to have suffix *Mem or *Mems (for collections)
         *
         *
         * todo: when printing something to console consider putting the label AT THE END, co that when you search
         *   with ctrl+f, you don't have to make horizontal mouse scroll.
         *
         * todo: introduce the concept of "profile" to generate per-environment deeplinks. They rather won't be
         *   Spring profiles as I don't want to rely on Spring. I'll have to find some substitute.
         *
         */

        Runtime.getRuntime().addShutdownHook(Thread.ofVirtual().unstarted(() -> {
            TestMemoryUtil.printBar();
            TestMemoryUtil.printBar();
            TestMemoryUtil.printBar();
            TestMemoryUtil.printLabel(501109, "procId");
            TestMemoryUtil.printBar();
            TestMemoryUtil.printBar();
            TestMemoryUtil.printBar();
        }));

    }

    @Test
    public void ttt() throws UnknownHostException {

        final String string = InetAddress.getLocalHost().toString();

        System.out.println("string = " + string);
        //        throw new IllegalStateException(")");

    }


}
