package com.yjq.programmer.dao;

import com.yjq.programmer.domain.Result;
import com.yjq.programmer.domain.ResultExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ResultMapper {
    int countByExample(ResultExample example);

    int deleteByExample(ResultExample example);

    int deleteByPrimaryKey(String id);

    int insert(Result record);

    int insertSelective(Result record);

    List<Result> selectByExample(ResultExample example);

    Result selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Result record, @Param("example") ResultExample example);

    int updateByExample(@Param("record") Result record, @Param("example") ResultExample example);

    int updateByPrimaryKeySelective(Result record);

    int updateByPrimaryKey(Result record);
}
