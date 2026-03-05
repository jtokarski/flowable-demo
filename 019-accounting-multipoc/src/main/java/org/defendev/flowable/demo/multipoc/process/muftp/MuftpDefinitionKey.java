package org.defendev.flowable.demo.multipoc.process.muftp;

import org.defendev.flowable.demo.multipoc.process.BpmnDefinitionKey;



public interface MuftpDefinitionKey extends BpmnDefinitionKey {

    String PROCESS = "madeup-financial-transaction-posting-process";

    String VARIABLE_MUFTP_PROCESS = "muftpProcess";

    String TASK_SUBMIT_TRANSACTION_DRAFT = "submit-transaction-draft-task";

    String MESSAGE_CANCEL_MUFTP = "cancel-muftp";

}
