package org.defendev.flowable.demo.consoleapp;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import static java.lang.String.format;



public class SendRejectionMail implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        System.out.println(format("Sending rejection email to [%s]", execution.getVariable("employee")));
    }

}
