package org.defendev.flowable.demo.spring;

import org.flowable.dmn.engine.DmnEngine;
import org.flowable.dmn.engine.test.FlowableDmnExtension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;
import static org.apache.commons.lang3.Validate.validState;


/*
 *
 * It's disputable whether Flowable JUnit extensions are really useful. However, those to features
 * are convincing enough:
 *   - test method parameter resolution for engines (DmnEngine, ProcessEngine) as well as their
 *     services (RuntimeService, DmnRepositoryService, etc.)
 *   - @Deployment, @DmnDeploymentAnnotation annotations to limit test setup code
 *
 * Use of Flowable JUnit extensions with SpringExtension raises a concern about potential
 * competing ParameterResolver(s). But it's fair to say that this is addressed by SpringExtension.supportsParameter()
 * strategy, that is SpringExtension will only attempt to provide @Autowired parameters, and kindly leaves place
 * for other ParameterResolver(s).
 * In the light of this concern my custom FlowableMultiDmnSpringExtension works exactly the same
 * as FlowableDmnSpringExtension.
 *
 */
public class FlowableMultiDmnSpringExtension extends FlowableDmnExtension {

    private static final Map<String, ExtensionContext.Namespace> NAMESPACES = new ConcurrentHashMap<>();

    private String resolveDmnEngineName(ExtensionContext context) {
        final String dmnEngineNameMethod = AnnotationSupport
            .findAnnotation(context.getRequiredTestMethod(), DmnEngineName.class)
            .map(DmnEngineName::name)
            .orElse(null);
        final String dmnEngineNameClass = AnnotationSupport
            .findAnnotation(context.getRequiredTestClass(), DmnEngineName.class)
            .map(DmnEngineName::name)
            .orElse(null);

        final String dmnEngineName = firstNonNull(dmnEngineNameMethod, dmnEngineNameClass);
        validState(nonNull(dmnEngineName), "DmnEngine must be specified - use annotation @DmnEngineName");
        return dmnEngineName;
    }

    @Override
    protected DmnEngine createDmnEngine(ExtensionContext context) {
        final String engineName = resolveDmnEngineName(context);
        return SpringExtension.getApplicationContext(context).getBean(engineName, DmnEngine.class);
    }

    @Override
    protected ExtensionContext.Store getStore(ExtensionContext context) {
        final String engineName = resolveDmnEngineName(context);
        final ExtensionContext.Namespace namespace = NAMESPACES.computeIfAbsent(engineName,
            name -> ExtensionContext.Namespace.create(name));
        return context.getRoot().getStore(namespace);
    }

}
