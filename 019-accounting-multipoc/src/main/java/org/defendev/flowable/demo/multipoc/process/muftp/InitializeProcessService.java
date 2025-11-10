package org.defendev.flowable.demo.multipoc.process.muftp;

import org.defendev.common.domain.command.Command;
import org.defendev.common.domain.command.result.CommandResult;
import org.defendev.flowable.demo.multipoc.dto.InitializedMuftpProcessDto;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class InitializeProcessService {

    private final RuntimeService runtimeService;

    @Autowired
    public InitializeProcessService(ProcessEngine processEngine) {
        runtimeService = processEngine.getRuntimeService();
    }

    @Transactional(transactionManager = "platformTransactionManager")
    public CommandResult<InitializedMuftpProcessDto> execute(InitializeMuftpProcessCommand command) {
        final ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(MuftpDefinitionKey.PROCESS);
        final String processInstanceId = processInstance.getId();
        return CommandResult.success(new InitializedMuftpProcessDto(processInstanceId, null, null));
    }

    public static class InitializeMuftpProcessCommand extends Command {

    }

}
