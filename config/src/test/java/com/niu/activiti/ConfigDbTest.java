package com.niu.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据库配置
 *
 * @author [nza]
 * @version 1.0 [2021/02/02 14:14]
 * @createTime [2021/02/02 14:14]
 */
public class ConfigDbTest {

    private static final Logger log = LoggerFactory.getLogger(ConfigDbTest.class);

    @Test
    public void testConfig() {
        // 获取默认的流程引擎对象 根据资源文件加载
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResourceDefault();

        log.info("processEngineConfiguration = {}", configuration);

        ProcessEngine processEngine = configuration.buildProcessEngine();

        log.info("获取流程引擎: {}", processEngine.getName());

        processEngine.close();
    }

    @Test
    public void testDataSourceConfig() {
        // 获取默认的流程引擎对象 根据资源文件加载
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti.druid.cfg.xml");

        log.info("processEngineConfiguration = {}", configuration);

        ProcessEngine processEngine = configuration.buildProcessEngine();

        log.info("获取流程引擎: {}", processEngine.getName());

        processEngine.close();
    }
}
