package com.niu.activiti.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试mdc
 *
 * @version 1.0 [2021/02/02 16:43]
 * @author [nza]
 * @createTime [2021/02/02 16:43]
 */
public class MDCErrorDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(MDCErrorDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        log.info("run MDCErrorDelegate");
        throw new RuntimeException("TEST");
    }
}
