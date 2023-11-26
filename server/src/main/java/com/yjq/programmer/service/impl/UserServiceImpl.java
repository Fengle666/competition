package com.yjq.programmer.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.*;
import com.yjq.programmer.domain.*;
import com.yjq.programmer.dto.*;
import com.yjq.programmer.enums.RoleEnum;
import com.yjq.programmer.service.IContestService;
import com.yjq.programmer.service.ISignService;
import com.yjq.programmer.service.IUserService;
import com.yjq.programmer.util.CommonUtil;
import com.yjq.programmer.util.CopyUtil;
import com.yjq.programmer.util.UuidUtil;
import com.yjq.programmer.util.ValidateEntityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2022-06-09 10:45
 */
@Service
@Transactional
public class UserServiceImpl implements IUserService {


    @Resource
    private UserMapper userMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private AnnounceMapper announceMapper;

    @Resource
    private ContestMapper contestMapper;

    @Resource
    private IContestService contestService;

    @Resource
    private ISignService signService;

    @Resource
    private SignMapper signMapper;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    /**
     * 分页获取用户数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<UserDTO>> getUserList(PageDTO<UserDTO> pageDTO) {
        UserExample userExample = new UserExample();
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        // 不知道每页多少条记录，默认为每页5条记录
        if(pageDTO.getSize() == null){
            pageDTO.setSize(5);
        }
        UserExample.Criteria c1 = userExample.createCriteria();
        if(pageDTO.getParam() != null) {
            UserDTO userDTO = pageDTO.getParam();
            if(!CommonUtil.isEmpty(userDTO.getNo())) {
                c1.andNoLike("%" + userDTO.getNo() + "%");
            }
            if(userDTO.getRoleId() != 0 && userDTO.getRoleId() != 0) {
                c1.andRoleIdEqualTo(userDTO.getRoleId());
            }
            ResponseDTO<UserDTO> loginUser = getLoginUser(userDTO.getToken());
            if(loginUser.getCode() != 0) {
                pageDTO.setTotal(0l);
                pageDTO.setList(new ArrayList<>());
                return ResponseDTO.success(pageDTO);
            }
            UserDTO loginUserDTO = loginUser.getData();
            if(RoleEnum.STUDENT.getCode().equals(loginUserDTO.getRoleId()) && RoleEnum.STUDENT.getCode().equals(userDTO.getRoleId())) {
                // 学生用户只能看到自己的信息
                c1.andIdEqualTo(loginUserDTO.getId());
            }
        }
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出用户数据
        List<User> userList = userMapper.selectByExample(userExample);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 将domain类型数据  转成 DTO类型数据
        List<UserDTO> userDTOList = CopyUtil.copyList(userList, UserDTO.class);
        pageDTO.setList(userDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 保存用户信息
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveUser(UserDTO userDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(userDTO);
        if (!validate.getCode().equals(CodeMsg.SUCCESS.getCode())) {
            return ResponseDTO.errorByMsg(validate);
        }
        User user = CopyUtil.copy(userDTO, User.class);
        if(CommonUtil.isEmpty(user.getId())) {
            // 添加操作
            // 判断学号/学工号是否存在
            if(isNoExist(user, "")){
                return ResponseDTO.errorByMsg(CodeMsg.USER_NO_EXIST);
            }
            user.setId(UuidUtil.getShortUuid());
            if(userMapper.insertSelective(user) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.USER_ADD_ERROR);
            }
        } else {
            // 修改操作
            // 判断学号/学工号是否存在
            if(isNoExist(user, user.getId())){
                return ResponseDTO.errorByMsg(CodeMsg.USER_NO_EXIST);
            }
            ResponseDTO<UserDTO> loginUser = getLoginUser(userDTO.getToken());
            if(loginUser.getCode() != 0) {
                return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
            }
            if(userMapper.updateByPrimaryKeySelective(user) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.USER_EDIT_ERROR);
            }
            UserDTO loginUserDTO = loginUser.getData();
            if(user.getId().equals(loginUserDTO.getId())) {
                // 如果是修改个人信息  则更新redis中数据
                loginUserDTO = CopyUtil.copy(userMapper.selectByPrimaryKey(user.getId()), UserDTO.class);
                loginUserDTO.setToken(userDTO.getToken());
                stringRedisTemplate.opsForValue().set("USER_" + userDTO.getToken(), JSON.toJSONString(loginUserDTO), 3600, TimeUnit.SECONDS);
            }
        }
        return ResponseDTO.successByMsg(true, "保存用户信息成功！");
    }

    /**
     * 删除用户信息
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> deleteUser(UserDTO userDTO) {
        if(CommonUtil.isEmpty(userDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 删除用户信息
        if(userMapper.deleteByPrimaryKey(userDTO.getId()) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.USER_DELETE_ERROR);
        }
        // 删除用户有关的评论信息
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andUserIdEqualTo(userDTO.getId());
        commentMapper.deleteByExample(commentExample);
        // 删除用户有关的公告信息
        AnnounceExample announceExample = new AnnounceExample();
        announceExample.createCriteria().andUserIdEqualTo(userDTO.getId());
        announceMapper.deleteByExample(announceExample);
        // 删除该用户发布的竞赛信息
        ContestExample contestExample = new ContestExample();
        contestExample.createCriteria().andUserIdEqualTo(userDTO.getId());
        List<Contest> contestList = contestMapper.selectByExample(contestExample);
        for(Contest contest : contestList) {
            contestService.deleteContest(CopyUtil.copy(contest, ContestDTO.class));
        }
        // 删除该用户的报名信息
        SignExample signExample = new SignExample();
        signExample.createCriteria().andUserIdEqualTo(userDTO.getId());
        List<Sign> signList = signMapper.selectByExample(signExample);
        for(Sign sign : signList) {
            signService.deleteSign(CopyUtil.copy(sign, SignDTO.class));
        }
        return ResponseDTO.successByMsg(true, "删除用户信息成功！");
    }


    /**
     * 用户登录操作
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<UserDTO> loginUser(UserDTO userDTO) {
        // 进行是否为空判断
        if(CommonUtil.isEmpty(userDTO.getNo())){
            return ResponseDTO.errorByMsg(CodeMsg.USER_NO_EMPTY);
        }
        if(CommonUtil.isEmpty(userDTO.getPassword())){
            return ResponseDTO.errorByMsg(CodeMsg.PASSWORD_EMPTY);
        }
        // 对比昵称和密码是否正确
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNoEqualTo(userDTO.getNo()).andPasswordEqualTo(userDTO.getPassword()).andRoleIdEqualTo(userDTO.getRoleId());
        List<User> userList = userMapper.selectByExample(userExample);
        if(userList == null || userList.size() != 1){
            return ResponseDTO.errorByMsg(CodeMsg.USERNAME_PASSWORD_ERROR);
        }
        // 生成登录token并存入Redis中
        UserDTO selectedUserDto = CopyUtil.copy(userList.get(0), UserDTO.class);
        String token = UuidUtil.getShortUuid();
        selectedUserDto.setToken(token);
        //把token存入redis中 有效期1小时
        stringRedisTemplate.opsForValue().set("USER_" + token, JSON.toJSONString(selectedUserDto), 3600, TimeUnit.SECONDS);
        return ResponseDTO.successByMsg(selectedUserDto, "登录成功！");
    }

    /**
     * 检查用户是否登录
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<UserDTO> checkLogin(UserDTO userDTO) {
        if(userDTO == null || CommonUtil.isEmpty(userDTO.getToken())){
            return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        ResponseDTO<UserDTO> responseDTO = getLoginUser(userDTO.getToken());
        if(responseDTO.getCode() != 0){
            return responseDTO;
        }
        logger.info("获取到的登录信息={}", responseDTO.getData());
        return ResponseDTO.success(responseDTO.getData());
    }

    /**
     * 退出登录操作
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> logout(UserDTO userDTO) {
        if(!CommonUtil.isEmpty(userDTO.getToken())){
            // token不为空  清除redis中数据
            stringRedisTemplate.delete("USER_" + userDTO.getToken());
        }
        return ResponseDTO.successByMsg(true, "退出登录成功！");
    }

    /**
     * 获取当前登录用户
     * @return
     */
    public ResponseDTO<UserDTO> getLoginUser(String token){
        String value = stringRedisTemplate.opsForValue().get("USER_" + token);
        if(CommonUtil.isEmpty(value)){
            return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        UserDTO selectedUserDTO = JSON.parseObject(value, UserDTO.class);
        return ResponseDTO.success(selectedUserDTO);
    }

    /**
     * 根据条件搜索用户数据
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<List<UserDTO>> getBySearch(UserDTO userDTO) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if(userDTO.getRoleId() != null) {
            criteria.andRoleIdEqualTo(userDTO.getRoleId());
        }
        List<User> userList = userMapper.selectByExample(userExample);
        List<UserDTO> userDTOList = CopyUtil.copyList(userList, UserDTO.class);
        return ResponseDTO.success(userDTOList);
    }


    /**
     * 判断学号/学工号是否重复
     * @param user
     * @param id
     * @return
     */
    public Boolean isNoExist(User user, String id) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNoEqualTo(user.getNo());
        List<User> selectedUserList = userMapper.selectByExample(userExample);
        if(selectedUserList != null && selectedUserList.size() > 0) {
            if(selectedUserList.size() > 1){
                return true; //出现重名
            }
            if(!selectedUserList.get(0).getId().equals(id)) {
                return true; //出现重名
            }
        }
        return false;//没有重名
    }
}
