package org.defendev.flowable.demo.multipoc.controller;

import org.defendev.common.domain.command.result.CommandResult;
import org.defendev.common.domain.exception.CommandFailedException;
import org.defendev.flowable.demo.multipoc.dto.InitializedMuftpProcessDto;
import org.defendev.flowable.demo.multipoc.process.muftp.MuftpProcessFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@RequestMapping("/")
@Controller
public class MuftpProcessController {

    private final MuftpProcessFacade muftpProcessFacade;

    @Autowired
    public MuftpProcessController(MuftpProcessFacade muftpProcessFacade) {
        this.muftpProcessFacade = muftpProcessFacade;
    }

    @PostMapping(path = "muftp/process")
    public ResponseEntity<InitializedMuftpProcessDto> initializeProcess() {
        final CommandResult<InitializedMuftpProcessDto> commandResult = muftpProcessFacade.initializeProcess();
        if (commandResult.isSuccess()) {
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(commandResult.getData());
        } else {
            throw new CommandFailedException(commandResult);
        }
    }

}
