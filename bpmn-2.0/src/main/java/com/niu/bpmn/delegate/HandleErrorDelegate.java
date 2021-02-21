package com.niu.bpmn.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Java delegate
 *
 * @author [nza]
 * @version 1.0 2021/2/18
 * @createTime 21:57
 */
public class HandleErrorDelegate implements JavaDelegate, Serializable {

    private static final long serialVersionUID = 8575381967220201077L;

    private static final Logger log = LoggerFactory.getLogger(HandleErrorDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        log.info("HandleErrorDelegate is Running............");
    }
}
