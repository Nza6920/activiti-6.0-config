package com.niu.activiti.coreapi.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试业务任务
 *
 * @author [nza]
 * @version 1.0 [2021/02/05 14:55]
 * @createTime [2021/02/05 14:55]
 */
public class TestServiceTask implements JavaDelegate {

    /**
     * 计数器
     */
    public static Integer time = 0;

    private static final Logger log = LoggerFactory.getLogger(TestServiceTask.class);

    @Override
    public void execute(DelegateExecution execution) {
        // 消费 投资 净出口
        log.info("这是第: {} 次", ++time);
    }
}
