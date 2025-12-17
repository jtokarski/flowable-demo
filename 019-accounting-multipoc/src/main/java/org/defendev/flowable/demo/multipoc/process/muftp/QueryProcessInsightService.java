package org.defendev.flowable.demo.multipoc.process.muftp;

import org.defendev.common.domain.query.Query;
import org.defendev.common.domain.query.result.QueryResult;
import org.defendev.flowable.demo.multipoc.dto.MuftpProcessInsightDto;
import org.defendev.flowable.demo.multipoc.model.MuftpProcess;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

import static java.util.Optional.ofNullable;
import static org.defendev.common.time.TimeUtil.ZULU_ZONE_ID;



@Service
public class QueryProcessInsightService {

    private final RuntimeService runtimeService;

    private final HistoryService historyService;

    @Autowired
    public QueryProcessInsightService(ProcessEngine processEngine) {
        runtimeService = processEngine.getRuntimeService();
        historyService = processEngine.getHistoryService();
    }

    @Transactional
    public QueryResult<MuftpProcessInsightDto> execute(MuftpProcessInsightQuery query) {
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

        final MuftpProcessInsightDto dto = new MuftpProcessInsightDto(
            ofNullable(startTime).map(t -> t.toInstant().atZone(ZULU_ZONE_ID)).orElse(null),
            String.valueOf(financialTransactionId),
            financialTransactionCreateZulu.atZone(ZULU_ZONE_ID)
        );
        return QueryResult.success(dto);
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
