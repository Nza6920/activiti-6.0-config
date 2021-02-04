package com.niu.activiti.coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 流程运行控制服务测试
 *
 * @author [nza]
 * @version 1.0 [2021/02/04 13:13]
 * @createTime [2021/02/04 13:13]
 */
public class RuntimeServiceTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(RuntimeServiceTest.class);

    public RuntimeServiceTest() {
        super("activiti.cfg.api.xml");
    }

    /**
     * 测试流程启动
     */
    @Test
    public void testStartProcess() {

        RuntimeService runtimeService = processEngine.getRuntimeService();

        // 部署流程
        ProcessDefinition processDefinition = deploy("my_process.bpmn20.xml");

        // 启动流程
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("k1", "v1");

        // 通过 key 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my_process", variables);

        // 通过 id 启动流程
//        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), variables);

        // 通过 builder 启动流程
//        ProcessInstance processInstance = runtimeService.createProcessInstanceBuilder()
//                .businessKey("niuniuniu")
//                .processDefinitionKey("my_process")
//                .variables(variables)
//                .start();

        log.info("processInstance: {}", processInstance);

    }

    /**
     * 测试流程变量
     */
    @Test
    public void testVariables() {

        RuntimeService runtimeService = processEngine.getRuntimeService();

        // 部署流程
        ProcessDefinition processDefinition = deploy("my_process.bpmn20.xml");

        // 启动流程
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("k1", "v1");
        variables.put("k2", "v2");

        // 通过 key 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey(), variables);

        // 修改流程变量
        variables.put("k2", "v22");
        variables.put("k3", "v3");
        runtimeService.setVariables(processInstance.getId(), variables);

        // 获取流程变量
        Map<String, Object> queryVariables = runtimeService.getVariables(processInstance.getId());
        log.info("queryVariables = {}", queryVariables);
    }

    /**
     * 测试流程实例查询
     */
    @Test
    public void testQueryProcessInstance() {

        RuntimeService runtimeService = processEngine.getRuntimeService();

        // 部署流程
        ProcessDefinition processDefinition = deploy("my_process.bpmn20.xml");

        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey());

        ProcessInstance queryProcessInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();

        log.info("queryProcessInstance: {}", queryProcessInstance);
    }

    /**
     * 测试查询流程执行流
     */
    @Test
    public void testQueryExecution() {
        RuntimeService runtimeService = processEngine.getRuntimeService();

        // 部署流程
        ProcessDefinition processDefinition = deploy("my_process.bpmn20.xml");

        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey());
        log.info("processInstance: {}", processInstance);

        List<Execution> executions = runtimeService.createExecutionQuery()
                .listPage(0, 100);
        for (Execution execution : executions) {
            log.info("execution: {}", execution);
        }
    }

    /**
     * trigger 触发 receive task
     */
    @Test
    public void testTrigger() {
        RuntimeService runtimeService = processEngine.getRuntimeService();

        // 部署流程
        ProcessDefinition processDefinition = deploy("trigger_process.bpmn20.xml");

        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey());
        log.info("processInstance: {}", processInstance);


        // 查询执行流
        Execution execution = runtimeService.createExecutionQuery()
                // 定义的 taskId
                .activityId("someTask")
                .singleResult();

        // 发送触发消息, 推动流程执行
        log.info("execution = {}", execution);
        runtimeService.trigger(execution.getId());

        execution = runtimeService.createExecutionQuery()
                .activityId("someTask")
                .singleResult();
        log.info("execution = {}", execution);
    }

    /**
     * 测试触发信号（全局可触发）
     */
    @Test
    public void testSignal() {
        RuntimeService runtimeService = processEngine.getRuntimeService();

        // 部署流程
        ProcessDefinition processDefinition = deploy("signal_process.bpmn20.xml");

        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey());
        log.info("processInstance: {}", processInstance);


        Execution execution = runtimeService.createExecutionQuery()
                // 信号名称
                .signalEventSubscriptionName("my-signal")
                .singleResult();
        log.info("execution: {}", execution);

        // 触发信号
        runtimeService.signalEventReceived("my-signal");

        execution = runtimeService.createExecutionQuery()
                // 信号名称
                .signalEventSubscriptionName("my-signal")
                .singleResult();
        log.info("execution: {}", execution);
    }

    /**
     * 测试触发消息（只针对当前执行流实例）
     */
    @Test
    public void testMessage() {
        RuntimeService runtimeService = processEngine.getRuntimeService();

        // 部署流程
        ProcessDefinition processDefinition = deploy("message_process.bpmn20.xml");

        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinition.getKey());
        log.info("processInstance: {}", processInstance);

        Execution execution = runtimeService.createExecutionQuery()
                // 消息名称
                .messageEventSubscriptionName("my-message")
                .singleResult();
        log.info("execution: {}", execution);

        // 触发消息
        runtimeService.messageEventReceived("my-message", execution.getId());

        execution = runtimeService.createExecutionQuery()
                // 消息名称
                .messageEventSubscriptionName("my-message")
                .singleResult();
        log.info("execution: {}", execution);
    }

    /**
     * 测试触发消息启动流程
     */
    @Test
    public void testMessageStart() {
        RuntimeService runtimeService = processEngine.getRuntimeService();

        // 部署流程
        ProcessDefinition processDefinition = deploy("message_start_process.bpmn20.xml");

        // message 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("my-message");
        log.info("processInstance: {}", processInstance);
    }
}
