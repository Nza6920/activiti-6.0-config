package com.niu.activiti.db;

import com.google.common.collect.Lists;
import org.activiti.engine.ManagementService;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Db 相关配置
 *
 * @version 1.0 [2021/02/05 17:31]
 * @author [nza]
 * @createTime [2021/02/05 17:31]
 */
public class DbConfigTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(DbConfigTest.class);

    public DbConfigTest() {
        super("activiti.cfg.db_mysql.xml");
    }

    /**
     * 测试数据库配置
     */
    @Test
    public void testDbConfig() {

        ProcessDefinition processDefinition = deploy("my_process.bpmn20.xml");

        ManagementService managementService = processEngine.getManagementService();

        log.info("查询数据库表信息");
        Map<String, Long> tableCount = managementService.getTableCount();
        ArrayList<String> tableNames = Lists.newArrayList(tableCount.keySet());
        Collections.sort(tableNames);

        log.info("表数量: {}", tableNames.size());
        for (String tableName : tableNames) {
            log.info("table: {}", tableName);
        }
    }

    /**
     * 测试清理表
     */
    @Test
    public void testDropDbConfig() {

        ProcessDefinition processDefinition = deploy("my_process.bpmn20.xml");

        ManagementService managementService = processEngine.getManagementService();

        managementService.executeCommand(new Command<Object>() {
            @Override
            public Object execute(CommandContext commandContext) {
                log.info("开始删除表结构...");
                commandContext.getDbSqlSession().dbSchemaDrop();
                log.info("删除表结构成功");
                return null;
            }
        });
    }
}
