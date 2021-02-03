package com.niu.activiti;

import com.niu.activiti.event.CustomEventListener;
import com.sun.org.apache.xpath.internal.operations.String;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.delegate.event.impl.ActivitiEntityEventImpl;
import org.activiti.engine.delegate.event.impl.ActivitiEventImpl;
import org.activiti.engine.event.EventLogEntry;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 事件监听器配置测试
 *
 * @author [nza]
 * @version 1.0 [2021/02/03 11:36]
 * @createTime [2021/02/03 11:36]
 */
public class ConfigEventListenerTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(ConfigEventListenerTest.class);

    public ConfigEventListenerTest() {
        super("activiti.cfg.listener.xml");
    }

    @Test
    public void testEventLog() {

        // 启动流程
        ProcessDefinition processDefinition = deploy("my_process_listener.bpmn20.xml");
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceById(processDefinition.getId());
        log.info("启动流程 [{}]", processInstance.getProcessDefinitionKey());

        // 完成任务
        Task task = processEngine.getTaskService().createTaskQuery().singleResult();
        processEngine.getTaskService()
                .complete(task.getId());

        // 通过程序注册事件监听器
        processEngine.getRuntimeService()
                .addEventListener(new CustomEventListener());

        // 手动触发自定义事件
        processEngine.getRuntimeService()
                .dispatchEvent(new ActivitiEntityEventImpl("Hello", ActivitiEventType.CUSTOM));

    }
}
