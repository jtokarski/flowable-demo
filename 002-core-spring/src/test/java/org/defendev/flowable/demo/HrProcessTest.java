package org.defendev.flowable.demo;

import org.defendev.flowable.demo.spring.DmnEngineName;
import org.defendev.flowable.demo.spring.FlowableCoreSpringConfig;
import org.defendev.flowable.demo.spring.FlowableMultiDmnSpringExtension;
import org.defendev.flowable.demo.spring.FlowableMultiSpringExtension;
import org.defendev.flowable.demo.spring.ProcessEngineName;
import org.flowable.dmn.api.DmnDecisionService;
import org.flowable.dmn.api.DmnRepositoryService;
import org.flowable.dmn.api.ExecuteDecisionBuilder;
import org.flowable.dmn.engine.DmnEngine;
import org.flowable.dmn.engine.test.DmnDeploymentAnnotation;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;



@DmnEngineName(name = "hrDmnEngine")
@ProcessEngineName(name = "hrProcessEngine")
@ExtendWith({ SpringExtension.class, FlowableMultiSpringExtension.class, FlowableMultiDmnSpringExtension.class })
@ContextConfiguration(classes = { FlowableCoreSpringConfig.class })
public class HrProcessTest {

    @DmnDeploymentAnnotation(resources = { "decisions/dmn-meanOfTransportSelection.dmn" })
    @Deployment(resources = {
        "processes/absence-approval.bpmn20.xml",
        "processes/go-out.bpmn20.xml"
    })
    @Test
    public void shouldEvaluateDecisionAsPartOfProcess(ProcessEngine hrProcessEngine, RuntimeService runtimeService,
                                                      DmnEngine hrDmnEngine,
                                                      DmnRepositoryService dmnRepositoryService) {
        // given
        final Map<String, Object> variablesForProcessStart = Map.of(
            "distanceKilometers", 3,
            "celsiusDegreesOutside", 18
        );

        // when
        final ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("go-out",
            variablesForProcessStart);
        final String processInstanceId = processInstance.getId();
        final List<Execution> executions = runtimeService.createExecutionQuery()
            .processInstanceId(processInstanceId)
            .list();
        final Map<String, Object> variablesAfterDecision = runtimeService.getVariables(executions.get(0).getId());

        // then
        assertThat(variablesAfterDecision).contains(entry("meanOfTransport", "BY_FOOT"));
    }

    @DmnDeploymentAnnotation(resources = { "decisions/dmn-meanOfTransportSelection.dmn" })
    @Test
    public void shouldEvaluateDecisionOutsideProcess(DmnEngine hrDmnEngine, DmnDecisionService dmnDecisionService) {
        // given
        final Map<String, Object> decisionInputVariables = Map.of(
            "distanceKilometers", 29,
            "celsiusDegreesOutside", 42
        );
        final ExecuteDecisionBuilder executeDecisionBuilder = dmnDecisionService.createExecuteDecisionBuilder()
            .decisionKey("meanOfTransportSelection")
            .variables(decisionInputVariables);

        // when
        final List<Map<String, Object>> decisionOutputVariables = dmnDecisionService
            .executeDecision(executeDecisionBuilder);

        // then
        assertThat(decisionOutputVariables).containsExactly(Map.of("meanOfTransport", "CAR"));
    }

}
