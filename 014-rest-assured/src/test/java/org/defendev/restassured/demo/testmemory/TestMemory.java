package org.defendev.restassured.demo.testmemory;



/*
 * "Test Memory" Utilities
 *
 * Intended for tests that serve more as a bot to get into particular stage of some long business
 * process in the application.
 * The goal is to have - at the end of test execution, regardless of whether success or fail - some
 * inventory of crucial ids, and (deep)links. And so that it's accessible with the least amount
 * of scrolling.
 *
 * So @AfterEach uses shutdown hook to delay output print as much as possible.
 *
 * Guidelines and conventions:
 *   - the *Memory classes to have non-final, public fields, always initialized with non-null value
 *   - a field of a *Memory class can be a collection of other *Memory class
 *   - possibly extract interface from *Memory classes with print() method
 *   - a variable or field of some *Memory class to have suffix *Mem or *Mems (for collections)
 *
 * Pro-tip:
 *   - when adding some logging to application code (to be logged during test execution), put the label
 *     AT THE END of log statement, so that when searched with Ctrl+F, you don't need to do
 *     horizontal scrolling.
 *
 */
public abstract class TestMemory {

    public abstract void print();

}
