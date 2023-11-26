package com.yjq.programmer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.AnnounceMapper;
import com.yjq.programmer.dao.UserMapper;
import com.yjq.programmer.domain.*;
import com.yjq.programmer.dto.*;
import com.yjq.programmer.service.IAnnounceService;
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
 * @create 2023-04-27 10:40
 */
@Service
@Transactional
public class AnnounceServiceImpl implements IAnnounceService {

    @Resource
    private AnnounceMapper announceMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * 保存公告信息
     * @param announceDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveAnnounce(AnnounceDTO announceDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(announceDTO);
        if (!validate.getCode().equals(CodeMsg.SUCCESS.getCode())) {
            return ResponseDTO.errorByMsg(validate);
        }
        Announce announce = CopyUtil.copy(announceDTO, Announce.class);
        if(CommonUtil.isEmpty(announce.getId())) {
            // 添加操作
            announce.setId(UuidUtil.getShortUuid());
            announce.setCreateTime(new Date());
            if(announceMapper.insertSelective(announce) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.ANNOUNCE_ADD_ERROR);
            }
        } else {
            // 修改操作
            if(announceMapper.updateByPrimaryKeySelective(announce) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.ANNOUNCE_EDIT_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "保存公告信息成功！");
    }

    /**
     * 分页获取公告数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<AnnounceDTO>> getAnnounceList(PageDTO<AnnounceDTO> pageDTO) {
        AnnounceExample announceExample = new AnnounceExample();
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        // 不知道每页多少条记录，默认为每页5条记录
        if(pageDTO.getSize() == null){
            pageDTO.setSize(5);
        }
        AnnounceExample.Criteria c1 = announceExample.createCriteria();
        if(pageDTO.getParam() != null) {
            AnnounceDTO announceDTO = pageDTO.getParam();
            if(!CommonUtil.isEmpty(announceDTO.getContent())) {
                c1.andContentLike("%" + announceDTO.getContent() + "%");
            }
            if(!CommonUtil.isEmpty(announceDTO.getUserId())) {
                c1.andUserIdEqualTo(announceDTO.getUserId());
            }
        }
        announceExample.setOrderByClause("create_time desc");
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出公告数据
        List<Announce> announceList = announceMapper.selectByExample(announceExample);
        PageInfo<Announce> pageInfo = new PageInfo<>(announceList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 将domain类型数据  转成 DTO类型数据
        List<AnnounceDTO> announceDTOList = CopyUtil.copyList(announceList, AnnounceDTO.class);
        for(AnnounceDTO announceDTO : announceDTOList) {
            User user = userMapper.selectByPrimaryKey(announceDTO.getUserId());
            if(user == null) {
                announceDTO.setUserDTO(new UserDTO());
            } else {
                announceDTO.setUserDTO(CopyUtil.copy(user, UserDTO.class));
            }
        }
        pageDTO.setList(announceDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 删除公告信息
     * @param announceDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> deleteAnnounce(AnnounceDTO announceDTO) {
        if(CommonUtil.isEmpty(announceDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 删除公告信息
        if(announceMapper.deleteByPrimaryKey(announceDTO.getId()) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.ANNOUNCE_DELETE_ERROR);
        }
        return ResponseDTO.successByMsg(true, "删除公告信息成功！");
    }
}
