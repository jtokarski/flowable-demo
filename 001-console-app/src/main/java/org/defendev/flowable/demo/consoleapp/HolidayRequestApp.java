package org.defendev.flowable.demo.consoleapp;

import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.lang.String.format;



/*
 * Implementation of Tutorial: https://www.flowable.com/open-source/docs/bpmn/ch02-GettingStarted
 *
 */
public class HolidayRequestApp {

    public static void main(String[] args) throws IOException {

        /*
         * The first thing we need to do is to instantiate a ProcessEngine instance. This is
         * a thread-safe object that you typically have to instantiate only once in an application.
         * A ProcessEngine is created from a ProcessEngineConfiguration instance,
         * which allows you to configure and tweak the settings for the process engine.
         * Often, the ProcessEngineConfiguration is created using a configuration XML file,
         * but (as we do here) you can also create it programmatically. The minimum configuration
         * a ProcessEngineConfiguration needs is a JDBC connection to a database:
         *
         */
        final ProcessEngineConfiguration config = new StandaloneProcessEngineConfiguration()
            .setJdbcUrl("jdbc:h2:mem:flowable;DB_CLOSE_DELAY=-1")
            .setJdbcUsername("sa")
            .setJdbcPassword("")
            .setJdbcDriver("org.h2.Driver")
            .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        final ProcessEngine processEngine = config.buildProcessEngine();

        final RepositoryService repositoryService = processEngine.getRepositoryService();
        final Deployment deployment = repositoryService.createDeployment()
            .addClasspathResource("holiday-request.bpmn20.xml")
            .deploy();

        final ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
            .deploymentId(deployment.getId())
            .singleResult();
        System.out.println(format("Found process definition: %s", processDefinition.getName()));



        final Scanner scanner = new Scanner(System.in);

        System.out.println("Who are you?");
        final String employee = scanner.nextLine();

        System.out.println("How many holidays do you want to request?");
        final int nrOfHolidays = Integer.parseInt(scanner.nextLine());

        System.out.println("Why do you need them?");
        final String description = scanner.nextLine();

        System.out.println(format("\n -------- %s --- %s --- %s ------- ", employee, nrOfHolidays, description));

        final RuntimeService runtimeService = processEngine.getRuntimeService();


        //
        // Starting a process instance
        //
        final Map<String, Object> processVariables = Map.of(
            "employee", employee,
            "nrOfHolidays", nrOfHolidays,
            "description", description
        );
        final ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holidayRequest",
            processVariables);


        //
        // Querying and completing tasks
        //
        final TaskService taskService = processEngine.getTaskService();
        final List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();
        System.out.println(format("\nYou have %d tasks:", tasks.size()));
        for (int i=0; i<tasks.size(); i+=1) {
            System.out.println(format("%d) %s", i+1, tasks.get(i).getName()));
        }
        System.out.println("Which task would you like to complete?");
        final int taskIndex = Integer.parseInt(scanner.nextLine());
        final Task task = tasks.get(taskIndex - 1);
        final Map<String, Object> processVariables2 = taskService.getVariables(task.getId());
        System.out.println(format("%s wants %d of holidays. Do you approve this?", processVariables2.get("employee"),
            processVariables2.get("nrOfHolidays")));

        final boolean approved = scanner.nextLine().toLowerCase().equals("y");
        final Map<String, Object> completionVariables = new HashMap<>();
        completionVariables.put("approved", approved);
        taskService.complete(task.getId(), completionVariables);

        //
        // Working with historical data
        //
        final HistoryService historyService = processEngine.getHistoryService();
        final List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery()
            .processInstanceId(processInstance.getId())
            .finished()
            .orderByHistoricActivityInstanceEndTime().asc()
            .list();

        for (final HistoricActivityInstance activityInstance : activityInstances) {
            System.out.println(
                format("%s took %d ", activityInstance.getActivityId(), activityInstance.getDurationInMillis())
            );
        }

        System.out.println("\nPress ENTER to continue...\n");
        scanner.nextLine();


        // shutdown H2 if server
    }

}
