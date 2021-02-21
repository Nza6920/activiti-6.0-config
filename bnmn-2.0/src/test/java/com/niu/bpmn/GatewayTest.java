package com.niu.bpmn;

import com.google.common.collect.Maps;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 网关测试类
 *
 * @author [nza]
 * @version 1.0 2021/2/18
 * @createTime 18:45
 */
public class GatewayTest extends BaseTest {

    private final Logger log = LoggerFactory.getLogger(GatewayTest.class);

    public GatewayTest() {
        super("activiti.cfg.db.xml");
    }

    /**
     * 单一网关测试
     */
    @Test
    public void testExclusiveGateway() {

        ProcessDefinition processDefinition = deploy("my_process_exclusiveGateway.bpmn20.xml");
        log.info("部署流程定义文件: {}", processDefinition.getKey());

        log.info("启动流程: {}", processDefinition.getKey());
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("score", 70);
        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey(), variables);

        Task task = processEngine.getTaskService()
                .createTaskQuery()
                .singleResult();
        log.info("当前task: {}", task.getName());
    }

    /**
     * 并行网关测试
     */
    @Test
    public void testParallelGateway() {

        ProcessDefinition processDefinition = deploy("my_process_parallelGateway.bpmn20.xml");
        log.info("部署流程定义文件: {}", processDefinition.getKey());

        log.info("启动流程: {}", processDefinition.getKey());
        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey());

        List<Task> tasks = processEngine.getTaskService()
                .createTaskQuery()
                .processInstanceId(processInstance.getId())
                .listPage(0, 100);

        log.info("当前task数量: {}", tasks.size());
        for (Task task : tasks) {
            log.info("当前task: {}", task.getName());

            // 完成task
            processEngine.getTaskService()
                    .complete(task.getId());
        }

        Task completeTask = processEngine.getTaskService()
                .createTaskQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();
        log.info("当前task: {}", completeTask.getName());
    }

    /**
     * 包容性网关测试
     */
    @Test
    public void testInclusiveGateway() {

        ProcessDefinition processDefinition = deploy("my_process_inclusiveGateway.bpmn20.xml");
        log.info("部署流程定义文件: {}", processDefinition.getKey());

        log.info("启动流程: {}", processDefinition.getKey());

        Map<String, Object> variables = Maps.newHashMap();
        variables.put("score", 95);
        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey(), variables);

        List<Task> tasks = processEngine.getTaskService()
                .createTaskQuery()
                .processInstanceId(processInstance.getId())
                .listPage(0, 100);

        log.info("当前task数量: {}", tasks.size());
        for (Task task : tasks) {
            log.info("当前task: {}", task.getName());
        }
    }

    /**
     * 事件网关测试
     */
    @Test
    public void testEventBaseGateway() throws InterruptedException {

        ProcessDefinition processDefinition = deploy("my_process_eventBaseGateway.bpmn20.xml");
        log.info("部署流程定义文件: {}", processDefinition.getKey());

        log.info("启动流程: {}", processDefinition.getKey());
        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey());

        // 触发信号
        processEngine.getRuntimeService()
                .signalEventReceived("alertSignal");

        // 睡眠10s
        Thread.sleep(1000 * 10);

        List<Task> tasks = processEngine.getTaskService()
                .createTaskQuery()
                .processInstanceId(processInstance.getId())
                .listPage(0, 100);
        log.info("当前task数量: {}", tasks.size());
        for (Task task : tasks) {
            log.info("当前task: {}", task.getName());
        }

        processInstance = processEngine.getRuntimeService()
                .createProcessInstanceQuery()
                .active()
                .processInstanceId(processInstance.getId())
                .singleResult();
        log.info("流程是否运行: {}", processInstance != null);
    }
}
