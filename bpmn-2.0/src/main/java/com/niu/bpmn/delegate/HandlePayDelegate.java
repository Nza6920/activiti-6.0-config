package com.niu.bpmn.delegate;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Objects;

/**
 * 确认支付
 *
 * @version 1.0 [2021/02/21 20:44]
 * @author [nza]
 * @createTime [2021/02/21 20:44]
 */
public class HandlePayDelegate implements JavaDelegate, Serializable {

    private static final Logger log = LoggerFactory.getLogger(HandlePayDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        log.info("确认支付");

        execution.getParent().setVariableLocal("K2", "V2");
        execution.setVariable("callSubK1", "callSubV1");
        execution.setVariable("callSubK2", "callSubV2");

        log.info("getVariables: {}", execution.getVariables());

        // 抛出错误事件
        if (Objects.equals(execution.getVariable("errorFlag"), true)) {
            throw new BpmnError("bpmnError");
        }
    }
}
