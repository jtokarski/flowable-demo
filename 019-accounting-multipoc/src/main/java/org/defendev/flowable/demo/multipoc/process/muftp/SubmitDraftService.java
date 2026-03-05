package org.defendev.flowable.demo.multipoc.process.muftp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.defendev.common.domain.command.Command;
import org.defendev.common.domain.command.result.CommandResult;
import org.defendev.flowable.demo.multipoc.dto.SubmittedDraftMuftpDto;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.defendev.flowable.demo.multipoc.process.muftp.MuftpDefinitionKey.TASK_SUBMIT_TRANSACTION_DRAFT;



@Service
public class SubmitDraftService {

    private static final Logger log = LogManager.getLogger();

    private final TaskService taskService;

    @Autowired
    public SubmitDraftService(ProcessEngine processEngine) {
        this.taskService = processEngine.getTaskService();
    }

    @Transactional(transactionManager = "platformTransactionManager")
    public CommandResult<SubmittedDraftMuftpDto> execute(SubmitDraftCommand command) {

        final Task task = taskService.createTaskQuery()
            .processInstanceId(command.processInstanceId)
            .taskDefinitionKey(TASK_SUBMIT_TRANSACTION_DRAFT)
            .singleResult();

        taskService.complete(task.getId());

        return CommandResult.success(new SubmittedDraftMuftpDto(command.processInstanceId()));
    }

    public static class SubmitDraftCommand extends Command {

        private final String processInstanceId;

        public SubmitDraftCommand(String processInstanceId) {
            this.processInstanceId = processInstanceId;
        }

        public String processInstanceId() {
            return processInstanceId;
        }
    }

}
