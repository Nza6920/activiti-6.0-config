package com.niu.bpmn.delegate;

import com.niu.bpmn.error.MyError;
import org.activiti.engine.delegate.BpmnError;
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
public class HandleProcessDelegate implements JavaDelegate, Serializable {


    private static final long serialVersionUID = -2687379088800172261L;

    private static final Logger log = LoggerFactory.getLogger(HandleProcessDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {

        log.info("HandleProcessDelegate is Running............");

        log.info("抛出自定义错误信号");
        throw new MyError("my_error");
    }
}
