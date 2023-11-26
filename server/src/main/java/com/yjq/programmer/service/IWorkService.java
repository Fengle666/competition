package com.yjq.programmer.service;

import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.WorkDTO;

import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2023-04-22 12:43
 */
public interface IWorkService {

    // 保存作品信息
    ResponseDTO<Boolean> saveWork(WorkDTO workDTO);

    // 分页获取作品数据
    ResponseDTO<PageDTO<WorkDTO>> getWorkList(PageDTO<WorkDTO> pageDTO);

    // 获取所有作品信息
    ResponseDTO<List<WorkDTO>> getAllWork(WorkDTO workDTO);

    // 删除作品信息
    ResponseDTO<Boolean> deleteWork(WorkDTO workDTO);
}
