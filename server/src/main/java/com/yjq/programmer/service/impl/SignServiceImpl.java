package com.yjq.programmer.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.ContestMapper;
import com.yjq.programmer.dao.SignMapper;
import com.yjq.programmer.dao.UserMapper;
import com.yjq.programmer.dao.WorkMapper;
import com.yjq.programmer.dao.my.MySignMapper;
import com.yjq.programmer.domain.*;
import com.yjq.programmer.dto.*;
import com.yjq.programmer.enums.ContestStateEnum;
import com.yjq.programmer.enums.PagingEnum;
import com.yjq.programmer.enums.RoleEnum;
import com.yjq.programmer.enums.SignStateEnum;
import com.yjq.programmer.service.ISignService;
import com.yjq.programmer.service.IUserService;
import com.yjq.programmer.service.IWorkService;
import com.yjq.programmer.util.CommonUtil;
import com.yjq.programmer.util.CopyUtil;
import com.yjq.programmer.util.UuidUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2023-04-22 8:10
 */
@Service
@Transactional
public class SignServiceImpl implements ISignService {

    @Resource
    private SignMapper signMapper;

    @Resource
    private ContestMapper contestMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private MySignMapper mySignMapper;

    @Resource
    private WorkMapper workMapper;

    @Resource
    private IWorkService workService;

    @Resource
    private IUserService userService;

    @Value("${yjq.upload.path}")
    private String uploadPhotoPath;//文件保存位置

    /**
     * 报名竞赛
     * @param signDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> signContest(SignDTO signDTO) {
        if(CommonUtil.isEmpty(signDTO.getContestId()) || CommonUtil.isEmpty(signDTO.getUserId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        Contest contest = contestMapper.selectByPrimaryKey(signDTO.getContestId());
        if(contest == null) {
            return ResponseDTO.errorByMsg(CodeMsg.CONTEST_NOT_EXIST);
        }
        // 判断是否报名结束
        if(!ContestStateEnum.SIGN.getCode().equals(contest.getState())) {
            return ResponseDTO.errorByMsg(CodeMsg.CONTEST_STATE_ERROR);
        }
        User user = userMapper.selectByPrimaryKey(signDTO.getUserId());
        if(user == null) {
            return ResponseDTO.errorByMsg(CodeMsg.USER_NOT_EXIST);
        }
        // 检查报名要求
        if(user.getYear() > contest.getYear()) {
            return ResponseDTO.errorByMsg(CodeMsg.CONTEST_CONDITION_ERROR);
        }
        // 判断是否已经报名过了
        SignExample signExample = new SignExample();
        signExample.createCriteria().andUserIdEqualTo(signDTO.getUserId()).andContestIdEqualTo(signDTO.getContestId());
        if(signMapper.selectByExample(signExample).size() > 0 ) {
            return ResponseDTO.errorByMsg(CodeMsg.SIGN_EXIST);
        }
        Sign sign = CopyUtil.copy(signDTO, Sign.class);
        sign.setId(UuidUtil.getShortUuid());
        sign.setCreateTime(new Date());
        if(signMapper.insertSelective(sign) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.SIGN_ADD_ERROR);
        }
        return ResponseDTO.successByMsg(true, "报名成功，请在报名列表留意自己报名情况！");
    }

    /**
     * 分页获取报名数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<SignDTO>> getSignList(PageDTO<SignDTO> pageDTO) {
        SignExample signExample = new SignExample();
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        // 不知道每页多少条记录，默认为每页5条记录
        if(pageDTO.getSize() == null){
            pageDTO.setSize(5);
        }
        SignExample.Criteria c1 = signExample.createCriteria();
        if(pageDTO.getParam() != null) {
            SignDTO signDTO = pageDTO.getParam();
            ResponseDTO<UserDTO> loginUser = userService.getLoginUser(signDTO.getToken());
            if(loginUser.getCode() != 0) {
                pageDTO.setTotal(0l);
                pageDTO.setList(new ArrayList<>());
                return ResponseDTO.success(pageDTO);
            }
            UserDTO loginUserDTO = loginUser.getData();


            if(RoleEnum.TEACHER.getCode().equals(loginUserDTO.getRoleId())) {
                // 老师用户只能看到自己竞赛的报名信息
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


            if(!CommonUtil.isEmpty(signDTO.getContestId())) {
                c1.andContestIdEqualTo(signDTO.getContestId());
            }
            if(signDTO.getState() != null) {
                c1.andStateEqualTo(signDTO.getState());
            }



            if(RoleEnum.STUDENT.getCode().equals(loginUserDTO.getRoleId())) {
                // 学生用户只能看到自己的信息
                c1.andUserIdEqualTo(loginUserDTO.getId());
            }

        }
        signExample.setOrderByClause("create_time asc");
        if(PagingEnum.YES.getCode().equals(pageDTO.getPaging())) {
            PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        }
        // 分页查出报名数据
        List<Sign> signList = signMapper.selectByExample(signExample);
        PageInfo<Sign> pageInfo = new PageInfo<>(signList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 将domain类型数据  转成 DTO类型数据
        List<SignDTO> signDTOList = CopyUtil.copyList(signList, SignDTO.class);
        for(SignDTO signDTO : signDTOList) {
            User user = userMapper.selectByPrimaryKey(signDTO.getUserId());
            if(user == null) {
                signDTO.setUserDTO(new UserDTO());
            } else {
                signDTO.setUserDTO(CopyUtil.copy(user, UserDTO.class));
            }
            Contest contest = contestMapper.selectByPrimaryKey(signDTO.getContestId());
            if(contest == null) {
                signDTO.setContestDTO(new ContestDTO());
            } else {
                signDTO.setContestDTO(CopyUtil.copy(contest, ContestDTO.class));
            }
        }
        pageDTO.setList(signDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 审核报名信息
     * @param signDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> editSign(SignDTO signDTO) {
        Sign sign = CopyUtil.copy(signDTO, Sign.class);
        if(signMapper.updateByPrimaryKey(sign) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.SIGN_EDIT_ERROR);
        }
        return ResponseDTO.successByMsg(true, "审核报名信息成功！");
    }

    /**
     * 删除报名信息
     * @param signDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> deleteSign(SignDTO signDTO) {
        if(CommonUtil.isEmpty(signDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        Sign sign = signMapper.selectByPrimaryKey(signDTO.getId());
        if(signMapper.deleteByPrimaryKey(signDTO.getId()) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.SIGN_DELETE_ERROR);
        }
        // 删除有关作品信息
        WorkExample workExample = new WorkExample();
        workExample.createCriteria().andUserIdEqualTo(sign.getUserId()).andContestIdEqualTo(sign.getContestId());
        List<Work> workList = workMapper.selectByExample(workExample);
        for(Work work : workList) {
            workService.deleteWork(CopyUtil.copy(work, WorkDTO.class));
        }
        return ResponseDTO.successByMsg(true, "删除报名信息成功！");
    }

    /**
     * 导出Excel文件
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<Map<String, Object>> exportExcel(PageDTO<SignDTO> pageDTO) {
        pageDTO.setPaging(PagingEnum.NO.getCode());
        ResponseDTO<PageDTO<SignDTO>> signList = getSignList(pageDTO);
        List<SignDTO> signDTOList = signList.getData().getList();
        for(SignDTO signDTO : signDTOList) {
            signDTO.setUsername(signDTO.getUserDTO().getUsername());
            signDTO.setNo(signDTO.getUserDTO().getNo());
            signDTO.setTitle(signDTO.getContestDTO().getTitle());
            if(SignStateEnum.WAIT.getCode().equals(signDTO.getState())) {
                signDTO.setStateName(SignStateEnum.WAIT.getDesc());
            } else if(SignStateEnum.SUCCESS.getCode().equals(signDTO.getState())) {
                signDTO.setStateName(SignStateEnum.SUCCESS.getDesc());
            } else if(SignStateEnum.FAIL.getCode().equals(signDTO.getState())) {
                signDTO.setStateName(SignStateEnum.FAIL.getDesc());
            }
        }
        // Excel配置
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("报名情况汇总表","报名情况汇总表"), SignDTO.class, signDTOList);
        try{
            Map<String, Object> responseMap = new HashMap<>();
            String savePath = uploadPhotoPath + CommonUtil.getFormatterDate(new Date(), "yyyyMMdd") + "\\";
            File savePathFile = new File(savePath);
            if(!savePathFile.exists()){
                //若不存在改目录，则创建目录
                savePathFile.mkdir();
            }
            String filename = new Date().getTime()+".xlsx";
            FileOutputStream outputStream = new FileOutputStream(savePath + filename);
            responseMap.put("fileName", "报名情况汇总表.xlsx");
            responseMap.put("filePath", CommonUtil.getFormatterDate(new Date(), "yyyyMMdd") + "/" + filename);
            workbook.write(outputStream);
            outputStream.close();
            workbook.close();
            return ResponseDTO.successByMsg(responseMap, "导出Excel成功！");
        }catch(Exception e){
            e.printStackTrace();
            return ResponseDTO.errorByMsg(CodeMsg.FILE_EXPORT_ERROR);
        }
    }

    /**
     * 根据时间范围获取报名人数
     * @return
     */
    @Override
    public ResponseDTO<List<Integer>> getSignCountByDate() {
        List<Integer> totalList = new ArrayList<>();
        Map<String, Object> queryMap = new HashMap<>();
        // 获取大大前天的报名人数
        queryMap.put("start", 4);
        queryMap.put("end", 3);
        totalList.add(mySignMapper.getSignTotalByDate(queryMap));
        // 获取大前天的报名人数
        queryMap.put("start", 3);
        queryMap.put("end", 2);
        totalList.add(mySignMapper.getSignTotalByDate(queryMap));
        // 获取前天的报名人数
        queryMap.put("start", 2);
        queryMap.put("end", 1);
        totalList.add(mySignMapper.getSignTotalByDate(queryMap));
        // 获取昨天的报名人数
        queryMap.put("start", 1);
        queryMap.put("end", 0);
        totalList.add(mySignMapper.getSignTotalByDate(queryMap));
        // 获取当天的报名人数
        queryMap.put("start", 0);
        queryMap.put("end", -1);
        totalList.add(mySignMapper.getSignTotalByDate(queryMap));
        return ResponseDTO.success(totalList);
    }
}
