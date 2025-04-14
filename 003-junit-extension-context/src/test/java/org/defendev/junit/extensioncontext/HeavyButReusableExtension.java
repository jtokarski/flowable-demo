package org.defendev.junit.extensioncontext;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;
import static org.apache.commons.lang3.Validate.validState;



/*
 *
 * This sub-project is a research on how to use the features of JUnit extension framework.
 * Primarily for the needs of testing Flowable-based projects that involve using
 * multiple ProcessEngine(s) in parallel in the same VM.
 * The main focus is on the fine-grained control over global caching of extension state.
 *
 * rozk-todo:
 *   In contrary to root ExtensionContext what are the lower-level? Test-Class-level?
 *   Test-Method-level? Or any other differentiation?
 *
 * See:
 *   https://junit.org/junit5/docs/current/user-guide/#extensions-keeping-state
 *   https://docs.spring.io/spring-framework/reference/testing/testcontext-framework/ctx-management/caching.html
 *
 */
public class HeavyButReusableExtension implements ParameterResolver, AfterEachCallback {

    private static final ExtensionContext.Namespace NAMESPACE =
        ExtensionContext.Namespace.create(HeavyButReusableExtension.class);

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
        throws ParameterResolutionException
    {
        return HeavyButReusable.class.equals(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
        throws ParameterResolutionException
    {
        final String heavyNameMethod = AnnotationSupport
            .findAnnotation(extensionContext.getRequiredTestMethod(), HeavyButReusableName.class)
            .map(HeavyButReusableName::name)
            .orElse(null);

        final String heavyNameClass = AnnotationSupport
            .findAnnotation(extensionContext.getRequiredTestClass(), HeavyButReusableName.class)
            .map(HeavyButReusableName::name)
            .orElse(null);

        final String heavyName = firstNonNull(heavyNameMethod, heavyNameClass);
        validState(nonNull(heavyName), "HeavyButReusable must be specified - use annotation @HeavyButReusableName");

        final ExtensionContext rootContext = extensionContext.getRoot();
        final ExtensionContext.Store store = rootContext.getStore(NAMESPACE);

        return store.getOrComputeIfAbsent(heavyName, (String s) -> new HeavyButReusable(heavyName));
    }

    @Override
    public void afterEach(ExtensionContext context) {
        HeavyButReusable.logInstantiations();
    }

}
