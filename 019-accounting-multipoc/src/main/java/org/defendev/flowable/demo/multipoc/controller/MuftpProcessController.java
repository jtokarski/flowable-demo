package org.defendev.flowable.demo.multipoc.controller;

import org.defendev.common.domain.command.result.CommandResult;
import org.defendev.common.domain.exception.CommandFailedException;
import org.defendev.flowable.demo.multipoc.dto.CancelledMuftpDto;
import org.defendev.flowable.demo.multipoc.dto.InitializedMuftpDto;
import org.defendev.flowable.demo.multipoc.dto.SubmittedDraftMuftpDto;
import org.defendev.flowable.demo.multipoc.process.muftp.MuftpProcessFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@RequestMapping("/")
@Controller
public class MuftpProcessController {

    private final MuftpProcessFacade processFacade;

    @Autowired
    public MuftpProcessController(MuftpProcessFacade processFacade) {
        this.processFacade = processFacade;
    }

    @PostMapping(path = "muftp/process/_initialize")
    public ResponseEntity<InitializedMuftpDto> initializeProcess() {
        final CommandResult<InitializedMuftpDto> commandResult = processFacade.initializeProcess();
        if (commandResult.isSuccess()) {
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(commandResult.getData());
        } else {
            throw new CommandFailedException(commandResult);
        }
    }

    @PostMapping(path = "muftp/process/{processInstanceId}/draft/_submit")
    public ResponseEntity<SubmittedDraftMuftpDto> submitDraft(@PathVariable String processInstanceId) {
        final CommandResult<SubmittedDraftMuftpDto> commandResult = processFacade.submitDraft(processInstanceId);
        if (commandResult.isSuccess()) {
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(commandResult.getData());
        } else {
            throw new CommandFailedException(commandResult);
        }
    }

    @PostMapping(path = "muftp/process/{processInstanceId}/_cancel")
    public ResponseEntity<CancelledMuftpDto> cancelProcess(@PathVariable String processInstanceId) {
        final CommandResult<CancelledMuftpDto> commandResult = processFacade.cancelProcess(processInstanceId);
        if (commandResult.isSuccess()) {
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(commandResult.getData());
        } else {
            throw new CommandFailedException(commandResult);
        }
    }

}
