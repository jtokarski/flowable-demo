package org.defendev.flowable.demo.multipoc.process;



public interface BpmnDefinitionKey {

    public static final String TASK_VARIABLE_COMPLETION_TYPE = "completionType";

    public enum TaskCompletionType {
        completed,
        cancelled
    }

}
