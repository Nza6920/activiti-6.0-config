package com.niu.bpmn.listener;

import com.google.common.collect.Lists;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务监听器
 *
 * @author [nza]
 * @version 1.0 2021/2/18
 * @createTime 17:23
 */
public class MyTaskListener implements TaskListener {


    private static final Logger log = LoggerFactory.getLogger(MyTaskListener.class);

    @Override
    public void notify(DelegateTask task) {

        log.info("MyTaskListener Running, eventName: {}", task.getEventName());

        // 添加候选用户组
        task.addCandidateUsers(Lists.newArrayList("user1", "user2"));

        // 添加候选组
        task.addCandidateGroup("group1");

        // 设置变量
        task.setVariable("k1", "v1");

        // 设置过期时间
        task.setDueDate(DateTime.now().plusDays(3).toDate());
    }
}
