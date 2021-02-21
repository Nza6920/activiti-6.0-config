package com.niu.bpmn.error;

import org.activiti.engine.delegate.BpmnError;

/**
 * 自定义错误
 *
 * @author [nza]
 * @version 1.0 2021/2/19
 * @createTime 0:48
 */
public class MyError extends BpmnError {

    public MyError(String errorCode) {
        super(errorCode);
    }
}
