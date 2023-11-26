package com.yjq.programmer.service;

import com.yjq.programmer.dto.AnnounceDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2023-04-27 10:40
 */
public interface IAnnounceService {

    // 保存公告信息
    ResponseDTO<Boolean> saveAnnounce(AnnounceDTO announceDTO);

    // 分页获取公告数据
    ResponseDTO<PageDTO<AnnounceDTO>> getAnnounceList(PageDTO<AnnounceDTO> pageDTO);

    // 删除公告信息
    ResponseDTO<Boolean> deleteAnnounce(AnnounceDTO announceDTO);
}
