package org.defendev.junit.demo.extensioncontext;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;
import static org.apache.commons.lang3.Validate.validState;



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
