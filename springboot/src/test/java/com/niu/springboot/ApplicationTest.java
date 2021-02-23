package com.niu.springboot;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Zian.Niu
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class ApplicationTest {

    private static final Logger log = LoggerFactory.getLogger(ApplicationTest.class);

    @Autowired
    private RuntimeService runtimeService;

    @Test
    public void testActiviti() {

        log.info("测试 Spring 集成.......");

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my_process");
        log.info("启动流程: {}", processInstance.getProcessDefinitionKey());
    }
}
