package com.niu.activiti.coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.CommentEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 测试任务管理服务
 *
 * @author [nza]
 * @version 1.0 [2021/02/04 16:25]
 * @createTime [2021/02/04 16:25]
 */
public class TaskServiceTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(RuntimeServiceTest.class);

    public TaskServiceTest() {
        super("activiti.cfg.api.xml");
    }

    /**
     * 测试流程变量
     */
    @Test
    public void testTaskService() {

        TaskService taskService = processEngine.getTaskService();

        ProcessDefinition processDefinition = deploy("my_process_task.bpmn20.xml");

        Map<String, Object> variables = Maps.newHashMap();
        variables.put("message", "消息");
        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey(), variables);
        log.info("processInstance: {}", processInstance);

        // 查询当前 task
        Task task = taskService.createTaskQuery()
                .singleResult();
        log.info("task: {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        log.info("task.description: {}", task.getDescription());

        taskService.setVariable(task.getId(), "k1", "v1");
        taskService.setVariableLocal(task.getId(), "localK1", "localV1");

        Map<String, Object> queryVariables = taskService.getVariables(task.getId());
        log.info("queryVariables: {}", queryVariables);

        Map<String, Object> queryLocalVariables = taskService.getVariablesLocal(task.getId());
        log.info("queryLocalVariables: {}", queryLocalVariables);

        Map<String, Object> queryVariablesByExecutionId = processEngine.getRuntimeService().getVariables(task.getExecutionId());
        log.info("queryVariablesByExecutionId: {}", queryVariablesByExecutionId);

        // 完成任务
        Map<String, Object> completeVar = Maps.newHashMap();
        completeVar.put("ck1", "cv1");
        taskService.complete(task.getId(), completeVar);

        // 查询当前 task
        task = taskService.createTaskQuery()
                .taskId(task.getId())
                .singleResult();
        log.info("task: {}", task);
    }

    /**
     * 测试待办人
     */
    @Test
    public void testTaskServiceUser() {

        TaskService taskService = processEngine.getTaskService();

        ProcessDefinition processDefinition = deploy("my_process_task.bpmn20.xml");

        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey());
        log.info("processInstance: {}", processInstance);

        // 查询当前 task
        Task task = taskService.createTaskQuery()
                .singleResult();
        log.info("task: {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));

        // 设置任务发起人
        taskService.setOwner(task.getId(), "user1");

        // 设置待办人
        // setAssignee 不推荐使用
//        taskService.setAssignee(task.getId(), "jimmy");

        // 查询未指定待办人的任务
        List<Task> taskUnAssigns = taskService.createTaskQuery()
                .taskCandidateUser("niu1")
                .taskUnassigned()
                .listPage(0, 100);
        for (Task item : taskUnAssigns) {
            log.info("未指定待办人的任务: {}", item);
            try {
                // 设置待办人
                taskService.claim(task.getId(), "jimmy");
            } catch (Exception e) {
                log.error("任务已经指定了代办人");
            }
        }

        // 查询任务与用户的关联
        List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(task.getId());
        for (IdentityLink identityLink : identityLinks) {
            log.info("identityLink: {}", identityLink);
        }

        // 查询指定用的代办
        List<Task> assignTasks = taskService.createTaskQuery()
                .taskAssignee("jimmy")
                .listPage(0, 100);
        for (Task assignTask : assignTasks) {
            Map<String, Object> completeVariables = Maps.newHashMap();
            completeVariables.put("ck1", "cv1");
            // 完成任务
            taskService.complete(assignTask.getId(), completeVariables);
        }

        List<Task> hasTasks = taskService.createTaskQuery()
                .taskAssignee("jimmy")
                .listPage(0, 100);
        log.info("是否为空： {}", CollectionUtils.isEmpty(hasTasks));
    }

    /**
     * 测试任务附件
     */
    @Test
    public void testTaskAttachment() {

        TaskService taskService = processEngine.getTaskService();

        ProcessDefinition processDefinition = deploy("my_process_task.bpmn20.xml");

        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey());
        log.info("processInstance: {}", processInstance);

        // 查询当前 task
        Task task = taskService.createTaskQuery()
                .singleResult();
        log.info("task: {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));

        // 添加附件
        Attachment attachment = taskService.createAttachment("url",
                task.getId(),
                processInstance.getId(),
                "附件1",
                "附件描述",
                "https://github.com/Nza6920");
        log.info("新增附件, attachment: {}", ToStringBuilder.reflectionToString(attachment, ToStringStyle.JSON_STYLE));

        // 查询任务附件列表
        List<Attachment> taskAttachments = taskService.getTaskAttachments(task.getId());
        for (Attachment taskAttachment : taskAttachments) {
            log.info("taskAttachment: {}", ToStringBuilder.reflectionToString(taskAttachment, ToStringStyle.JSON_STYLE));
        }
    }

    /**
     * 测试任务评论
     */
    @Test
    public void testTaskComment() {

        TaskService taskService = processEngine.getTaskService();

        ProcessDefinition processDefinition = deploy("my_process_task.bpmn20.xml");

        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey());
        log.info("processInstance: {}", processInstance);

        // 查询当前 task
        Task task = taskService.createTaskQuery()
                .singleResult();
        log.info("task: {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));

        // 添加 Comment
        taskService.addComment(task.getId(), processInstance.getId(), "comment1", CommentEntity.TYPE_COMMENT);
        taskService.addComment(task.getId(), processInstance.getId(), "comment2");

        // 查询任务评论
        List<Comment> taskComments = taskService.getTaskComments(task.getId());
        for (Comment taskComment : taskComments) {
            log.info("taskComment: {}", ToStringBuilder.reflectionToString(taskComment, ToStringStyle.JSON_STYLE));
        }
    }


    /**
     * 测试事件记录（包含任务所有事件： 指定所有者、代办人。。。。）
     */
    @Test
    public void testTaskEventRecord() {

        TaskService taskService = processEngine.getTaskService();

        ProcessDefinition processDefinition = deploy("my_process_task.bpmn20.xml");

        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey());
        log.info("processInstance: {}", processInstance);

        // 查询当前 task
        Task task = taskService.createTaskQuery()
                .singleResult();
        log.info("task: {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));

        // 添加 Comment
        taskService.addComment(task.getId(), processInstance.getId(), "comment1");
        taskService.addComment(task.getId(), processInstance.getId(), "comment2");

        taskService.setOwner(task.getId(), "niu1");
        taskService.claim(task.getId(), "niu2");

        // 查询任务评论
        List<Comment> taskComments = taskService.getTaskComments(task.getId());
        for (Comment taskComment : taskComments) {
            log.info("taskComment: {}", ToStringBuilder.reflectionToString(taskComment, ToStringStyle.JSON_STYLE));
        }

        // 查询事件记录
        List<Event> taskEvents = taskService.getTaskEvents(task.getId());
        for (Event event : taskEvents) {
            log.info("taskEvent: {}", ToStringBuilder.reflectionToString(event, ToStringStyle.JSON_STYLE));
        }
    }
}
