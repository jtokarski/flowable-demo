package org.defendev.flowable.demo.multipoc.dto;



public record InitializedMuftpProcessDto(
    String processInstanceId,
    String financialTransactionId,
    String bookkeeperTaskId
) { }
