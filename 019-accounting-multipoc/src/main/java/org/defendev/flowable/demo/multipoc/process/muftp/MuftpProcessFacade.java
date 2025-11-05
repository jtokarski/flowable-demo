package org.defendev.flowable.demo.multipoc.process.muftp;

import org.defendev.common.domain.query.result.QueryResult;
import org.defendev.flowable.demo.multipoc.dto.MuftpProcessInsightDto;
import org.defendev.flowable.demo.multipoc.process.muftp.QueryMuftpProcessInsightService.MuftpProcessInsightQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class MuftpProcessFacade {

    private final QueryMuftpProcessInsightService queryMuftpProcessInsightService;

    @Autowired
    public MuftpProcessFacade(QueryMuftpProcessInsightService queryMuftpProcessInsightService) {
        this.queryMuftpProcessInsightService = queryMuftpProcessInsightService;
    }

    public QueryResult<MuftpProcessInsightDto> queryMuftpProcessInsight(String processInstanceId) {
        return queryMuftpProcessInsightService.execute(new MuftpProcessInsightQuery(processInstanceId));
    }

}
