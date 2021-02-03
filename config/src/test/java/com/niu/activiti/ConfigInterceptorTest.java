package com.niu.activiti;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 拦截器配置测试
 *
 * @author [nza]
 * @version 1.0 [2021/02/03 15:35]
 * @createTime [2021/02/03 15:35]
 */
public class ConfigInterceptorTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(ConfigInterceptorTest.class);

    public ConfigInterceptorTest() {
        super("activiti.cfg.interceptor.xml");
    }

    @Test
    public void testInterceptor() {

        ProcessDefinition processDefinition = deploy("my_process_interceptor.bpmn20.xml");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        log.info("启动流程 [{}]", processInstance.getProcessDefinitionKey());

        // 获取任务
        Task task = processEngine.getTaskService().createTaskQuery().singleResult();
        Assert.assertEquals("hello", task.getName());

        // 完成任务
        processEngine.getTaskService().complete(task.getId());
    }
}
