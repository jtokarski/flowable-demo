package org.defendev.thymeleaf.spring.demo;

import org.defendev.thymeleaf.spring.demo.config.TemplatingApplicationConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.expression.ThymeleafEvaluationContext;

import static org.assertj.core.api.Assertions.assertThat;



public class ThymeleafSpringTest {

    private ApplicationContext context;

    @BeforeEach
    public void setUp() {
        final AnnotationConfigApplicationContext contextImpl = new AnnotationConfigApplicationContext();
        contextImpl.register(TemplatingApplicationConfig.class);
        contextImpl.refresh();
        context = contextImpl;
    }

    @Test
    public void shouldAccessContextBeansWithSpel() {
        final ITemplateEngine springTemplateEngine = context.getBean("springTemplateEngine", ITemplateEngine.class);

        final Context thymeleafContext = new Context();

        /*
         * Whenever programmatically processing the template, this is a critical step: Manually register
         * the Bean Resolver.
         *
         */
        final ThymeleafEvaluationContext evaluationContext = new ThymeleafEvaluationContext(context, null);
        thymeleafContext.setVariable(ThymeleafEvaluationContext.THYMELEAF_EVALUATION_CONTEXT_CONTEXT_VARIABLE_NAME,
            evaluationContext);

        thymeleafContext.setVariable("thymeleafCtxVariable", "..::ooo::..");

        final String springNoSpelTxt = springTemplateEngine.process("no-spel.th.txt", thymeleafContext);
        final String springNoSpelHtml = springTemplateEngine.process("no-spel.th.html", thymeleafContext);

        final String springWithSpelTxt = springTemplateEngine.process("with-spel.th.txt", thymeleafContext);
        final String springWithSpelHtml = springTemplateEngine.process("with-spel.th.html", thymeleafContext);

        assertThat(springNoSpelTxt).isNotBlank();
        assertThat(springNoSpelHtml).isNotBlank();
        assertThat(springWithSpelTxt).isNotBlank();
        assertThat(springWithSpelHtml).isNotBlank();
    }

}
