package com.niu.activiti.event;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务时间监听器
 *
 * @version 1.0 [2021/02/03 16:34]
 * @author [nza]
 * @createTime [2021/02/03 16:34]
 */
public class JobEventListener implements ActivitiEventListener {

    private static final Logger log = LoggerFactory.getLogger(JobEventListener.class);

    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiEventType type = event.getType();
        switch (type) {
            case TIMER_SCHEDULED:
                log.info("定时任务准备执行: {} \t 流程实例ID: {}", type, event.getProcessInstanceId());
                break;
            case TIMER_FIRED:
                log.info("定时任务启动成功: {} \t 流程实例ID: {}", type, event.getProcessInstanceId());
                break;
            case JOB_EXECUTION_SUCCESS:
                log.info("任务执行成功: {} \t 流程实例ID: {}", type, event.getProcessInstanceId());
                break;
            case JOB_RETRIES_DECREMENTED:
                log.info("任务重试: {} \t 流程实例ID: {}", type, event.getProcessInstanceId());
                break;
            default:
                break;
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
