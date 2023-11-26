package com.yjq.programmer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.ContestMapper;
import com.yjq.programmer.dao.ResultMapper;
import com.yjq.programmer.dao.SignMapper;
import com.yjq.programmer.dao.UserMapper;
import com.yjq.programmer.dao.my.MyContestMapper;
import com.yjq.programmer.domain.*;
import com.yjq.programmer.dto.*;
import com.yjq.programmer.enums.ContestStateEnum;
import com.yjq.programmer.enums.RoleEnum;
import com.yjq.programmer.service.IContestService;
import com.yjq.programmer.service.ISignService;
import com.yjq.programmer.service.IUserService;
import com.yjq.programmer.util.CommonUtil;
import com.yjq.programmer.util.CopyUtil;
import com.yjq.programmer.util.UuidUtil;
import com.yjq.programmer.util.ValidateEntityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2023-04-21 14:47
 */
@Service
@Transactional
public class ContestServiceImpl implements IContestService {

    @Resource
    private ContestMapper contestMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ResultMapper resultMapper;

    @Resource
    private MyContestMapper myContestMapper;

    @Resource
    private SignMapper signMapper;

    @Resource
    private ISignService signService;

    @Resource
    private IUserService userService;

    /**
     * 分页获取竞赛数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<ContestDTO>> getContestList(PageDTO<ContestDTO> pageDTO) {
        ContestExample contestExample = new ContestExample();
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        // 不知道每页多少条记录，默认为每页5条记录
        if(pageDTO.getSize() == null){
            pageDTO.setSize(5);
        }
        ContestExample.Criteria c1 = contestExample.createCriteria();
        if(pageDTO.getParam() != null) {
            ContestDTO contestDTO = pageDTO.getParam();

            ResponseDTO<UserDTO> loginUser = userService.getLoginUser(contestDTO.getToken());
            if(loginUser.getCode() != 0) {
                pageDTO.setTotal(0l);
                pageDTO.setList(new ArrayList<>());
                return ResponseDTO.success(pageDTO);
            }
            UserDTO loginUserDTO = loginUser.getData();


            if(!CommonUtil.isEmpty(contestDTO.getTitle())) {
                c1.andTitleLike("%" + contestDTO.getTitle() + "%");
            }

            if(RoleEnum.TEACHER.getCode().equals(loginUserDTO.getRoleId())) {
                // 老师只能看到自己的竞赛信息
                c1.andUserIdEqualTo(loginUserDTO.getId());
            } else {
                if(!CommonUtil.isEmpty(contestDTO.getUserId())) {
                    c1.andUserIdEqualTo(contestDTO.getUserId());
                }
            }



        }
        contestExample.setOrderByClause("create_time desc");
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出竞赛数据
        List<Contest> contestList = contestMapper.selectByExample(contestExample);
        PageInfo<Contest> pageInfo = new PageInfo<>(contestList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 将domain类型数据  转成 DTO类型数据
        List<ContestDTO> contestDTOList = CopyUtil.copyList(contestList, ContestDTO.class);
        for(ContestDTO contestDTO : contestDTOList) {
            User user = userMapper.selectByPrimaryKey(contestDTO.getUserId());
            if(user == null) {
                contestDTO.setUserDTO(new UserDTO());
            } else {
                contestDTO.setUserDTO(CopyUtil.copy(user, UserDTO.class));
            }
        }
        pageDTO.setList(contestDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 保存竞赛信息
     * @param contestDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveContest(ContestDTO contestDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(contestDTO);
        if (!validate.getCode().equals(CodeMsg.SUCCESS.getCode())) {
            return ResponseDTO.errorByMsg(validate);
        }
        Contest contest = CopyUtil.copy(contestDTO, Contest.class);
        if(CommonUtil.isEmpty(contest.getId())) {
            // 添加操作
            contest.setId(UuidUtil.getShortUuid());
            contest.setCreateTime(new Date());
            if(contestMapper.insertSelective(contest) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.CONTEST_ADD_ERROR);
            }
        } else {
            // 修改操作
            if(contestMapper.updateByPrimaryKeySelective(contest) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.CONTEST_EDIT_ERROR);
            }
            if(ContestStateEnum.OVER.getCode().equals(contestDTO.getState())) {
                // 已结束的竞赛计算作品排名
                ResultExample resultExample = new ResultExample();
                resultExample.createCriteria().andContestIdEqualTo(contestDTO.getId());
                resultExample.setOrderByClause("score desc");
                List<Result> resultList = resultMapper.selectByExample(resultExample);
                int rank = 1;
                for(Result result : resultList) {
                    result.setRank(rank);
                    resultMapper.updateByPrimaryKeySelective(result);
                    rank++;
                }
            }
        }
        return ResponseDTO.successByMsg(true, "保存竞赛信息成功！");
    }

    /**
     * 删除竞赛信息
     * @param contestDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> deleteContest(ContestDTO contestDTO) {
        if(CommonUtil.isEmpty(contestDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 删除竞赛信息
        if(contestMapper.deleteByPrimaryKey(contestDTO.getId()) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.CONTEST_DELETE_ERROR);
        }
        // 删除和竞赛有关的报名信息
        SignExample signExample = new SignExample();
        signExample.createCriteria().andContestIdEqualTo(contestDTO.getId());
        List<Sign> signList = signMapper.selectByExample(signExample);
        for(Sign sign : signList) {
            signService.deleteSign(CopyUtil.copy(sign, SignDTO.class));
        }
        return ResponseDTO.successByMsg(true, "删除竞赛信息成功！");
    }

    /**
     * 获取所有竞赛信息
     * @param contestDTO
     * @return
     */
    @Override
    public ResponseDTO<List<Contest>> getAllContest(ContestDTO contestDTO) {
        List<Contest> contestList = contestMapper.selectByExample(new ContestExample());
        return ResponseDTO.success(contestList);
    }

    /**
     * 根据时间范围获取竞赛个数
     * @return
     */
    @Override
    public ResponseDTO<List<Integer>> getContestCountByDate() {
        List<Integer> totalList = new ArrayList<>();
        Map<String, Object> queryMap = new HashMap<>();
        // 获取大大前天的竞赛人数
        queryMap.put("start", 4);
        queryMap.put("end", 3);
        totalList.add(myContestMapper.getContestTotalByDate(queryMap));
        // 获取大前天的竞赛人数
        queryMap.put("start", 3);
        queryMap.put("end", 2);
        totalList.add(myContestMapper.getContestTotalByDate(queryMap));
        // 获取前天的竞赛人数
        queryMap.put("start", 2);
        queryMap.put("end", 1);
        totalList.add(myContestMapper.getContestTotalByDate(queryMap));
        // 获取昨天的竞赛人数
        queryMap.put("start", 1);
        queryMap.put("end", 0);
        totalList.add(myContestMapper.getContestTotalByDate(queryMap));
        // 获取当天的竞赛人数
        queryMap.put("start", 0);
        queryMap.put("end", -1);
        totalList.add(myContestMapper.getContestTotalByDate(queryMap));
        return ResponseDTO.success(totalList);
    }
}
