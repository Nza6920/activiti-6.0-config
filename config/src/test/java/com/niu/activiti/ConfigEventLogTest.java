package com.niu.activiti;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.event.EventLogEntry;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 事件日志配置测试
 *
 * @author [nza]
 * @version 1.0 [2021/02/03 11:36]
 * @createTime [2021/02/03 11:36]
 */
public class ConfigEventLogTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(ConfigEventLogTest.class);

    public ConfigEventLogTest() {
        super("activiti.cfg.event.xml");
    }

    @Test
    public void testEventLog() {

        // 启动流程
        ProcessDefinition processDefinition = deploy("my_process_his.bpmn20.xml");
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceById(processDefinition.getId());
        log.info("启动流程 [{}]", processInstance.getProcessDefinitionKey());

        // 完成任务
        Task task = processEngine.getTaskService().createTaskQuery().singleResult();
        processEngine.getTaskService()
                .complete(task.getId());

        List<EventLogEntry> eventLogEntries = processEngine.getManagementService()
                .getEventLogEntriesByProcessInstanceId(processInstance.getProcessInstanceId());
        for (EventLogEntry eventLogEntry : eventLogEntries) {
            log.info("事件类型: {}, 事件数据: {}", eventLogEntry.getType(), new String(eventLogEntry.getData()));
        }
        log.info("事件数量: {}", eventLogEntries.size());
    }
}
