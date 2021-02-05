package com.niu.activiti.coreapi;


import com.niu.activiti.coreapi.mapper.CustomMapper;
import org.activiti.engine.ManagementService;
import org.activiti.engine.impl.cmd.AbstractCustomSqlExecution;
import org.activiti.engine.impl.persistence.entity.JobEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.management.TablePage;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.*;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 管理服务
 *
 * @author [nza]
 * @version 1.0 [2021/02/05 13:42]
 * @createTime [2021/02/05 13:42]
 */
public class ManagementServiceTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(ManagementServiceTest.class);

    public ManagementServiceTest() {
        super("activiti.cfg.manage.api.xml");
    }

    @Test
    public void testJobQuery() {

        // 部署
        ProcessDefinition processDefinition = deploy("my_process_management.bpmn20.xml");

        ManagementService managementService = processEngine.getManagementService();

        // 查询TimerJob
        log.info("查询TimerJob");
        List<Job> jobList = managementService.createTimerJobQuery()
                .listPage(0, 100);
        for (Job timerJob : jobList) {
            log.info("timerJob: {}", timerJob);
        }

        // 查询普通Job
        JobQuery jobQuery = managementService.createJobQuery();

        // 查询挂起Job
        SuspendedJobQuery suspendedJobQuery = managementService.createSuspendedJobQuery();

        // 查询死信Job
        DeadLetterJobQuery deadLetterJobQuery = managementService.createDeadLetterJobQuery();
    }

    @Test
    public void testTablePageQuery() {

        // 部署
        ProcessDefinition processDefinition = deploy("my_process_management.bpmn20.xml");

        ManagementService managementService = processEngine.getManagementService();

        // 查询流程部署表数据
        TablePage tablePage = managementService.createTablePageQuery()
                .tableName(managementService.getTableName(JobEntity.class))
                .listPage(0, 100);
        for (Map<String, Object> row : tablePage.getRows()) {
            log.info("row: {}", row);
        }
    }

    /**
     * 自定义查询语句
     */
    @Test
    public void testCustomQuery() {
        // 部署
        ProcessDefinition processDefinition = deploy("my_process.bpmn20.xml");

        ManagementService managementService = processEngine.getManagementService();

        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey());

        // 自定义查询
        List<Map<String, Object>> mapList = managementService.executeCustomSql(new AbstractCustomSqlExecution<CustomMapper, List<Map<String, Object>>>(CustomMapper.class) {
            @Override
            public List<Map<String, Object>> execute(CustomMapper customMapper) {
                return customMapper.findAll();
            }
        });
        for (Map<String, Object> item : mapList) {
            log.info("item: {}", item);
        }
    }

    /**
     * 测试执行命令
     */
    @Test
    public void testCommand() {
        // 部署
        ProcessDefinition processDefinition = deploy("my_process.bpmn20.xml");

        ManagementService managementService = processEngine.getManagementService();

        // 执行自定义命令
        ProcessDefinitionEntity processDefinitionEntity = managementService.executeCommand(commandContext -> commandContext.getProcessDefinitionEntityManager()
                .findLatestProcessDefinitionByKey(processDefinition.getKey()));

        log.info("processDefinitionEntity: {}", processDefinitionEntity);
    }
}
