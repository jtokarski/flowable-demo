package org.defendev.spring.core.demo;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
//
// Usually the @Enable... annotations come with more @Import(s), not only ImportBeanDefinitionRegistrar(s)
//
@Import({ DataSourceCheckerRegistrar.class })
public @interface EnableDataSourceChecker {

    String dataSourceRef() default "dataSource";

}
