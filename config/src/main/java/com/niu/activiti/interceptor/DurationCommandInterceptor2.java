package com.niu.activiti.interceptor;

import org.activiti.engine.impl.interceptor.AbstractCommandInterceptor;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 命令执行时长拦截器
 *
 * @author [nza]
 * @version 1.0 [2021/02/03 15:43]
 * @createTime [2021/02/03 15:43]
 */
public class DurationCommandInterceptor2 extends AbstractCommandInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(DurationCommandInterceptor2.class);

    @Override
    public <T> T execute(CommandConfig config, Command<T> command) {
        logger.info("2命令开始执行");
        long start = System.currentTimeMillis();

        try {
            return this.getNext().execute(config, command);
        } finally {
            long end = System.currentTimeMillis();

            long duration = end - start;
            logger.info("2命令: {} \t 执行时长: {} ms", command.getClass().getSimpleName(), duration);
        }
    }
}
