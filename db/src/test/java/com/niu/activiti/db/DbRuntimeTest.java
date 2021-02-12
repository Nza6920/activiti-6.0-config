package com.niu.activiti.db;

import com.google.common.collect.Maps;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 运行时流程相关数据表
 *
 * @author [nza]
 * @version 1.0 2021/2/12
 * @createTime 15:29
 */
public class DbRuntimeTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(DbRuntimeTest.class);

    public DbRuntimeTest() {
        super("activiti.cfg.db_mysql.xml");
    }

    @Test
    public void testRuntime() {

        // 部署流程
        ProcessDefinition processDefinition = deploy("my_process_ru.bpmn20.xml");

        RuntimeService runtimeService = processEngine.getRuntimeService();

        // 执行流程定义文件
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("k1", "v1");
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), variables);
    }

    /**
     * 测试任务主人
     */
    @Test
    public void testSetOwner() {

        TaskService taskService = processEngine.getTaskService();

        Task task = taskService.createTaskQuery()
                .processDefinitionKey("my_process_ru")
                .singleResult();

        taskService.setOwner(task.getId(), "user1");
    }

    /**
     * 测试事件信息
     */
    @Test
    public void testMessage() {

        ProcessDefinition processDefinition = deploy("message_start_process.bpmn20.xml");

    }


    /**
     * 测试事件信息
     */
    @Test
    public void testMessageProcess() {

        ProcessDefinition processDefinition = deploy("message_process.bpmn20.xml");

        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey());
        log.info("processInstance: {}", processInstance);
    }

    /**
     * 测试Job
     */
    @Test
    public void testJob() throws InterruptedException {

        ProcessDefinition processDefinition = deploy("my_process_job.bpmn20.xml");

        // 查询定时任务
        List<Job> jobs = processEngine.getManagementService()
                .createTimerJobQuery()
                .listPage(0, 100);
        for (Job job : jobs) {
            log.info("定时任务: {} \t 默认重试次数: {}", job, job.getRetries());
        }
        log.info("定时任务数量: {}", jobs.size());

        Thread.sleep(1000 * 60 * 10);
    }
}
