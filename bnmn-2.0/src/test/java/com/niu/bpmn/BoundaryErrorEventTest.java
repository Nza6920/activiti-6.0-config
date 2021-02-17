package com.niu.bpmn;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述
 *
 * @author [nza]
 * @version 1.0 2021/2/17
 * @createTime 11:24
 */
public class BoundaryErrorEventTest extends BaseTest {


    private static final Logger log = LoggerFactory.getLogger(TimerEventTest.class);

    public BoundaryErrorEventTest() {
        super("activiti.cfg.db.xml");
    }


    @Test
    public void testReviewSalesLeadProcess() {

        ProcessDefinition processDefinition = deploy("reviewSalesLead.bpmn20.xml");
        log.info("部署流程: {}", processDefinition.getKey());


        TaskService taskService = processEngine.getTaskService();

        // 会设置流程变量 ${initiator} 的值
        Authentication.setAuthenticatedUserId("kermit");

        Map<String, Object> variables = new HashMap<>();
        variables.put("details", "very interesting");
        variables.put("customerName", "Alfresco");
        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey(), variables);
        log.info("启动流程: {}", processInstance.getProcessDefinitionKey());

        Task task = taskService.createTaskQuery()
                .taskAssignee("kermit")
                .singleResult();
        Assert.assertEquals("Provide new sales lead", task.getName());
        log.info("输出 task.getName = {}", task.getName());

        log.info("完成任务: {}", task.getName());
        taskService.complete(task.getId());

        Task ratingTask = taskService.createTaskQuery()
                .taskCandidateGroup("accountancy")
                .singleResult();
        Assert.assertEquals("Review customer rating", ratingTask.getName());
        log.info("输出 ratingTask.getName = {}", ratingTask.getName());

        Task profitabilityTask = taskService.createTaskQuery()
                .taskCandidateGroup("management")
                .singleResult();
        Assert.assertEquals("Review profitability", profitabilityTask.getName());
        log.info("输出 profitabilityTask.getName = {}", profitabilityTask.getName());

        variables = new HashMap<>();
        variables.put("notEnoughInformation", true);
        taskService.complete(profitabilityTask.getId(), variables);

        log.info("进入异常捕获流程");
        Task provideDetailsTask = taskService.createTaskQuery()
                .taskAssignee("kermit")
                .singleResult();
        Assert.assertEquals("Provide additional details", provideDetailsTask.getName());
        log.info("输出 provideDetailsTask.getName = {}", provideDetailsTask.getName());
        taskService.complete(provideDetailsTask.getId());

        log.info("重新进入评级子流程");
        List<Task> reviewTasks = taskService.createTaskQuery()
                .orderByTaskName()
                .asc()
                .list();
        Assert.assertEquals("Review customer rating", reviewTasks.get(0).getName());
        Assert.assertEquals("Review profitability", reviewTasks.get(1).getName());
        log.info("输出 reviewTasks.get(0).getName = {}", reviewTasks.get(0).getName());
        log.info("输出 reviewTasks.get(1).getName = {}", reviewTasks.get(1).getName());

        taskService.complete(reviewTasks.get(0).getId());
            variables.put("notEnoughInformation", false);
        taskService.complete(reviewTasks.get(1).getId(), variables);
        Authentication.setAuthenticatedUserId(null);

        processInstance = processEngine.getRuntimeService()
                .createProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();

        log.info("流程执行完成: {}", processInstance == null);
    }
}
