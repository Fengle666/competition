package com.yjq.programmer.service;

import com.yjq.programmer.domain.Contest;
import com.yjq.programmer.dto.ContestDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;

import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2023-04-21 14:47
 */
public interface IContestService {

    // 分页获取竞赛数据
    ResponseDTO<PageDTO<ContestDTO>> getContestList(PageDTO<ContestDTO> pageDTO);

    // 保存竞赛信息
    ResponseDTO<Boolean> saveContest(ContestDTO contestDTO);

    // 删除竞赛信息
    ResponseDTO<Boolean> deleteContest(ContestDTO contestDTO);

    // 获取所有竞赛信息
    ResponseDTO<List<Contest>> getAllContest(ContestDTO contestDTO);

    // 根据时间范围获取竞赛个数
    ResponseDTO<List<Integer>> getContestCountByDate();
}
