package com.niu.activiti.db;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 流程定义存储表测试
 *
 * @version 1.0 [2021/02/08 13:05]
 * @author [nza]
 * @createTime [2021/02/08 13:05]
 */
public class DbRepositoryTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(DbRepositoryTest.class);

    public DbRepositoryTest() {
        super("activiti.cfg.db_mysql.xml");
    }

    /**
     * 测试部署
     */
    @Test
    public void testDeploy() {
        deploy("my_process_re.bpmn20.xml", "流程定义测试", "review");
    }

    /**
     * 测试挂起
     */
    @Test
    public void testSuspend() {

        // 挂起流程定义文件
        processEngine.getRepositoryService()
                .suspendProcessDefinitionById("my_process:3:7503");


        // 流程定义文件是否被挂起
        boolean suspended = processEngine.getRepositoryService()
                .isProcessDefinitionSuspended("my_process:3:7503");
        log.info("suspended: {}", suspended);

    }
}
