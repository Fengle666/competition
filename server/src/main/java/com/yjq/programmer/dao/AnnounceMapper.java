package com.yjq.programmer.dao;

import com.yjq.programmer.domain.Announce;
import com.yjq.programmer.domain.AnnounceExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AnnounceMapper {
    int countByExample(AnnounceExample example);

    int deleteByExample(AnnounceExample example);

    int deleteByPrimaryKey(String id);

    int insert(Announce record);

    int insertSelective(Announce record);

    List<Announce> selectByExample(AnnounceExample example);

    Announce selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Announce record, @Param("example") AnnounceExample example);

    int updateByExample(@Param("record") Announce record, @Param("example") AnnounceExample example);

    int updateByPrimaryKeySelective(Announce record);

    int updateByPrimaryKey(Announce record);
}
