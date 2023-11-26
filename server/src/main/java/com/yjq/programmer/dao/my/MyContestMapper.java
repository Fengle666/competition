package com.yjq.programmer.dao.my;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2023-04-27 13:57
 */
public interface MyContestMapper {

    // 根据时间范围获取竞赛个数
    Integer getContestTotalByDate(@Param("queryMap") Map<String, Object> queryMap);
}
