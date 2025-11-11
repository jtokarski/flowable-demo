package org.defendev.flowable.demo.multipoc.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.defendev.common.domain.query.result.QueryResult;
import org.defendev.flowable.demo.multipoc.dto.SimpleJooqDto;
import org.defendev.flowable.demo.multipoc.service.QuerySimpleJooqService;
import org.defendev.flowable.demo.multipoc.service.QuerySimpleJooqService.SimpleJooqQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;



@RequestMapping("/")
@Controller
public class ExempliGratiaController {

    private static final Logger log = LogManager.getLogger();

    private final QuerySimpleJooqService querySimpleJooqService;

    @Autowired
    public ExempliGratiaController(QuerySimpleJooqService querySimpleJooqService) {
        this.querySimpleJooqService = querySimpleJooqService;
    }

    @GetMapping(path = "exempli-gratia/simple-jooq-query")
    public ResponseEntity<List<SimpleJooqDto>> simpleJooqQuery() {
        final QueryResult<List<SimpleJooqDto>> queryResult = querySimpleJooqService.execute(new SimpleJooqQuery());
        if (queryResult.isSuccess()) {
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(queryResult.getData());
        } else {
            throw new IllegalArgumentException(""); // todo: proper advice, proper errorDto!
        }
    }

}
