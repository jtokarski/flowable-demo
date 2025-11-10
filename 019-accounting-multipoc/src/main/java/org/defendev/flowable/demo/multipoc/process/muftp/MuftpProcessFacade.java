package org.defendev.flowable.demo.multipoc.process.muftp;

import org.defendev.common.domain.command.result.CommandResult;
import org.defendev.common.domain.query.result.QueryResult;
import org.defendev.flowable.demo.multipoc.dto.InitializedMuftpProcessDto;
import org.defendev.flowable.demo.multipoc.dto.MuftpProcessInsightDto;
import org.defendev.flowable.demo.multipoc.process.muftp.InitializeProcessService.InitializeMuftpProcessCommand;
import org.defendev.flowable.demo.multipoc.process.muftp.QueryProcessInsightService.MuftpProcessInsightQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class MuftpProcessFacade {

    private final QueryProcessInsightService queryProcessInsightService;

    private final InitializeProcessService initializeProcessService;

    @Autowired
    public MuftpProcessFacade(QueryProcessInsightService queryProcessInsightService,
                              InitializeProcessService initializeProcessService) {
        this.queryProcessInsightService = queryProcessInsightService;
        this.initializeProcessService = initializeProcessService;
    }

    public QueryResult<MuftpProcessInsightDto> queryMuftpProcessInsight(String processInstanceId) {
        return queryProcessInsightService.execute(new MuftpProcessInsightQuery(processInstanceId));
    }

    public CommandResult<InitializedMuftpProcessDto> initializeProcess() {
        return initializeProcessService.execute(new InitializeMuftpProcessCommand());
    }

}
