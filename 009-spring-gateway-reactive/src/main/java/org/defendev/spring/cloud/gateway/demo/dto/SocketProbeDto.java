package org.defendev.spring.cloud.gateway.demo.dto;



public record SocketProbeDto(
    boolean requestValid,
    boolean success,
    long executionTimeMilliseconds,
    String errorDetail
) { }
