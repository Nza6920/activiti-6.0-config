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
public class MyJavaDelegate implements JavaDelegate, Serializable {

    private static final long serialVersionUID = -5045854687138729124L;

    private static final Logger log = LoggerFactory.getLogger(MyJavaDelegate.class);

    private Expression name;

    private Expression desc;

    @Override
    public void execute(DelegateExecution execution) {

        log.info("MyJavaDelegate is Running............");

        // 获取属性注入
        if (name != null) {
            Object nameValue = name.getValue(execution);
            log.info("name: {}", nameValue);
        }

        if (desc != null) {
            Object descValue = desc.getValue(execution);
            log.info("desc: {}", descValue);
        }
    }
}
