package com.niu.bpmn;

import com.niu.bpmn.delegate.MyJavaBean;
import com.niu.bpmn.delegate.MyJavaDelegate;
import org.activiti.engine.ActivitiEngineAgenda;
import org.activiti.engine.ManagementService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.ibatis.mapping.MappedStatement;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.collections.Maps;

import java.util.List;
import java.util.Map;

/**
 * 测试服务任务
 *
 * @author [nza]
 * @version 1.0 2021/2/18
 * @createTime 22:07
 */
public class ServiceTaskTest extends BaseTest {

    private Logger log = LoggerFactory.getLogger(ServiceTaskTest.class);

    public ServiceTaskTest() {
        super("activiti.cfg.db.xml");
    }

    /**
     * 测试 javaDelegate
     * 会自动执行 serviceTask
     */
    @Test
    public void testDelegate() {

        ProcessDefinition processDefinition = deploy("my_process_service_task_delegate.bpmn20.xml");
        log.info("部署流程定义文件: {}", processDefinition.getKey());

        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey());
        log.info("启动流程: {}", processInstance.getProcessDefinitionKey());

        // 查询历史数据
        List<HistoricActivityInstance> historicActivityInstances = processEngine.getHistoryService()
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstance.getId())
                .orderByHistoricActivityInstanceEndTime()
                .asc()
                .listPage(0, 100);
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            log.info("historicActivityInstance: {}", historicActivityInstance);
        }
    }


    /**
     * 测试 ActivitiBehavior
     * 不会自动执行 serviceTask 需要手动触发
     */
    @Test
    public void testBehavior() {

        ProcessDefinition processDefinition = deploy("my_process_service_task_behavior.bpmn20.xml");
        log.info("部署流程定义文件: {}", processDefinition.getKey());

        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey());
        log.info("启动流程: {}", processInstance.getProcessDefinitionKey());

        // 获取当前执行的 serviceTask
        log.info("获取当前执行的 serviceTask");
        Execution myServiceTask = processEngine.getRuntimeService()
                .createExecutionQuery()
                .activityId("myServiceTask")
                .singleResult();
        log.info("myServiceTask: {}", myServiceTask);

        // 手动驱动 serviceTask 前进
        ManagementService managementService = processEngine.getManagementService();
        managementService.executeCommand(new Command<Object>() {
            @Override
            public Object execute(CommandContext commandContext) {

                ActivitiEngineAgenda agenda = commandContext.getAgenda();

                // 推动当前流程前进
                agenda.planTakeOutgoingSequenceFlowsOperation((ExecutionEntity) myServiceTask, false);
                return null;
            }
        });

        // 查询历史数据
        List<HistoricActivityInstance> historicActivityInstances = processEngine.getHistoryService()
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstance.getId())
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .listPage(0, 100);
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            log.info("historicActivityInstance: {}", historicActivityInstance);
        }
    }


    /**
     * 测试 属性注入
     */
    @Test
    public void testFieldInject() {

        ProcessDefinition processDefinition = deploy("my_process_service_task_field_inject.bpmn20.xml");
        log.info("部署流程定义文件: {}", processDefinition.getKey());


        Map<String, Object> variables = Maps.newHashMap();
        variables.put("desc", "hello");
        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey(), variables);
        log.info("启动流程: {}", processInstance.getProcessDefinitionKey());

    }

    /**
     * 测试 变量代理类
     */
    @Test
    public void testVariablesDelegate() {

        ProcessDefinition processDefinition = deploy("my_process_service_task_variables_delegate.bpmn20.xml");
        log.info("部署流程定义文件: {}", processDefinition.getKey());

        Map<String, Object> variables = Maps.newHashMap();
        variables.put("myJavaDelegate", new MyJavaDelegate());
        log.info("variables: {}", variables);

        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey(), variables);
        log.info("启动流程: {}", processInstance.getProcessDefinitionKey());
    }


    /**
     * 测试 调用方法
     */
    @Test
    public void testExpressInvoke() {

        ProcessDefinition processDefinition = deploy("my_process_service_task_invoke.bpmn20.xml");
        log.info("部署流程定义文件: {}", processDefinition.getKey());

        Map<String, Object> variables = Maps.newHashMap();
        variables.put("myJavaBean", new MyJavaBean("niu"));
        log.info("variables: {}", variables);

        log.info("启动流程: {}", processDefinition.getKey());
        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey(), variables);
    }


    /**
     * 测试 错误信号
     */
    @Test
    public void testHandleError() {

        ProcessDefinition processDefinition = deploy("my_process_service_task_error.bpmn20.xml");
        log.info("部署流程定义文件: {}", processDefinition.getKey());


        log.info("启动流程: {}", processDefinition.getKey());
        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey());
    }
}
