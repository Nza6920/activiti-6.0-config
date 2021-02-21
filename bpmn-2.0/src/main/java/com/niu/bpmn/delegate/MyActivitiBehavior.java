package com.niu.bpmn.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.delegate.ActivityBehavior;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * My Activiti Behavior
 *
 * @author [nza]
 * @version 1.0 2021/2/18
 * @createTime 22:00
 */
public class MyActivitiBehavior implements ActivityBehavior, Serializable {

    private static final long serialVersionUID = -8432946248656423229L;

    private static final Logger log = LoggerFactory.getLogger(MyJavaDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {

        log.info("MyActivitiBehavior is Running, EventName: {}", execution.getEventName());


    }
}
