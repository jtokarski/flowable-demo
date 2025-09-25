package org.defendev.spring.security.oauth2.client.demo.dto;

import java.util.List;



public record MockAuthzKeyResponseDto(List<MockAuthzKeyDto> keys) { }
