package com.yjq.programmer.service;

import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.ResultDTO;

import java.util.Map;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2023-04-22 20:03
 */
public interface IResultService {

    // 保存成绩信息
    ResponseDTO<Boolean> saveResult(ResultDTO resultDTO);

    // 分页获取成绩数据
    ResponseDTO<PageDTO<ResultDTO>> getResultList(PageDTO<ResultDTO> pageDTO);

    // 导出Excel文件
    ResponseDTO<Map<String, Object>> exportExcel(PageDTO<ResultDTO> pageDTO);
}
