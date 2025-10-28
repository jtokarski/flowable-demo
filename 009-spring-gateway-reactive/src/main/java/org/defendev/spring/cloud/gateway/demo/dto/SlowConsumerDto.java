package org.defendev.spring.cloud.gateway.demo.dto;

import java.util.List;



public record SlowConsumerDto(
    int delayStartHandshake,
    int delayWriteRequest,
    int delayByteRead,
    long executionTimeMilliseconds,
    int totalBytes,
    List<String> completedSteps
) { }
