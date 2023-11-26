package com.yjq.programmer.dao;

import com.yjq.programmer.domain.Sign;
import com.yjq.programmer.domain.SignExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SignMapper {
    int countByExample(SignExample example);

    int deleteByExample(SignExample example);

    int deleteByPrimaryKey(String id);

    int insert(Sign record);

    int insertSelective(Sign record);

    List<Sign> selectByExample(SignExample example);

    Sign selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Sign record, @Param("example") SignExample example);

    int updateByExample(@Param("record") Sign record, @Param("example") SignExample example);

    int updateByPrimaryKeySelective(Sign record);

    int updateByPrimaryKey(Sign record);
}
