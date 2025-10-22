package org.defendev.thymeleaf.spring.demo.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.Set;



@Import({ AnimalSoundService.class })
@Configuration
public class TemplatingApplicationConfig {

    private ITemplateResolver textTemplateResolver(ApplicationContext applicationContext) {
        final SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setOrder(1);
        resolver.setApplicationContext(applicationContext);
        resolver.setResolvablePatterns(Set.of("*.txt", "**/*.txt"));
        resolver.setPrefix("classpath:/eg-templates/");
        resolver.setSuffix("");
        resolver.setTemplateMode(TemplateMode.TEXT);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(true);
        return resolver;
    }

    private ITemplateResolver htmlTemplateResolver(ApplicationContext applicationContext) {
        final SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setOrder(2);
        /*
         * This does not expose beans to template expressions. It only delegates
         * Resource resolution to  ApplicationContext.getResource().
         *
         */
        resolver.setApplicationContext(applicationContext);
        resolver.setResolvablePatterns(Set.of("*.html", "**/*.html"));
        resolver.setPrefix("classpath:/eg-templates/");
        resolver.setSuffix("");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(true);
        return resolver;
    }

    /*
     * Turns out that it won't work. The (core) TemplateEngine needs ongl as well.
     * Apparently it's not intended to pair with SpringResourceTemplateResolver...
     *
     */
    @Bean
    public ITemplateEngine baseTemplateEngine(ApplicationContext applicationContext) {
        final TemplateEngine engine = new TemplateEngine();
        engine.addTemplateResolver(textTemplateResolver(applicationContext));
        engine.addTemplateResolver(htmlTemplateResolver(applicationContext));
        return engine;
    }

    @Bean
    public ITemplateEngine springTemplateEngine(ApplicationContext applicationContext) {
        final SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.addTemplateResolver(textTemplateResolver(applicationContext));
        engine.addTemplateResolver(htmlTemplateResolver(applicationContext));
        engine.setEnableSpringELCompiler(true);
        return engine;
    }

}
