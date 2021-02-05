package com.niu.activiti.coreapi.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 自定义Mapper
 *
 * @author [nza]
 * @version 1.0 [2021/02/05 14:10]
 * @createTime [2021/02/05 14:10]
 */
public interface CustomMapper {

    /**
     * 查询所有任务
     */
    @Select("SELECT * FROM ACT_RU_TASK")
    List<Map<String, Object>> findAll();
}
