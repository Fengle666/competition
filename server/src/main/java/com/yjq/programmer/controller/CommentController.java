package com.yjq.programmer.controller;

import com.yjq.programmer.dto.CommentDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.ICommentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2023-04-27 9:21
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private ICommentService commentService;


    /**
     * 分页获取评论数据
     * @param pageDTO
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<CommentDTO>> getCommentList(@RequestBody PageDTO<CommentDTO> pageDTO){
        return commentService.getCommentList(pageDTO);
    }

    /**
     * 保存评论信息
     * @param commentDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO<Boolean> saveComment(@RequestBody CommentDTO commentDTO){
        return commentService.saveComment(commentDTO);
    }

    /**
     * 删除评论信息
     * @param commentDTO
     * @return
     */
    @PostMapping("/delete")
    public ResponseDTO<Boolean> deleteComment(@RequestBody CommentDTO commentDTO){
        return commentService.deleteComment(commentDTO);
    }
}
