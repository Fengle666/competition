package com.yjq.programmer.dao;

import com.yjq.programmer.domain.Work;
import com.yjq.programmer.domain.WorkExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WorkMapper {
    int countByExample(WorkExample example);

    int deleteByExample(WorkExample example);

    int deleteByPrimaryKey(String id);

    int insert(Work record);

    int insertSelective(Work record);

    List<Work> selectByExample(WorkExample example);

    Work selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Work record, @Param("example") WorkExample example);

    int updateByExample(@Param("record") Work record, @Param("example") WorkExample example);

    int updateByPrimaryKeySelective(Work record);

    int updateByPrimaryKey(Work record);
}
