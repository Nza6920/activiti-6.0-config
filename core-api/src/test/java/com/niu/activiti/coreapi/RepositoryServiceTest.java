package com.niu.activiti.coreapi;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLink;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 流程存储服务测试
 *
 * @author [nza]
 * @version 1.0 [2021/02/04 13:13]
 * @createTime [2021/02/04 13:13]
 */
public class RepositoryServiceTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(RepositoryServiceTest.class);

    public RepositoryServiceTest() {
        super("activiti.cfg.api.xml");
    }

    /**
     * 测试部署
     */
    @Test
    public void testRepositoryDeploy() {

        RepositoryService repositoryService = processEngine.getRepositoryService();

        // 部署流程定义文件
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        deploymentBuilder.name("测试资源部署")
                .addClasspathResource("my_process.bpmn20.xml")
                .addClasspathResource("my_process2.bpmn20.xml");
        Deployment deploy = deploymentBuilder.deploy();

        log.info("部署完成: {}", deploy);

        DeploymentBuilder deploymentBuilder2 = repositoryService.createDeployment();
        deploymentBuilder2.name("测试资源部署2")
                .addClasspathResource("my_process.bpmn20.xml")
                .addClasspathResource("my_process2.bpmn20.xml");
        Deployment deploy2 = deploymentBuilder2.deploy();

        log.info("部署完成2: {}", deploy2);

        // 查询流程部署
        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        List<Deployment> deployments = deploymentQuery
                .orderByDeploymenTime()
                .asc()
                .listPage(0, 100);

        log.info("流程部署数量: {}", deployments.size());
        for (Deployment deployment : deployments) {
            // 查询流程定义文件
            List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                    .deploymentId(deployment.getId())
                    .listPage(0, 100);
            for (ProcessDefinition processDefinition : processDefinitions) {
                log.info("processDefinition: {} \t 版本: {} \t ID: {} \t key: {}",
                        processDefinition,
                        processDefinition.getVersion(),
                        processDefinition.getId(),
                        processDefinition.getKey());
            }
            log.info("流程定义文件数量: {}", processDefinitions.size());
        }
    }

    /**
     * 测试 挂起
     */
    @Test
    public void testRepositorySuspend() {
        RepositoryService repositoryService = processEngine.getRepositoryService();

        // 部署流程定义文件
        ProcessDefinition processDefinition = deploy("my_process.bpmn20.xml");

        // 挂起流程定义
        repositoryService.suspendProcessDefinitionById(processDefinition.getId());

        // 启动流程
        log.info("开始启动");
        processEngine.getRuntimeService().startProcessInstanceById(processDefinition.getId());
        log.info("启动成功");

        repositoryService.activateProcessDefinitionById(processDefinition.getId());
    }

    /**
     * 设置用户、用户组
     */
    @Test
    public void testCandidateStart() {
        RepositoryService repositoryService = processEngine.getRepositoryService();

        // 部署流程定义文件
        ProcessDefinition processDefinition = deploy("my_process.bpmn20.xml");

        // 设置用户、用户组
        repositoryService.addCandidateStarterUser(processDefinition.getId(), "nza");
        repositoryService.addCandidateStarterGroup(processDefinition.getId(), "nzaGroup");

        // 查询用户关系
        List<IdentityLink> identityLinks = repositoryService.getIdentityLinksForProcessDefinition(processDefinition.getId());
        for (IdentityLink identityLink : identityLinks) {
            log.info("identityLink: {}", identityLink);
        }

        // 删除用户、用户组
        repositoryService.deleteCandidateStarterUser(processDefinition.getId(), "nza");
        repositoryService.deleteCandidateStarterGroup(processDefinition.getId(), "nzaGroup");
    }
}
