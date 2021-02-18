package com.niu.bpmn.delegate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Bean
 *
 * @author [nza]
 * @version 1.0 2021/2/19
 * @createTime 0:28
 */
public class MyJavaBean implements Serializable {

    private static final long serialVersionUID = 1013030005530862974L;

    private Logger log = LoggerFactory.getLogger(MyJavaBean.class);

    private String name;

    public MyJavaBean(String name) {
        this.name = name;
    }

    public void sayHello() {
        log.info("hello hello hello hello hello: {}", name);
    }

    public String getName() {
        log.info("name: {}", name);
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
