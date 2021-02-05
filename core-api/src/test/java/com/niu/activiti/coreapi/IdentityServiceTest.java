package com.niu.activiti.coreapi;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 身份管理服务测试
 *
 * @author [nza]
 * @version 1.0 [2021/02/05 09:51]
 * @createTime [2021/02/05 09:51]
 */
public class IdentityServiceTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(IdentityServiceTest.class);

    public IdentityServiceTest() {
        super("activiti.cfg.api.xml");
    }

    @Test
    public void testIdentity() {

        IdentityService identityService = processEngine.getIdentityService();

        // 创建用户
        User user1 = identityService.newUser("user_1");
        user1.setEmail("user1@qq.com");
        identityService.saveUser(user1);

        User user2 = identityService.newUser("user_2");
        user2.setEmail("user2@qq.com");
        identityService.saveUser(user2);

        // 创建分组
        Group group1 = identityService.newGroup("group1");
        identityService.saveGroup(group1);
        Group group2 = identityService.newGroup("group2");
        identityService.saveGroup(group2);

        // 创建关系
        identityService.createMembership(user1.getId(), group1.getId());
        identityService.createMembership(user2.getId(), group1.getId());
        identityService.createMembership(user1.getId(), group2.getId());

        // 修改用户数据
        User queryUser = identityService.createUserQuery()
                .userId(user1.getId())
                .singleResult();
        queryUser.setLastName("xiaoHong");
        identityService.saveUser(queryUser);

        // 查询分组用户
        List<User> userList = identityService.createUserQuery()
                .memberOfGroup(group1.getId())
                .listPage(0, 100);
        for (User user : userList) {
            log.info("user: {}", ToStringBuilder.reflectionToString(user, ToStringStyle.JSON_STYLE));
        }

        // 查询用户所在分组
        List<Group> groups = identityService.createGroupQuery()
                .groupMember(user1.getId())
                .listPage(0, 100);
        for (Group group : groups) {
            log.info("group: {}", ToStringBuilder.reflectionToString(group, ToStringStyle.JSON_STYLE));
        }
    }
}
