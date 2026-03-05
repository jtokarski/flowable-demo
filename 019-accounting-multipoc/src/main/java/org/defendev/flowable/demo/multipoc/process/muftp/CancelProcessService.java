package org.defendev.flowable.demo.multipoc.process.muftp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.defendev.common.domain.command.Command;
import org.defendev.common.domain.command.result.CommandResult;
import org.defendev.flowable.demo.multipoc.dto.CancelledMuftpDto;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.eventsubscription.api.EventSubscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static java.util.Objects.nonNull;
import static org.defendev.flowable.demo.multipoc.process.muftp.MuftpDefinitionKey.MESSAGE_CANCEL_MUFTP;


@Service
public class CancelProcessService {

    private static final Logger log = LogManager.getLogger();

    private final RuntimeService runtimeService;

    @Autowired
    public CancelProcessService(ProcessEngine processEngine) {
        this.runtimeService = processEngine.getRuntimeService();
    }

    @Transactional(transactionManager = "platformTransactionManager")
    public CommandResult<CancelledMuftpDto> execute(CancelMuftpProcessCommand command) {

        final EventSubscription eventSubscription = runtimeService.createEventSubscriptionQuery()
            .eventType("message")
            .eventName(MESSAGE_CANCEL_MUFTP)
            .processInstanceId(command.processInstanceId())
            .singleResult();

        if (nonNull(eventSubscription)) {
            runtimeService.messageEventReceived(
                MESSAGE_CANCEL_MUFTP,
                eventSubscription.getExecutionId(),
                Map.of()
            );
        } else {
            log.info("No message subscription found for process instance id " + command.processInstanceId() +
                " - process may have completed already");
        }

        return CommandResult.success(new CancelledMuftpDto(command.processInstanceId()));
    }

    public static class CancelMuftpProcessCommand extends Command {

        private final String processInstanceId;

        public CancelMuftpProcessCommand(String processInstanceId) {
            this.processInstanceId = processInstanceId;
        }

        public String processInstanceId() {
            return processInstanceId;
        }
    }

}
