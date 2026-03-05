package org.defendev.flowable.demo.multipoc.dto.processinsight;

import java.util.List;



public record ActiveTaskGroupDto(
    String definitionKey,
    List<ActiveTaskDto> tasks
) { }
