package org.defendev.spring.core.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;



@SpringJUnitConfig(classes = { TestContext.class })
public class MyFirstEnableAnnotationTest {

    @Test
    public void contextStarts(@Autowired DataSourceChecker checker) {
        assertThat(checker).isNotNull();
        assertThat(checker.isH2Database("s4les")).isTrue();
    }

}
