package org.defendev.flowable.demo.multipoc.dto;

import java.time.ZonedDateTime;



public record MuftpProcessInsightDto(
    ZonedDateTime processStartTime,
    String financialTransactionId,
    ZonedDateTime financialTransactionCreate
) { }
