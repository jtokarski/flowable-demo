package org.defendev.flowable.demo.multipoc.process.muftp;

import org.defendev.flowable.demo.multipoc.model.FinancialTransaction;
import org.defendev.flowable.demo.multipoc.model.MuftpProcess;
import org.defendev.flowable.demo.multipoc.repository.MuftpProcessRepository;
import org.defendev.flowable.demo.multipoc.service.SecurityContextService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.defendev.flowable.demo.multipoc.model.FinancialTransaction.LifecycleStatus.NEW;



@Component("muftp_CreateProcessDataContainerDelegate")
public class CreateProcessDataContainerDelegate implements ExecutionListener {

    private final SecurityContextService securityContextService;

    private final MuftpProcessRepository muftpProcessRepository;

    @Autowired
    public CreateProcessDataContainerDelegate(SecurityContextService securityContextService, MuftpProcessRepository muftpProcessRepository) {
        this.securityContextService = securityContextService;
        this.muftpProcessRepository = muftpProcessRepository;
    }

    @Override
    public void notify(DelegateExecution execution) {
        final MuftpProcess process = new MuftpProcess();
        final String processInstanceId = execution.getRootProcessInstanceId();
        process.setProcessInstanceId(processInstanceId);
        process.setInitiatedBy(securityContextService.getUserId());
        final FinancialTransaction transaction = new FinancialTransaction();
        transaction.setLifecycleStatus(NEW);
        transaction.setMuftpProcess(process);
        process.setFinancialTransaction(transaction);

        muftpProcessRepository.save(process);

        execution.setVariableLocal(MuftpDefinitionKey.VARIABLE_MUFTP_PROCESS, process);
    }

}
