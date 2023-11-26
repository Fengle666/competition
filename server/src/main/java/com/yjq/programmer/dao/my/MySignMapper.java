package com.yjq.programmer.dao.my;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2023-04-27 13:43
 */
public interface MySignMapper {

    // 根据时间范围获取报名人数
    Integer getSignTotalByDate(@Param("queryMap") Map<String, Object> queryMap);
}
