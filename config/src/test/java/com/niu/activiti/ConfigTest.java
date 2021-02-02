package com.niu.activiti;

import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置测试类
 *
 * @author [nza]
 * @version 1.0 [2021/02/02 13:24]
 * @createTime [2021/02/02 13:24]
 */
public class ConfigTest {

    private static final Logger log = LoggerFactory.getLogger(ConfigTest.class);

    @Test
    public void testConfig() {
        // 获取默认的流程引擎对象 根据资源文件加载
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResourceDefault();

        log.info("processEngineConfiguration = {}", processEngineConfiguration);
    }

    @Test
    public void testConfig2() {
        // 创建独立的流程引擎
        ProcessEngineConfiguration standaloneProcessEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        log.info("standaloneProcessEngineConfiguration = {}", standaloneProcessEngineConfiguration);
    }
}
