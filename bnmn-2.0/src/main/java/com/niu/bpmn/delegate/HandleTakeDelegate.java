package com.niu.bpmn.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * 确认收货
 *
 * @version 1.0 [2021/02/21 20:44]
 * @author [nza]
 * @createTime [2021/02/21 20:44]
 */
public class HandleTakeDelegate implements JavaDelegate, Serializable {

    private static final Logger log = LoggerFactory.getLogger(HandlePayDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        log.info("确认收货");
    }
}
