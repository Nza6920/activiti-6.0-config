package com.niu.activiti.interceptor;

import org.activiti.engine.impl.agenda.AbstractOperation;
import org.activiti.engine.impl.interceptor.DebugCommandInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志拦截器
 *
 * @author [nza]
 * @version 1.0 [2021/02/02 16:49]
 * @createTime [2021/02/02 16:49]
 */
public class LogCommandInvoker extends DebugCommandInvoker {

    private static final Logger logger = LoggerFactory.getLogger(LogCommandInvoker.class);

    @Override
    public void executeOperation(Runnable runnable) {
        if (runnable instanceof AbstractOperation) {
            AbstractOperation operation = (AbstractOperation) runnable;
            if (operation.getExecution() != null) {
                logger.info("拦截到了： {}", operation);
            }
        }
        super.executeOperation(runnable);
    }
}
