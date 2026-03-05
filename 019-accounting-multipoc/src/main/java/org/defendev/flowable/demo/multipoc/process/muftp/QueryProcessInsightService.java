package org.defendev.flowable.demo.multipoc.process.muftp;

import org.defendev.common.domain.query.Query;
import org.defendev.common.domain.query.result.QueryResult;
import org.defendev.flowable.demo.multipoc.dto.processinsight.ActiveTaskDto;
import org.defendev.flowable.demo.multipoc.dto.processinsight.ActiveTaskGroupDto;
import org.defendev.flowable.demo.multipoc.dto.processinsight.ProcessDto;
import org.defendev.flowable.demo.multipoc.model.MuftpProcess;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.defendev.common.stream.Streams.stream;
import static org.defendev.common.time.TimeUtil.ZULU_ZONE_ID;



/*
 * Wishlist - driven by historical emergency needs, not by high-level design nor pursuit
 * of concise output (elegant design refactor postponed to unknown future).
 *
 * - is unfinished, i.e. is visible to RuntimeService ?
 * - exists, i.e. can it be found at all?
 *
 *
 */
@Service
public class QueryProcessInsightService {

    private final RuntimeService runtimeService;

    private final HistoryService historyService;

    private final TaskService taskService;

    @Autowired
    public QueryProcessInsightService(ProcessEngine processEngine) {
        runtimeService = processEngine.getRuntimeService();
        historyService = processEngine.getHistoryService();
        taskService = processEngine.getTaskService();
    }

    @Transactional
    public QueryResult<ProcessDto> execute(MuftpProcessInsightQuery query) {
        final Boolean unfinished = queryUnfinished(query);
        final Boolean exists = queryExists(query);
        final List<ActiveTaskGroupDto> activeTaskGroups = queryActiveTasks(query);

        final HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
            .processInstanceId(query.getProcessInstanceId())
            .processDefinitionKey(MuftpDefinitionKey.PROCESS)
            .includeProcessVariables()
            .singleResult();

        final MuftpProcess muftpProcess = (MuftpProcess) historyService.createHistoricVariableInstanceQuery()
            .processInstanceId(query.getProcessInstanceId())
            .variableName(MuftpDefinitionKey.VARIABLE_MUFTP_PROCESS)
            .singleResult()
            .getValue();

        // Demonstrates that Flowable time travel works fine
        final Date startTime = processInstance.getStartTime();

        // Demonstrates that Spring Data time travel works fine
        final LocalDateTime financialTransactionCreateZulu = muftpProcess.getFinancialTransaction().getCreateZulu();

        // Demonstrates that Hibernate Envers time travel works fine
        // todo

        // Demonstrates that in my Java code time travel works (calls to injected IClockManager)
        // todo

        final Long financialTransactionId = muftpProcess.getFinancialTransaction().getId();

        final ProcessDto dto = new ProcessDto(
            unfinished,
            exists,
            activeTaskGroups,
            ofNullable(startTime).map(t -> t.toInstant().atZone(ZULU_ZONE_ID)).orElse(null),
            String.valueOf(financialTransactionId),
            financialTransactionCreateZulu.atZone(ZULU_ZONE_ID)
        );
        return QueryResult.success(dto);
    }

    private boolean queryUnfinished(MuftpProcessInsightQuery query) {
        final ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
            .processInstanceId(query.getProcessInstanceId())
            .singleResult();
        return nonNull(processInstance);
    }

    private boolean queryExists(MuftpProcessInsightQuery query) {
        final HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
            .processInstanceId(query.getProcessInstanceId())
            .singleResult();
        return nonNull(historicProcessInstance);
    }

    private List<ActiveTaskGroupDto> queryActiveTasks(MuftpProcessInsightQuery query) {
        final List<Task> tasks = taskService.createTaskQuery()
            .processInstanceId(query.getProcessInstanceId())
            .active()
            .list();
        return stream(tasks)
            .collect(Collectors.groupingBy(Task::getTaskDefinitionKey, Collectors.toList()))
            .entrySet().stream()
            .map(entry -> new ActiveTaskGroupDto(
                entry.getKey(),
                stream(entry.getValue()).map(task -> new ActiveTaskDto(task.getId())).collect(Collectors.toList())
            ))
            .collect(Collectors.toList());
    }

    public static class MuftpProcessInsightQuery extends Query {

        private final String processInstanceId;

        public MuftpProcessInsightQuery(String processInstanceId) {
            this.processInstanceId = processInstanceId;
        }

        public String getProcessInstanceId() {
            return processInstanceId;
        }

    }
}
