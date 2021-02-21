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
 * 脚本任务
 *
 * @author [nza]
 * @version 1.0 2021/2/18
 * @createTime 18:45
 */
public class SubProcessTest extends BaseTest {

    private final Logger log = LoggerFactory.getLogger(SubProcessTest.class);

    public SubProcessTest() {
        super("activiti.cfg.db.xml");
    }

    /**
     * 测试 子流程
     * 无法获取父流程的本地变量
     */
    @Test
    public void testSubProcess() {

        ProcessDefinition processDefinition = deploy("my_process_subprocess.bpmn20.xml");
        log.info("部署流程定义文件: {}", processDefinition.getKey());

        log.info("启动流程: {}", processDefinition.getKey());
        Map<String, Object> variables = Maps.newHashMapWithExpectedSize(1);
        variables.put("errorFlag", true);
        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey(), variables);

        List<Task> tasks = processEngine.getTaskService()
                .createTaskQuery()
                .processInstanceId(processInstance.getId())
                .listPage(0, 100);
        log.info("当前任务数量: {}", tasks.size());
        for (Task task : tasks) {
            log.info("当前任务: {}", task.getName());
        }

        Map<String, Object> processVariables = processEngine.getRuntimeService().getVariables(processInstance.getId());
        log.info("processVariables: {}", processVariables);
    }

    /**
     * 测试 事件子流程
     */
    @Test
    public void testEventSubProcess() {

        ProcessDefinition processDefinition = deploy("my_process_event_subprocess.bpmn20.xml");
        log.info("部署流程定义文件: {}", processDefinition.getKey());

        log.info("启动流程: {}", processDefinition.getKey());
        Map<String, Object> variables = Maps.newHashMapWithExpectedSize(1);
        variables.put("errorFlag", true);
        variables.put("k1", "v1");
        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey(), variables);

        List<Task> tasks = processEngine.getTaskService()
                .createTaskQuery()
                .processInstanceId(processInstance.getId())
                .listPage(0, 100);
        log.info("当前任务数量: {}", tasks.size());
        for (Task task : tasks) {
            log.info("当前任务: {}", task.getName());
        }

        Map<String, Object> processVariables = processEngine.getRuntimeService().getVariables(processInstance.getId());
        log.info("processVariables: {}", processVariables);
    }

    /**
     * 测试 调用式子流程
     */
    @Test
    public void testCallActivitySubProcess() {

        // 部署子流程
        ProcessDefinition processSubDefinition = deploy("my_process_invoke_sub_subprocess.bpmn20.xml");

        // 部署父流程
        ProcessDefinition processDefinition = deploy("my_process_invoke_main_subprocess.bpmn20.xml");
        log.info("部署流程定义文件: {}", processDefinition.getKey());

        log.info("启动流程: {}", processDefinition.getKey());
        Map<String, Object> variables = Maps.newHashMapWithExpectedSize(1);
        variables.put("errorFlag", true);
        variables.put("k3", "v3");
        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey(), variables);

        List<Task> tasks = processEngine.getTaskService()
                .createTaskQuery()
                .processInstanceId(processInstance.getId())
                .listPage(0, 100);
        log.info("当前任务数量: {}", tasks.size());
        for (Task task : tasks) {
            log.info("当前任务: {}", task.getName());
        }

        Map<String, Object> processVariables = processEngine.getRuntimeService().getVariables(processInstance.getId());
        log.info("processVariables: {}", processVariables);
    }
}
