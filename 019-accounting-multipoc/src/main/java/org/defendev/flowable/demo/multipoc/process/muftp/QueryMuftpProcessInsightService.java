package org.defendev.flowable.demo.multipoc.process.muftp;


import org.defendev.common.domain.query.Query;
import org.defendev.common.domain.query.result.QueryResult;
import org.defendev.flowable.demo.multipoc.dto.MuftpProcessInsightDto;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class QueryMuftpProcessInsightService {

    private final RuntimeService runtimeService;

    @Autowired
    public QueryMuftpProcessInsightService(ProcessEngine processEngine) {
        runtimeService = processEngine.getRuntimeService();
    }

    public QueryResult<MuftpProcessInsightDto> execute(MuftpProcessInsightQuery query) {




        return QueryResult.success(new MuftpProcessInsightDto());
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
