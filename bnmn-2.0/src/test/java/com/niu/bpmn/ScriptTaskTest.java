package com.niu.bpmn;

import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.collections.Maps;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;
import java.util.Map;

/**
 * 脚本任务
 *
 * @author [nza]
 * @version 1.0 2021/2/18
 * @createTime 18:45
 */
public class ScriptTaskTest extends BaseTest {

    private Logger log = LoggerFactory.getLogger(ScriptTaskTest.class);

    public ScriptTaskTest() {
        super("activiti.cfg.db.xml");
    }


    /**
     * 测试 groovy 脚本
     */
    @Test
    public void testScriptGroovy() {

        ProcessDefinition processDefinition = deploy("my_process_script_groovy.bpmn20.xml");
        log.info("部署流程定义文件: {}", processDefinition.getKey());

        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey());
        log.info("启动流程: {}", processInstance.getProcessDefinitionKey());


        List<HistoricVariableInstance> historicVariableInstances = processEngine.getHistoryService()
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstance.getId())
                .orderByVariableName()
                .asc()
                .listPage(0, 100);
        for (HistoricVariableInstance variable : historicVariableInstances) {
            log.info("name: {} key: {}", variable.getVariableName(), variable.getValue());
        }
    }

    /**
     * 测试 juel 脚本
     */
    @Test
    public void testScriptJuel() {

        ProcessDefinition processDefinition = deploy("my_process_script_juel.bpmn20.xml");
        log.info("部署流程定义文件: {}", processDefinition.getKey());


        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", 1);
        variables.put("key2", 3);

        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey(), variables);
        log.info("启动流程: {}", processInstance.getProcessDefinitionKey());

        List<HistoricVariableInstance> historicVariableInstances = processEngine.getHistoryService()
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstance.getId())
                .orderByVariableName()
                .asc()
                .listPage(0, 100);
        for (HistoricVariableInstance variable : historicVariableInstances) {
            log.info("variable: {}", variable);
        }
    }

    /**
     * 测试 juel 脚本
     */
    @Test
    public void testScriptJavascript() {

        ProcessDefinition processDefinition = deploy("my_process_script_javascript.bpmn20.xml");
        log.info("部署流程定义文件: {}", processDefinition.getKey());


        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", 2);
        variables.put("key2", 4);

        ProcessInstance processInstance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(processDefinition.getKey(), variables);
        log.info("启动流程: {}", processInstance.getProcessDefinitionKey());

        List<HistoricVariableInstance> historicVariableInstances = processEngine.getHistoryService()
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstance.getId())
                .orderByVariableName()
                .desc()
                .listPage(0, 100);
        for (HistoricVariableInstance variable : historicVariableInstances) {
            log.info("variable: {}", variable);
        }
    }

    /**
     * 测试脚本引擎
     */
    @Test
    public void testScriptEngine() throws ScriptException {

        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine juelEngine = scriptEngineManager.getEngineByName("juel");

        Object res = juelEngine.eval("${1 + 2}");

        log.info("result: {}", res);
    }
}
