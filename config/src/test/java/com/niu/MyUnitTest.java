package com.niu;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * 测试模板
 *
 * @author [nza]
 * @version 1.0 [2021/02/02 15:26]
 * @createTime [2021/02/02 15:26]
 */
public class MyUnitTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = "my_process.bpmn20.xml")
    public void test() {

        // 创建流程
        ProcessInstance processInstance = activitiRule.getRuntimeService()
                .startProcessInstanceById("my_process");
        Assert.assertNotNull(processInstance);

        // 获取任务
        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        Assert.assertEquals("hello", task.getName());

        // 完成任务
        activitiRule.getTaskService().complete(task.getId());
    }

}
