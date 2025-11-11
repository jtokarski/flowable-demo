package org.defendev.flowable.demo.multipoc.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.defendev.common.domain.query.Query;
import org.defendev.common.domain.query.result.QueryResult;
import org.defendev.flowable.demo.multipoc.dto.SimpleJooqDto;
import org.defendev.flowable.demo.multipoc.jooq.enums.FinancialtransactionLifecyclestatus;
import org.defendev.flowable.demo.multipoc.jooq.tables.Financialtransaction;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.nonNull;
import static org.defendev.flowable.demo.multipoc.jooq.AmpCore.AMP_CORE;



@Service
public class QuerySimpleJooqService {

    private static final Logger log = LogManager.getLogger();

    private final DSLContext jooqDslContext;

    @Autowired
    public QuerySimpleJooqService(DSLContext jooqDslContext) {
        this.jooqDslContext = jooqDslContext;
    }

    @Transactional
    public QueryResult<List<SimpleJooqDto>> execute(SimpleJooqQuery query) {
        final long notId = 1001L;

        final Financialtransaction ft = AMP_CORE.FINANCIALTRANSACTION.as("ft");
        final List<SimpleJooqDto> data = jooqDslContext
            .select(ft.ID, ft.LIFECYCLESTATUS)
            .from(ft)
            .where(ft.ID.ne(notId))
            .stream()
            .map(record -> {
                final Long id = record.get(ft.ID);
                final FinancialtransactionLifecyclestatus lifecycleStatusEnum = record.get(ft.LIFECYCLESTATUS);
                final String lifecycleStatus = nonNull(lifecycleStatusEnum) ? lifecycleStatusEnum.name() : null;
                return new SimpleJooqDto(String.valueOf(id), lifecycleStatus);
            })
            .toList();

        return QueryResult.success(data);
    }

    public static class SimpleJooqQuery extends Query { }

}
