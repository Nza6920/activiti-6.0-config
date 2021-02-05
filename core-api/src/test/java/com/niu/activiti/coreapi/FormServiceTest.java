package com.niu.activiti.coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.FormService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 表单管理服务
 *
 * @author [nza]
 * @version 1.0 [2021/02/05 10:42]
 * @createTime [2021/02/05 10:42]
 */
public class FormServiceTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(FormServiceTest.class);

    public FormServiceTest() {
        super("activiti.cfg.api.xml");
    }

    @Test
    public void testFormService() {

        FormService formService = processEngine.getFormService();

        ProcessDefinition processDefinition = deploy("my_process_form.bpmn20.xml");

        // 查询开始节点表单key
        String startFormKey = formService.getStartFormKey(processDefinition.getId());
        log.info("startFormKey: {}", startFormKey);

        // 查询开始节点表单属性
        StartFormData startFormData = formService.getStartFormData(processDefinition.getId());
        List<FormProperty> startFormProperties = startFormData.getFormProperties();
        for (FormProperty formProperty : startFormProperties) {
            log.info("startFormProperty: {}", ToStringBuilder.reflectionToString(formProperty, ToStringStyle.JSON_STYLE));
        }

        // 通过表单提交启动流程
        Map<String, String> startProperties = Maps.newHashMap();
        startProperties.put("message", "start process");
        ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), startProperties);
        log.info("流程启动成功: {}", processInstance);
        Task task = processEngine.getTaskService()
                .createTaskQuery()
                .singleResult();

        // 查询任务表单属性
        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormProperty> taskFormDataProperties = taskFormData.getFormProperties();
        for (FormProperty formProperty : taskFormDataProperties) {
            log.info("taskFormProperty: {}", ToStringBuilder.reflectionToString(formProperty, ToStringStyle.JSON_STYLE));
        }

        // 通过表单进行流程流转
        log.info("流程开始流转");
        Map<String, String> taskProperties = Maps.newHashMap();
        taskProperties.put("yesOrNo", "yes");
        formService.submitTaskFormData(task.getId(), taskProperties);

        // 查询任务是否存在
        ProcessInstance isProcessFinish = processEngine.getRuntimeService()
                .createProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();
        log.info("流程是否结束： {}", isProcessFinish == null);
    }
}
