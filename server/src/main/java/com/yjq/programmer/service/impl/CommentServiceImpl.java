package com.yjq.programmer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.CommentMapper;
import com.yjq.programmer.dao.UserMapper;
import com.yjq.programmer.domain.Comment;
import com.yjq.programmer.domain.CommentExample;
import com.yjq.programmer.domain.User;
import com.yjq.programmer.dto.CommentDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.UserDTO;
import com.yjq.programmer.service.ICommentService;
import com.yjq.programmer.util.CommonUtil;
import com.yjq.programmer.util.CopyUtil;
import com.yjq.programmer.util.UuidUtil;
import com.yjq.programmer.util.ValidateEntityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2023-04-27 9:22
 */
@Service
@Transactional
public class CommentServiceImpl implements ICommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * 保存评论信息
     * @param commentDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveComment(CommentDTO commentDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(commentDTO);
        if (!validate.getCode().equals(CodeMsg.SUCCESS.getCode())) {
            return ResponseDTO.errorByMsg(validate);
        }
        Comment comment = CopyUtil.copy(commentDTO, Comment.class);
        if(CommonUtil.isEmpty(comment.getId())) {
            // 添加操作
            comment.setId(UuidUtil.getShortUuid());
            comment.setCreateTime(new Date());
            if(commentMapper.insertSelective(comment) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.COMMENT_ADD_ERROR);
            }
        } else {
            // 修改操作
            if(commentMapper.updateByPrimaryKeySelective(comment) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.COMMENT_EDIT_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "保存评论信息成功！");
    }

    /**
     * 分页获取评论数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<CommentDTO>> getCommentList(PageDTO<CommentDTO> pageDTO) {
        CommentExample commentExample = new CommentExample();
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        // 不知道每页多少条记录，默认为每页5条记录
        if(pageDTO.getSize() == null){
            pageDTO.setSize(5);
        }
        CommentExample.Criteria c1 = commentExample.createCriteria();
        if(pageDTO.getParam() != null) {
            CommentDTO commentDTO = pageDTO.getParam();
            if(!CommonUtil.isEmpty(commentDTO.getContent())) {
                c1.andContentLike("%" + commentDTO.getContent() + "%");
            }
            if(!CommonUtil.isEmpty(commentDTO.getUserId())) {
                c1.andUserIdEqualTo(commentDTO.getUserId());
            }
        }
        commentExample.setOrderByClause("create_time desc");
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出评论数据
        List<Comment> commentList = commentMapper.selectByExample(commentExample);
        PageInfo<Comment> pageInfo = new PageInfo<>(commentList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 将domain类型数据  转成 DTO类型数据
        List<CommentDTO> commentDTOList = CopyUtil.copyList(commentList, CommentDTO.class);
        for(CommentDTO commentDTO : commentDTOList) {
            User user = userMapper.selectByPrimaryKey(commentDTO.getUserId());
            if(user == null) {
                commentDTO.setUserDTO(new UserDTO());
            } else {
                commentDTO.setUserDTO(CopyUtil.copy(user, UserDTO.class));
            }
        }
        pageDTO.setList(commentDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 删除评论信息
     * @param commentDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> deleteComment(CommentDTO commentDTO) {
        if(CommonUtil.isEmpty(commentDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 删除评论信息
        if(commentMapper.deleteByPrimaryKey(commentDTO.getId()) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.COMMENT_DELETE_ERROR);
        }
        return ResponseDTO.successByMsg(true, "删除评论信息成功！");
    }
}
