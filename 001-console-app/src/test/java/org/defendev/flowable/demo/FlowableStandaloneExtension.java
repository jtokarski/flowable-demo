package org.defendev.flowable.demo;

import org.defendev.flowable.demo.consoleapp.HolidayRequestApp;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.test.FlowableExtension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;
import static org.apache.commons.lang3.Validate.validState;



public class FlowableStandaloneExtension extends FlowableExtension {

    private static final Map<String, ExtensionContext.Namespace> NAMESPACES = new ConcurrentHashMap<>();

    private String resolveProcessEngineName(ExtensionContext context) {
        final String processEngineNameMethod = AnnotationSupport
            .findAnnotation(context.getRequiredTestMethod(), ProcessEngineName.class)
            .map(ProcessEngineName::name)
            .orElse(null);
        final String processEngineNameClass = AnnotationSupport
            .findAnnotation(context.getRequiredTestClass(), ProcessEngineName.class)
            .map(ProcessEngineName::name)
            .orElse(null);

        final String processEngineName = firstNonNull(processEngineNameMethod, processEngineNameClass);
        validState(nonNull(processEngineName), "ProcessEngine must be specified - use annotation @ProcessEngineName");
        return processEngineName;
    }

    @Override
    protected ProcessEngine createProcessEngine(ExtensionContext context) {
        final String processEngineName = resolveProcessEngineName(context);
        switch (processEngineName) {
            case "holidayRequestAppEngine":
                return HolidayRequestApp.setupProcessEngineConfiguration().buildProcessEngine();
            case "emptyTestEngine":
                return TestEngines.setupProcessEngineConfiguration().buildProcessEngine();
            default:
                throw new IllegalStateException(format("Unknown Process Engine Name [%s]", processEngineName));
        }
    }

    @Override
    protected ExtensionContext.Store getStore(ExtensionContext context) {
        final String engineName = resolveProcessEngineName(context);
        final ExtensionContext.Namespace namespace = NAMESPACES.computeIfAbsent(engineName,
            name -> ExtensionContext.Namespace.create(name));
        return context.getRoot().getStore(namespace);
    }

}
