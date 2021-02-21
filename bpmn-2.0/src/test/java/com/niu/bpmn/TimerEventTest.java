package com.niu.bpmn;

import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 定时边界事件测试
 *
 * @author [nza]
 * @version 1.0 2021/2/17
 * @createTime 2:02
 */
public class TimerEventTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(TimerEventTest.class);

    public TimerEventTest() {
        super("activiti.cfg.db.xml");
    }

    @Test
    public void testTimerBoundary() throws InterruptedException {

        ProcessDefinition processDefinition = deploy("my_process_timer-boundary.bpmn20.xml");


        // 启动流程
        log.info("启动流程: {}", processDefinition.getKey());
        processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey());
        
        
        TaskService taskService = processEngine.getTaskService();

        log.info("查询触发定时边界任务前的 userTask 列表");
        // 查询触发定时边界任务前的 userTask 列表
        List<Task> beforeTasks = taskService.createTaskQuery()
                .listPage(0, 100);
        for (Task beforeTask : beforeTasks) {
            log.info("beforeTask: {}, {}", beforeTask.getName(), beforeTask);
        }

        // 休眠 10s
        Thread.sleep(1000 * 10);

        log.info("查询触发定时边界任务后的 userTask 列表");
        // 查询触发定时边界任务后的 userTask 列表
        List<Task> timerBoundaryTasks = taskService.createTaskQuery()
                .listPage(0, 100);
        for (Task timerBoundaryTask : timerBoundaryTasks) {
            log.info("timerBoundaryTask: {}, {}", timerBoundaryTask.getName(), timerBoundaryTask);
        }
    }
}
