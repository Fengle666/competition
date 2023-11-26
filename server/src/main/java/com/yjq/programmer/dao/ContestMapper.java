package com.yjq.programmer.dao;

import com.yjq.programmer.domain.Contest;
import com.yjq.programmer.domain.ContestExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ContestMapper {
    int countByExample(ContestExample example);

    int deleteByExample(ContestExample example);

    int deleteByPrimaryKey(String id);

    int insert(Contest record);

    int insertSelective(Contest record);

    List<Contest> selectByExample(ContestExample example);

    Contest selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Contest record, @Param("example") ContestExample example);

    int updateByExample(@Param("record") Contest record, @Param("example") ContestExample example);

    int updateByPrimaryKeySelective(Contest record);

    int updateByPrimaryKey(Contest record);
}
