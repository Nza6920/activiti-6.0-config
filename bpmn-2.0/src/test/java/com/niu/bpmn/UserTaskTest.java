package com.niu.bpmn;

import com.google.common.collect.Lists;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时边界事件测试
 *
 * @author [nza]
 * @version 1.0 2021/2/17
 * @createTime 2:02
 */
public class UserTaskTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(UserTaskTest.class);

    public UserTaskTest() {
        super("activiti.cfg.db.xml");
    }

    @Test
    public void testUserTask() {

        // 部署流程
        ProcessDefinition processDefinition = deploy("my_process_usertask.bpmn20.xml");
        log.info("部署流程: {}", processDefinition.getKey());

        // 启动流程
        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey());
        log.info("启动流程: {}", processInstance.getProcessDefinitionKey());


        TaskService taskService = processEngine.getTaskService();

        Task taskByUser1 = taskService.createTaskQuery()
                .taskCandidateUser("user1")
                .singleResult();
        log.info("task by user1: {}", taskByUser1);

        Task taskByUser2 = taskService.createTaskQuery()
                .taskCandidateUser("user2")
                .singleResult();
        log.info("task by user2: {}", taskByUser2);


        Task taskByGroup = taskService.createTaskQuery()
                .taskCandidateGroupIn(Lists.newArrayList("group1", "group2"))
                .singleResult();
        log.info("task by taskByGroup: {}", taskByGroup);

        // 指定任务处理人
        taskService.claim(taskByGroup.getId(), "user1");
        // 不推荐使用这种方式
        // taskService.setAssignee(taskByGroup.getId(), "user1");

        // 根据任务处理人查询
        Task taskByAssign = taskService.createTaskQuery()
                .taskAssignee("user1")
                .singleResult();
        log.info("task by assign: {}", taskByAssign);
    }

    @Test
    public void testUserTaskListener() {

        // 部署流程
        ProcessDefinition processDefinition = deploy("my_process_usertask_listener.bpmn20.xml");
        log.info("部署流程: {}", processDefinition.getKey());

        // 启动流程
        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey());
        log.info("启动流程: {}", processInstance.getProcessDefinitionKey());


        TaskService taskService = processEngine.getTaskService();

        Task taskByUser1 = taskService.createTaskQuery()
                .taskCandidateUser("user1")
                .singleResult();
        log.info("task candidate by user1: {}", taskByUser1);

        Task taskByGroup = taskService.createTaskQuery()
                .taskCandidateGroupIn(Lists.newArrayList("group1"))
                .singleResult();
        log.info("task candidate group by user1: {}", taskByGroup);

        // 根据任务处理人查询
        Task taskByAssign = taskService.createTaskQuery()
                .taskAssignee("user1")
                .singleResult();
        log.info("task by assign: {}", taskByAssign);
    }
}
