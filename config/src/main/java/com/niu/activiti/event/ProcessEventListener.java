package com.niu.activiti.event;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 监听流程开始事件
 *
 * @author [nza]
 * @version 1.0 [2021/02/03 14:21]
 * @createTime [2021/02/03 14:21]
 */
public class ProcessEventListener implements ActivitiEventListener {

    private static final Logger log = LoggerFactory.getLogger(ProcessEventListener.class);

    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiEventType type = event.getType();
        switch (type) {
            case PROCESS_STARTED:
                log.info("流程启动: {} \t 流程实例ID: {}", type, event.getProcessInstanceId());
                break;
            case PROCESS_COMPLETED:
                log.info("流程完成: {} \t 流程实例ID: {}", type, event.getProcessInstanceId());
            default:
                break;
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
