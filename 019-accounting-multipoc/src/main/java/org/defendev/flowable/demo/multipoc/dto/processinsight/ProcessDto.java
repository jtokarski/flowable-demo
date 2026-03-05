package org.defendev.flowable.demo.multipoc.dto.processinsight;

import java.time.ZonedDateTime;
import java.util.List;



public record ProcessDto(
    Boolean unfinished,
    Boolean exists,
    List<ActiveTaskGroupDto> activeTaskGroups,
    ZonedDateTime processStartTime,
    String financialTransactionId,
    ZonedDateTime financialTransactionCreate
) { }
