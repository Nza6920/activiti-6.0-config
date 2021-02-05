package com.niu.activiti.db;

import org.activiti.engine.impl.persistence.entity.ByteArrayEntityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通用数据库测试
 *
 * @author [nza]
 * @version 1.0 [2021/02/05 17:48]
 * @createTime [2021/02/05 17:48]
 */
public class DbGeTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(DbGeTest.class);

    public DbGeTest() {
        super("activiti.cfg.db_mysql.xml");
    }

    @Test
    public void testByteArray() {
        // 部署
        ProcessDefinition processDefinition = deploy("my_process.bpmn20.xml");
    }

    @Test
    public void testByteArrayInsert() {

        processEngine.getManagementService()
                .executeCommand(commandContext -> {
                    log.info("手动插入字节流数据");
                    ByteArrayEntityImpl entity = new ByteArrayEntityImpl();
                    entity.setName("test");
                    entity.setBytes("test test".getBytes());
                    commandContext.getByteArrayEntityManager()
                            .insert(entity);
                    return null;
                });
    }
}
