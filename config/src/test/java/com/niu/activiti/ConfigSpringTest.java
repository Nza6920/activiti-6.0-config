package com.niu.activiti;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 集成Spring测试
 *
 * @author [nza]
 * @version 1.0 [2021/02/04 10:15]
 * @createTime [2021/02/04 10:15]
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(locations = {"classpath:activiti.context.xml"})
public class ConfigSpringTest {

    private static final Logger log = LoggerFactory.getLogger(ConfigSpringTest.class);

}
