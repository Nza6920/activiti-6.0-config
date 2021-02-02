package com.niu.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.logging.LogMDC;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MDC配置测试
 *
 * @author [nza]
 * @version 1.0 [2021/02/02 15:24]
 * @createTime [2021/02/02 15:24]
 */
public class ConfigMDCTest {

    private static final Logger log = LoggerFactory.getLogger(ConfigMDCTest.class);

    /**
     * 流程引擎
     */
    private ProcessEngine processEngine;

    /**
     * 初始化
     */
    @BeforeEach
    public void setUp() {
        if (processEngine == null) {
            // 获取默认的流程引擎对象 根据资源文件加载
            ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                    .createProcessEngineConfigurationFromResource("activiti.cfg.mdc.xml");
            LogMDC.setMDCEnabled(true);

            ProcessEngine processEngine = configuration.buildProcessEngine();
            Assert.assertNotNull(processEngine);

            this.processEngine = processEngine;
        }
    }

    @AfterEach
    public void tearDown() {
        if (processEngine != null) {
            processEngine.close();
        }
    }

    @Test
    public void testMdcConfig() {

        ProcessDefinition processDefinition = deploy("my_process_mdc_error.bpmn20.xml");

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        log.info("启动流程 [{}]", processInstance.getProcessDefinitionKey());

        // 获取任务
        Task task = processEngine.getTaskService().createTaskQuery().singleResult();
        Assert.assertEquals("hello", task.getName());

        // 完成任务
        processEngine.getTaskService().complete(task.getId());
    }

    /**
     * 部署流程定义文件
     */
    private ProcessDefinition deploy(String filename) {
        // 部署流程文件
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource(filename);
        Deployment deploy = deploymentBuilder.deploy();

        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .deploymentId(deploy.getId())
                .singleResult();

        return processDefinition;
    }
}
