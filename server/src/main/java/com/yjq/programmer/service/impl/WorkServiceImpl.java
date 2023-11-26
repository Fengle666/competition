package com.yjq.programmer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.ContestMapper;
import com.yjq.programmer.dao.ResultMapper;
import com.yjq.programmer.dao.UserMapper;
import com.yjq.programmer.dao.WorkMapper;
import com.yjq.programmer.domain.*;
import com.yjq.programmer.dto.*;
import com.yjq.programmer.enums.ContestStateEnum;
import com.yjq.programmer.enums.RoleEnum;
import com.yjq.programmer.service.IUserService;
import com.yjq.programmer.service.IWorkService;
import com.yjq.programmer.util.CommonUtil;
import com.yjq.programmer.util.CopyUtil;
import com.yjq.programmer.util.UuidUtil;
import com.yjq.programmer.util.ValidateEntityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2023-04-22 12:43
 */
@Service
@Transactional
public class WorkServiceImpl implements IWorkService {

    @Resource
    private WorkMapper workMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ContestMapper contestMapper;

    @Resource
    private ResultMapper resultMapper;

    @Resource
    private IUserService userService;

    /**
     * 保存作品信息
     * @param workDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveWork(WorkDTO workDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(workDTO);
        if (!validate.getCode().equals(CodeMsg.SUCCESS.getCode())) {
            return ResponseDTO.errorByMsg(validate);
        }
        Work work = CopyUtil.copy(workDTO, Work.class);
        work.setUpdateTime(new Date());
        Contest contest = contestMapper.selectByPrimaryKey(workDTO.getContestId());
        if(ContestStateEnum.OVER.getCode().equals(contest.getState())) {
            return ResponseDTO.errorByMsg(CodeMsg.WORK_IS_OVER);
        }
        if(CommonUtil.isEmpty(work.getId())) {
            // 添加操作
            // 判断是否上传过
            WorkExample workExample = new WorkExample();
            workExample.createCriteria().andContestIdEqualTo(work.getContestId()).andUserIdEqualTo(work.getUserId());
            if(workMapper.selectByExample(workExample).size() > 0) {
                return ResponseDTO.errorByMsg(CodeMsg.WORK_ALREADY_UPLOAD);
            }
            work.setId(UuidUtil.getShortUuid());
            if(workMapper.insertSelective(work) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.WORK_ADD_ERROR);
            }
        } else {
            // 修改操作
            if(workMapper.updateByPrimaryKeySelective(work) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.WORK_EDIT_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "上传作品成功，请在作品列表查看您的上传记录！");
    }

    /**
     * 分页获取作品数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<WorkDTO>> getWorkList(PageDTO<WorkDTO> pageDTO) {
        WorkExample workExample = new WorkExample();
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        // 不知道每页多少条记录，默认为每页5条记录
        if(pageDTO.getSize() == null){
            pageDTO.setSize(5);
        }
        WorkExample.Criteria c1 = workExample.createCriteria();
        if(pageDTO.getParam() != null) {
            WorkDTO workDTO = pageDTO.getParam();
            ResponseDTO<UserDTO> loginUser = userService.getLoginUser(workDTO.getToken());
            if(loginUser.getCode() != 0) {
                pageDTO.setTotal(0l);
                pageDTO.setList(new ArrayList<>());
                return ResponseDTO.success(pageDTO);
            }
            UserDTO loginUserDTO = loginUser.getData();

            if(RoleEnum.TEACHER.getCode().equals(loginUserDTO.getRoleId())) {
                // 老师用户只能看到自己竞赛的作品信息
                ContestExample contestExample = new ContestExample();
                contestExample.createCriteria().andUserIdEqualTo(loginUserDTO.getId());
                List<String> contestIdList = contestMapper.selectByExample(contestExample).stream().map(Contest::getId).collect(Collectors.toList());
                if(contestIdList.size() > 0) {
                    c1.andContestIdIn(contestIdList);
                } else {
                    PageInfo<Sign> pageInfo = new PageInfo<>(new ArrayList<>());
                    // 获取数据的总数
                    pageDTO.setTotal(pageInfo.getTotal());
                    pageDTO.setList(new ArrayList<>());
                    return ResponseDTO.success(pageDTO);
                }
            }


            if(!CommonUtil.isEmpty(workDTO.getContestId())) {
                c1.andContestIdEqualTo(workDTO.getContestId());
            }


            if(RoleEnum.STUDENT.getCode().equals(loginUserDTO.getRoleId())) {
                // 学生用户只能看到自己的信息
                c1.andUserIdEqualTo(loginUserDTO.getId());
            }
        }
        workExample.setOrderByClause("update_time desc");
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出作品数据
        List<Work> workList = workMapper.selectByExample(workExample);
        PageInfo<Work> pageInfo = new PageInfo<>(workList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 将domain类型数据  转成 DTO类型数据
        List<WorkDTO> workDTOList = CopyUtil.copyList(workList, WorkDTO.class);
        for(WorkDTO workDTO : workDTOList) {
            User user = userMapper.selectByPrimaryKey(workDTO.getUserId());
            if(user == null) {
                workDTO.setUserDTO(new UserDTO());
            } else {
                workDTO.setUserDTO(CopyUtil.copy(user, UserDTO.class));
            }
            Contest contest = contestMapper.selectByPrimaryKey(workDTO.getContestId());
            if(contest == null) {
                workDTO.setContestDTO(new ContestDTO());
            } else {
                workDTO.setContestDTO(CopyUtil.copy(contest, ContestDTO.class));
            }
        }
        pageDTO.setList(workDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 获取所有作品信息
     * @param workDTO
     * @return
     */
    @Override
    public ResponseDTO<List<WorkDTO>> getAllWork(WorkDTO workDTO) {
        WorkExample workExample = new WorkExample();
        List<Work> workList = workMapper.selectByExample(workExample);
        List<WorkDTO> workDTOList = CopyUtil.copyList(workList, WorkDTO.class);
        return ResponseDTO.success(workDTOList);
    }

    /**
     * 删除作品信息
     * @param workDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> deleteWork(WorkDTO workDTO) {
        if(CommonUtil.isEmpty(workDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        if(workMapper.deleteByPrimaryKey(workDTO.getId()) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.WORK_DELETE_ERROR);
        }
        // 删除成绩信息
        ResultExample resultExample = new ResultExample();
        resultExample.createCriteria().andWorksIdEqualTo(workDTO.getId());
        resultMapper.deleteByExample(resultExample);
        return ResponseDTO.successByMsg(true, "删除作品信息成功！");
    }
}
