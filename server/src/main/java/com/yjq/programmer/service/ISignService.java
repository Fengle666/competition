package com.yjq.programmer.service;

import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.SignDTO;

import java.util.List;
import java.util.Map;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2023-04-22 8:10
 */
public interface ISignService {

    // 报名竞赛
    ResponseDTO<Boolean> signContest(SignDTO signDTO);

    // 分页获取报名数据
    ResponseDTO<PageDTO<SignDTO>> getSignList(PageDTO<SignDTO> pageDTO);

    // 审核报名信息
    ResponseDTO<Boolean> editSign(SignDTO signDTO);

    // 删除报名信息
    ResponseDTO<Boolean> deleteSign(SignDTO signDTO);

    // 导出Excel文件
    ResponseDTO<Map<String, Object>> exportExcel(PageDTO<SignDTO> pageDTO);

    // 根据时间范围获取报名人数
    ResponseDTO<List<Integer>> getSignCountByDate();
}
