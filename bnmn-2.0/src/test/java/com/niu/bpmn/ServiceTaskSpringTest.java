package com.niu.bpmn;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * 测试基于 Spring 容器的 serviceTask
 *
 * @author [nza]
 * @version 1.0 2021/2/18
 * @createTime 22:39
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:activiti.context.xml"})
public class ServiceTaskSpringTest {

    private Logger log = LoggerFactory.getLogger(ServiceTaskSpringTest.class);

    @Resource
    private ActivitiRule activitiRule;

    @Test
    public void testServiceTaskSpring() {

        activitiRule.getProcessEngine()
                .getRepositoryService()
                .createDeployment()
                .addClasspathResource("my_process_service_task_spring.bpmn20.xml")
                .deploy();

        ProcessInstance processInstance = activitiRule.getProcessEngine()
                .getRuntimeService()
                .startProcessInstanceByKey("my_process_service_task_delegate");
        log.info("启动流程: {}", processInstance.getProcessDefinitionKey());
    }
}
