package com.niu.activiti;

import org.activiti.engine.runtime.Job;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 测试任务配置
 *
 * @author [nza]
 * @version 1.0 [2021/02/03 16:18]
 * @createTime [2021/02/03 16:18]
 */
public class ConfigJobTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(ConfigJobTest.class);

    public ConfigJobTest() {
        super("activiti.cfg.job.xml");
    }

    @Test
    public void testJob() throws InterruptedException {

        // 部署流程文件
        deploy("my_process_job.bpmn20.xml");

        log.info("start");

        // 查询定时任务
        List<Job> jobs = processEngine.getManagementService()
                .createTimerJobQuery()
                .listPage(0, 100);
        for (Job job : jobs) {
            log.info("定时任务: {} \t 默认重试次数: {}", job, job.getRetries());
        }
        log.info("定时任务数量: {}", jobs.size());

        Thread.sleep(1000 * 100);
        log.info("end");
    }
}
