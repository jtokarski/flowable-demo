package org.defendev.flowable.demo.multipoc.process.muftp;


import org.defendev.common.domain.query.Query;
import org.defendev.common.domain.query.result.QueryResult;
import org.defendev.flowable.demo.multipoc.dto.MuftpProcessInsightDto;
import org.defendev.flowable.demo.multipoc.model.MuftpProcess;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



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

        final MuftpProcess muftpProcess = (MuftpProcess) runtimeService.createVariableInstanceQuery()
            .processInstanceId(query.getProcessInstanceId())
            .variableName(MuftpDefinitionKey.VARIABLE_MUFTP_PROCESS)
            .singleResult()
            .getValue();

        final Long financialTransactionId = muftpProcess.getFinancialTransaction().getId();

        return QueryResult.success(new MuftpProcessInsightDto(String.valueOf(financialTransactionId)));
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
