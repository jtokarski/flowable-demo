package org.defendev.spring.core.demo;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.core.env.Environment;



public class DataSourceCheckerRegistrar implements ImportBeanDefinitionRegistrar {

    private final Environment environment;

    public DataSourceCheckerRegistrar(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        final EnableDataSourceChecker enableAnnotation = metadata.getAnnotations().get(EnableDataSourceChecker.class)
            .synthesize();
        final BeanDefinitionBuilder builder = BeanDefinitionBuilder
            .genericBeanDefinition(DataSourceCheckerFactoryBean.class);
        final String dataSourceRef = enableAnnotation.dataSourceRef();
        builder.addPropertyReference("wsxDataSource", dataSourceRef);
        registry.registerBeanDefinition("dsChecker", builder.getBeanDefinition());
    }

}
