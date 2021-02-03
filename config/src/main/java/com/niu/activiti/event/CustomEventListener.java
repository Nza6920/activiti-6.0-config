package com.niu.activiti.event;

import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义事件监听器
 *
 * @author [nza]
 * @version 1.0 [2021/02/03 14:52]
 * @createTime [2021/02/03 14:52]
 */
public class CustomEventListener implements ActivitiEventListener {

    private static final Logger log = LoggerFactory.getLogger(CustomEventListener.class);

    @Override
    public void onEvent(ActivitiEvent event) {
        if (event instanceof ActivitiEntityEvent) {
            ActivitiEventType type = event.getType();
            Object data = ((ActivitiEntityEvent) event).getEntity();
            if (type == ActivitiEventType.CUSTOM) {
                log.info("自定义事件: {}, 参数: {}", type, data);
            }
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
