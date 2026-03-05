package org.defendev.flowable.demo.multipoc.dto;



public record InitializedMuftpDto(
    String processInstanceId,
    String financialTransactionId,
    String bookkeeperTaskId
) { }
