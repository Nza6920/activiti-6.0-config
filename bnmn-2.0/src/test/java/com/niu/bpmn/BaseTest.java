package com.niu.bpmn;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.logging.LogMDC;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * 测试基类
 *
 * @author [nza]
 * @version 1.0 [2021/02/03 11:38]
 * @createTime [2021/02/03 11:38]
 */
public class BaseTest {

    /**
     * 流程引擎
     */
    protected ProcessEngine processEngine;

    /**
     * 配置文件
     */
    private final String resource;

    public BaseTest(String resource) {
        this.resource = resource;
    }

    /**
     * 初始化
     */
    @BeforeEach
    public void setUp() {
        if (processEngine == null) {
            // 获取默认的流程引擎对象 根据资源文件加载
            ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                    .createProcessEngineConfigurationFromResource(resource);
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

    /**
     * 部署流程定义文件
     */
    protected ProcessDefinition deploy(String filename) {
        // 部署流程文件
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource(filename);
        Deployment deploy = deploymentBuilder.deploy();

        return repositoryService
                .createProcessDefinitionQuery()
                .deploymentId(deploy.getId())
                .singleResult();
    }

    /**
     * 部署流程定义文件
     */
    protected ProcessDefinition deploy(String filename, String name, String category) {
        // 部署流程文件
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.addClasspathResource(filename)
                .category(category)
                .name(name);
        Deployment deploy = deploymentBuilder.deploy();

        return repositoryService
                .createProcessDefinitionQuery()
                .deploymentId(deploy.getId())
                .singleResult();
    }
}
