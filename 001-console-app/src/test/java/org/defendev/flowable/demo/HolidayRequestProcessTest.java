package org.defendev.flowable.demo;

import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;



@ProcessEngineName(name = "holidayRequestAppEngine")
@ExtendWith(FlowableMultiStandaloneExtension.class)
public class HolidayRequestProcessTest {

    @Test
    public void shouldResolveParameterToProductionEngine(ProcessEngine processEngine) {
        assertThat(processEngine.getName()).isEqualTo("holiday0");
    }

    @ProcessEngineName(name = "emptyTestEngine")
    @Test
    public void shouldResolveParameterToTestEngine(ProcessEngine processEngine) {
        assertThat(processEngine.getName()).isEqualTo("empty0");
    }

    /*
     * The actual goal of this test is to explore how - from Flowable API - get information
     * on process state - at what stage of progress the process is, where are the tokens.
     *
     * What specific pieces of information I try to obtain:
     *  - is the process finished Yes/No?
     *  - if finished - what was the end event?
     *  - if not finished - on which activity is it waiting - get id of activity as in XML
     *  - if not finished - is there just one activity waiting or more?
     *
     * In Flowable API, an Execution is equivalent to BPMN concept of "token".
     * When a process is started, at least two executions are created:
     *  - one representing the ProcessInstance itself - kind of "black-box" view of whole process.
     *    Note that interface ProcessInstance extends Execution
     *  - one for current activity within the process
     *
     * In Flowable API actions on various objects are performed by passing the object to Flowable
     * services (TaskService, RuntimeService, ...) and not on the object itself. For example:
     *   - taskService.getVariables(taskId)
     *   - taskService.complete(taskId, completionVariables)
     *   - runtimeService.createExecutionQuery().processInstanceId(processInstanceId).list()
     *   - ...
     *
     * See:
     *   https://forum.flowable.org/t/what-is-the-relationship-between-process-instance-and-execution/9701
     *   org.flowable.engine.runtime.ProcessInstance
     *   org.flowable.engine.runtime.Execution
     *
     */
    @Deployment(resources = {"holiday-request.bpmn20.xml"})
    @Test
    public void shouldLeaveProcessIncomplete(RepositoryService repositoryService, RuntimeService runtimeService,
                                             TaskService taskService, HistoryService historyService) {

        final List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
            .active()
            .list();
        assertThat(processDefinitions).isNotNull().hasSize(1);
        assertThat(processDefinitions.getFirst().getKey()).isEqualTo("holidayRequest");
        assertThat(processDefinitions.getFirst().getName()).isEqualTo("Holiday Request");

        final Map<String, Object> variablesForProcessStart = Map.of(
            "employee", "Vincent van Gogh",
            "nrOfHolidays", 11,
            "description", "My leave is due to a family matter that requires my attention."
        );
        final ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holidayRequest",
            variablesForProcessStart);
        assertThat(processInstance).isNotNull();
        final String processInstanceId = processInstance.getId();

        final List<Task> managerTasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();
        assertThat(managerTasks).isNotNull().hasSize(1);
        final Task managerTask = managerTasks.getFirst();

        final Map<String, Object> variablesByUserTask = taskService.getVariables(managerTask.getId());
        assertThat(variablesByUserTask).isNotNull();
        assertThat(variablesByUserTask.get("employee")).isEqualTo("Vincent van Gogh");
        assertThat(variablesByUserTask.get("nrOfHolidays")).isEqualTo(11);
        assertThat(variablesByUserTask.get("description"))
            .isEqualTo("My leave is due to a family matter that requires my attention.");

        final Map<String, Object> completionVariables = new HashMap<>();
        completionVariables.put("approved", true);

        taskService.complete(managerTask.getId(), completionVariables);

        final List<Execution> executionsIntermediate = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).list();
        assertThat(executionsIntermediate).hasSize(2);
        final Execution execution0 = executionsIntermediate.get(0);
        final Execution execution1 = executionsIntermediate.get(1);

        assertThat(execution0.getName()).isNull();
        assertThat(execution0.getReferenceType()).isNull();
        assertThat(execution0.getActivityId()).isNull();
        assertThat(execution0.isEnded()).isFalse();

        assertThat(execution1.getName()).isNull();
        assertThat(execution1.getReferenceType()).isNull();
        assertThat(execution1.getActivityId()).isEqualTo("holidayApprovedTask");
        assertThat(execution1.isEnded()).isFalse();

        final List<Task> finalizingTasks = taskService.createTaskQuery().taskDefinitionKey("holidayApprovedTask").list();
        assertThat(finalizingTasks).hasSize(1);
        final Task finalizingTask = finalizingTasks.getFirst();

        taskService.complete(finalizingTask.getId());

        final List<Execution> executionsFinal = runtimeService.createExecutionQuery()
            .processInstanceId(processInstanceId)
            .list();

        //
        // Finished process has 0 executions!
        //
        assertThat(executionsFinal).hasSize(0);

        final List<HistoricProcessInstance> historicProcessInstances = historyService
            .createHistoricProcessInstanceQuery()
            .list();

        assertThat(historicProcessInstances).hasSize(1);

        final String endActivityId = historicProcessInstances.getFirst().getEndActivityId();

        //
        // Process was finished by reaching "approveEnd" end event
        //
        assertThat(endActivityId).isEqualTo("approveEnd");
    }

}
