package org.defendev.flowable.demo.multipoc.process.muftp;

import org.defendev.common.domain.command.result.CommandResult;
import org.defendev.common.domain.query.result.QueryResult;
import org.defendev.flowable.demo.multipoc.dto.CancelledMuftpDto;
import org.defendev.flowable.demo.multipoc.dto.InitializedMuftpDto;
import org.defendev.flowable.demo.multipoc.dto.processinsight.ProcessDto;
import org.defendev.flowable.demo.multipoc.dto.SubmittedDraftMuftpDto;
import org.defendev.flowable.demo.multipoc.process.muftp.InitializeProcessService.InitializeMuftpProcessCommand;
import org.defendev.flowable.demo.multipoc.process.muftp.QueryProcessInsightService.MuftpProcessInsightQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class MuftpProcessFacade {

    private final QueryProcessInsightService queryProcessInsightService;

    private final InitializeProcessService initializeProcessService;

    private final SubmitDraftService submitDraftService;

    private final CancelProcessService cancelProcessService;

    @Autowired
    public MuftpProcessFacade(QueryProcessInsightService queryProcessInsightService,
                              InitializeProcessService initializeProcessService, SubmitDraftService submitDraftService,
                              CancelProcessService cancelProcessService) {
        this.queryProcessInsightService = queryProcessInsightService;
        this.initializeProcessService = initializeProcessService;
        this.submitDraftService = submitDraftService;
        this.cancelProcessService = cancelProcessService;
    }

    public QueryResult<ProcessDto> queryMuftpProcessInsight(String processInstanceId) {
        return queryProcessInsightService.execute(new MuftpProcessInsightQuery(processInstanceId));
    }

    public CommandResult<InitializedMuftpDto> initializeProcess() {
        return initializeProcessService.execute(new InitializeMuftpProcessCommand());
    }

    public CommandResult<SubmittedDraftMuftpDto> submitDraft(String processInstanceId) {
        return submitDraftService.execute(new SubmitDraftService.SubmitDraftCommand(processInstanceId));
    }

    public CommandResult<CancelledMuftpDto> cancelProcess(String processInstanceId) {
        return cancelProcessService.execute(new CancelProcessService.CancelMuftpProcessCommand(processInstanceId));
    }

}
