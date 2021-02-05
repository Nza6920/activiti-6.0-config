package com.niu.activiti.coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 历史管理服务
 *
 * @author [nza]
 * @version 1.0 [2021/02/05 11:13]
 * @createTime [2021/02/05 11:13]
 */
public class HistoryServiceTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(HistoryServiceTest.class);

    public HistoryServiceTest() {
        super("activiti.cfg.his.api.xml");
    }

    @Test
    public void testHistoryService() {

        HistoryService historyService = processEngine.getHistoryService();

        ProcessDefinition processDefinition = deploy("my_process_his.bpmn20.xml");

        Map<String, Object> variables = Maps.newHashMap();
        variables.put("normal", "normalV");
        variables.put("k1", "v1");
        variables.put("k2", "v2");

        Map<String, Object> transientVariables = Maps.newHashMap();
        transientVariables.put("transient", "瞬时变量");
        transientVariables.put("t1", "tv1");
        transientVariables.put("t2", "tv2");

        // 启动流程
        ProcessInstance processInstance = processEngine.getRuntimeService()
                .createProcessInstanceBuilder()
                .processDefinitionKey(processDefinition.getKey())
                // 普通变量会持久化
                .variables(variables)
                // 瞬时变量不会持久化
                .transientVariables(transientVariables)
                .start();

        // 修改流程变量
        log.info("修改流程变量");
        processEngine.getRuntimeService()
                .setVariable(processInstance.getId(), "k1", "vv1");

        Task task = processEngine.getTaskService()
                .createTaskQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();
        // 完成任务
        log.info("提交任务");
        Map<String, String> formProperties = Maps.newHashMap();
        formProperties.put("formK1", "formV1");
        // 覆盖流程变量
        formProperties.put("k2", "vv2");
        processEngine.getFormService()
                .submitTaskFormData(task.getId(), formProperties);

        // 查询历史流程实例对象
        log.info("查询历史流程实例对象");
        List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery()
                .listPage(0, 100);
        for (HistoricProcessInstance hisProcessInstance : historicProcessInstances) {
            log.info("hisProcessInstance = {}", hisProcessInstance);
        }

        // 查询历史节点实例对象
        log.info("查询历史节点实例对象");
        List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery()
                .listPage(0, 100);
        for (HistoricActivityInstance hisActivityInstance : activityInstances) {
            log.info("hisActivityInstance = {}", hisActivityInstance);
        }

        // 查询历史任务节点实例对象
        log.info("查询历史任务节点实例对象");
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .listPage(0, 100);
        for (HistoricTaskInstance hisTaskInstance : historicTaskInstances) {
            log.info("hisTaskInstance = {}", hisTaskInstance);
        }

        // 查询历史变量实例对象
        log.info("查询历史变量实例对象");
        List<HistoricVariableInstance> hisVariableInstances = historyService.createHistoricVariableInstanceQuery()
                .listPage(0, 100);
        for (HistoricVariableInstance hisVariableInstance : hisVariableInstances) {
            log.info("hisVariableInstance = {}", hisVariableInstance);
        }

        // 查询历史详情实例对象
        log.info("查询历史详情实例对象");
        List<HistoricDetail> historicDetails = historyService.createHistoricDetailQuery()
                .listPage(0, 100);
        for (HistoricDetail historicDetail : historicDetails) {
            log.info("historicDetail = {}", historicDetail);
        }

        // 查询历史日志实例对象
        log.info("查询历史日志实例对象");
        ProcessInstanceHistoryLog historyLog = historyService.createProcessInstanceHistoryLogQuery(processInstance.getId())
                .includeActivities()
                .includeComments()
                .includeFormProperties()
                .includeTasks()
                .includeVariables()
                .includeVariableUpdates()
                .singleResult();
        for (HistoricData historicDatum : historyLog.getHistoricData()) {
            log.info("historicDatum = {}", historicDatum);
        }

        // 删除历史流程实例
        log.info("删除历史流程实例");
        historyService.deleteHistoricProcessInstance(processInstance.getId());
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();
        log.info("流程是否删除： {}", historicProcessInstance == null);
    }
}
