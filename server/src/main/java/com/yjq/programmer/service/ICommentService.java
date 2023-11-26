package com.yjq.programmer.service;

import com.yjq.programmer.dto.CommentDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2023-04-27 9:22
 */
public interface ICommentService {

    // 保存评论信息
    ResponseDTO<Boolean> saveComment(CommentDTO commentDTO);

    // 分页获取评论数据
    ResponseDTO<PageDTO<CommentDTO>> getCommentList(PageDTO<CommentDTO> pageDTO);

    // 删除评论信息
    ResponseDTO<Boolean> deleteComment(CommentDTO commentDTO);
}
