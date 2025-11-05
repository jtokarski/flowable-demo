package org.defendev.flowable.demo.multipoc.controller;

import org.defendev.common.domain.query.result.QueryResult;
import org.defendev.flowable.demo.multipoc.dto.MuftpProcessInsightDto;
import org.defendev.flowable.demo.multipoc.process.muftp.MuftpProcessFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;



@RequestMapping("/")
@Controller
public class EmergencyActionController {

    private final MuftpProcessFacade muftpProcessFacade;

    @Autowired
    public EmergencyActionController(MuftpProcessFacade muftpProcessFacade) {
        this.muftpProcessFacade = muftpProcessFacade;
    }

    @GetMapping(path = "muftp/process/{processInstanceId}/insight")
    public ResponseEntity<MuftpProcessInsightDto> muftpProcessInsight(@PathVariable String processInstanceId) {
        final QueryResult<MuftpProcessInsightDto> queryResult = muftpProcessFacade.queryMuftpProcessInsight(
            processInstanceId);
        if (queryResult.isSuccess()) {
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new MuftpProcessInsightDto());
        } else {
            throw new IllegalArgumentException(""); // todo: proper advice, proper errorDto!
        }
    }

}
