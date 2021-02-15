package com.niu.activiti.db;

import com.google.common.collect.Maps;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * 历史记录相关表测试
 *
 * @author [nza]
 * @version 1.0 2021/2/15
 * @createTime 23:22
 */
public class DbHistoryTest extends BaseTest {

    public DbHistoryTest() {
        super("activiti.cfg.db_mysql.xml");
    }

    @Test
    public void testHistory() {

        ProcessDefinition processDefinition = deploy("my_process.bpmn20.xml");

        RuntimeService runtimeService = processEngine.getRuntimeService();

        // 启动流程
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("k1", "v1");
        variables.put("k2", "v2");
        variables.put("k3", "v3");
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey(processDefinition.getKey(), variables);

        // 修改流程变量
        runtimeService.setVariable(processInstance.getId(), "k1", "vv1");

        TaskService taskService = processEngine.getTaskService();

        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();
        // 指定当前代办的所属人
        taskService.setOwner(task.getId(), "owner1");

        // 添加一个附件
        taskService.createAttachment("url",
                task.getId(),
                processInstance.getId(),
                "附件1",
                "我是一个附件",
                "https://github.com/");

        // 添加评论
        taskService.addComment(task.getId(), processInstance.getId(), "评论1");

        // 提交代办
        Map<String, String> properties = Maps.newHashMap();
        properties.put("fk1", "fk2");

        // 覆盖之前的流程变量
        properties.put("k3", "vvv3");

        // 通过 FormService 提交的代办记录会存储在 his_details 表中
        // taskService 提交的不会保存 detail 记录
        processEngine.getFormService()
                .submitTaskFormData(task.getId(), properties);

    }
}
