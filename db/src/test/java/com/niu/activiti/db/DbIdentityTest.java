package com.niu.activiti.db;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 身份数据表测试
 *
 * @version 1.0 [2021/02/08 13:05]
 * @author [nza]
 * @createTime [2021/02/08 13:05]
 */
public class DbIdentityTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(DbIdentityTest.class);

    public DbIdentityTest() {
        super("activiti.cfg.db_mysql.xml");
    }

    /**
     * 测试部署
     */
    @Test
    public void testIdentity() {

        IdentityService identityService = processEngine.getIdentityService();

        // 创建用户
        User user111 = identityService.newUser("user111");
        user111.setFirstName("N");
        user111.setLastName("za");
        user111.setEmail("1@qq.com");
        identityService.saveUser(user111);

        // 创建分组
        Group group11 = identityService.newGroup("group11");
        group11.setName("分组1");
        group11.setType("inner");
        identityService.saveGroup(group11);

        // 建立 关系
        identityService.createMembership(user111.getId(), group11.getId());

        // 设置用户扩展信息
        identityService.setUserInfo(user111.getId(), "age", "12");
        identityService.setUserInfo(user111.getId(), "address", "chengdu");
        identityService.setUserInfo(user111.getId(), "length", "18");
    }
}
