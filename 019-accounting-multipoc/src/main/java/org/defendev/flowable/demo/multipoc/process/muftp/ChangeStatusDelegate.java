package org.defendev.flowable.demo.multipoc.process.muftp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.DelegateHelper;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;



@Component("muftp_ChangeStatusDelegate")
public class ChangeStatusDelegate implements JavaDelegate {

    private static final Logger log = LogManager.getLogger();

    @Override
    public void execute(DelegateExecution execution) {
        final Object status = DelegateHelper.getFieldExpression(execution, "status").getValue(execution);
        log.info("Setting status to00o " + status);
    }

}
