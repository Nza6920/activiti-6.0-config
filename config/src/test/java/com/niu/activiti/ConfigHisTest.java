package com.niu.activiti;

import com.google.common.collect.Maps;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 历史记录配置测试
 *
 * @author [nza]
 * @version 1.0 [2021/02/03 09:50]
 * @createTime [2021/02/03 09:50]
 */
public class ConfigHisTest {

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
                    .createProcessEngineConfigurationFromResource("activiti.cfg.his.xml");

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
    public void testHisConfig() {

        // 部署流程文件
        ProcessDefinition processDefinition = deploy("my_process_his.bpmn20.xml");

        // todo：启动流程
        startProcess(processDefinition);

        // todo：修改变量
        editVariable();

        // todo：提交表单完成任务节点
        submitForm();

        // todo：输出历史内容(activiti)
        // 查询历史活动
        showHistoryActivity();

        // 查询历史流程变量
        showHisVariables();

        // 查询历史任务
        showHisTask();

        // todo：输出历史表单
        showHisForm();

        // todo：输出历史详情
        showHisDetails();
    }

    private void showHisTask() {
        log.info("查询历史任务");
        List<HistoricTaskInstance> historicTaskInstances = processEngine.getHistoryService()
                .createHistoricTaskInstanceQuery()
                .listPage(0, 100);
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
            log.info("historicTaskInstance: {}", historicTaskInstance);
        }
        log.info("historicTaskInstances size: {}", historicTaskInstances.size());
    }

    private void showHisDetails() {
        log.info("查询历史详情");
        List<HistoricDetail> historicDetails = processEngine.getHistoryService()
                .createHistoricDetailQuery()
                .listPage(0, 100);
        for (HistoricDetail historicDetail : historicDetails) {
            log.info("historicDetail: {}", toString(historicDetail));
        }
        log.info("historicDetails size: {}", historicDetails.size());
    }

    private void showHisForm() {
        log.info("查询历史表单属性详情");
        List<HistoricDetail> historicFormDetails = processEngine.getHistoryService()
                .createHistoricDetailQuery()
                .formProperties()
                .listPage(0, 100);
        for (HistoricDetail hisFormDetail : historicFormDetails) {
            log.info("hisFormDetail: {}", toString(hisFormDetail));
        }
        log.info("historicFormDetails size: {}", historicFormDetails.size());
    }

    private void showHisVariables() {
        log.info("查询历史流程变量");
        List<HistoricVariableInstance> historicVariableInstances = processEngine.getHistoryService()
                .createHistoricVariableInstanceQuery()
                .listPage(0, 100);
        for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
            log.info("historicVariableInstance: {}", historicVariableInstance);
        }
        log.info("historicVariableInstances size: {}", historicVariableInstances.size());
    }

    private void showHistoryActivity() {
        log.info("查询历史活动实例");
        List<HistoricActivityInstance> historicActivityInstances = processEngine.getHistoryService()
                .createHistoricActivityInstanceQuery()
                .listPage(0, 100);
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            log.info("historicActivityInstance: {}", historicActivityInstance);
        }
        log.info("historicActivityInstances size: {}", historicActivityInstances.size());
    }

    private void submitForm() {
        Task task = processEngine.getTaskService()
                .createTaskQuery()
                .singleResult();
        Map<String, String> properties = Maps.newHashMap();
        properties.put("formK1", "formV1");
        properties.put("formK2", "formV2");
        processEngine.getFormService()
                .submitTaskFormData(task.getId(), properties);
    }

    private void editVariable() {
        List<Execution> executions = processEngine
                .getRuntimeService()
                .createExecutionQuery()
                .listPage(0, 100);
        for (Execution execution : executions) {
            log.info("execution: {}", execution);
        }
        log.info("execution size: {}", executions.size());

        // 获取执行ID
        String executionId = executions.iterator().next().getId();
        log.info("修改流程变量");
        processEngine.getRuntimeService()
                .setVariable(executionId, "k1", "vv1");
    }

    private void startProcess(ProcessDefinition processDefinition) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("k1", "v1");
        params.put("k2", "v2");
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceById(processDefinition.getId(), params);
        log.info("启动流程 [{}]", processInstance.getProcessDefinitionKey());
    }

    static String toString(HistoricDetail historicDetail) {
        return ToStringBuilder.reflectionToString(historicDetail, ToStringStyle.SHORT_PREFIX_STYLE);
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
