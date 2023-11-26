package com.yjq.programmer.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
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
import com.yjq.programmer.enums.PagingEnum;
import com.yjq.programmer.enums.RoleEnum;
import com.yjq.programmer.service.IResultService;
import com.yjq.programmer.service.IUserService;
import com.yjq.programmer.util.CommonUtil;
import com.yjq.programmer.util.CopyUtil;
import com.yjq.programmer.util.UuidUtil;
import com.yjq.programmer.util.ValidateEntityUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2023-04-22 20:03
 */
@Service
@Transactional
public class ResultServiceImpl implements IResultService {

    @Resource
    private ResultMapper resultMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ContestMapper contestMapper;

    @Resource
    private WorkMapper workMapper;

    @Resource
    private IUserService userService;

    @Value("${yjq.upload.path}")
    private String uploadPhotoPath;//文件保存位置

    /**
     * 保存成绩信息
     * @param resultDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveResult(ResultDTO resultDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(resultDTO);
        if (!validate.getCode().equals(CodeMsg.SUCCESS.getCode())) {
            return ResponseDTO.errorByMsg(validate);
        }
        Result result = CopyUtil.copy(resultDTO, Result.class);
        Contest contest = contestMapper.selectByPrimaryKey(resultDTO.getContestId());
        if(ContestStateEnum.OVER.getCode().equals(contest.getState())) {
            return ResponseDTO.errorByMsg(CodeMsg.RESULT_IS_OVER);
        }
        if(CommonUtil.isEmpty(result.getId())) {
            // 添加操作
            // 判断是否已经打分过
            ResultExample resultExample = new ResultExample();
            resultExample.createCriteria().andWorksIdEqualTo(resultDTO.getWorksId());
            if(resultMapper.selectByExample(resultExample).size() > 0) {
                return ResponseDTO.errorByMsg(CodeMsg.RESULT_ALREADY_EXIST);
            }
            result.setId(UuidUtil.getShortUuid());
            if(resultMapper.insertSelective(result) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.RESULT_ADD_ERROR);
            }
            return ResponseDTO.successByMsg(true, "打分成功，可以前往成绩列表查看！");
        } else {
            // 修改操作
            if(resultMapper.updateByPrimaryKeySelective(result) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.RESULT_EDIT_ERROR);
            }
            return ResponseDTO.successByMsg(true, "保存成绩信息成功！");
        }
    }

    /**
     * 分页获取成绩数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<ResultDTO>> getResultList(PageDTO<ResultDTO> pageDTO) {
        ResultExample resultExample = new ResultExample();
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        // 不知道每页多少条记录，默认为每页5条记录
        if(pageDTO.getSize() == null){
            pageDTO.setSize(5);
        }
        ResultExample.Criteria c1 = resultExample.createCriteria();
        if(pageDTO.getParam() != null) {
            ResultDTO resultDTO = pageDTO.getParam();
            ResponseDTO<UserDTO> loginUser = userService.getLoginUser(resultDTO.getToken());
            if(loginUser.getCode() != 0) {
                pageDTO.setTotal(0l);
                pageDTO.setList(new ArrayList<>());
                return ResponseDTO.success(pageDTO);
            }
            UserDTO loginUserDTO = loginUser.getData();

            if(RoleEnum.TEACHER.getCode().equals(loginUserDTO.getRoleId())) {
                // 老师用户只能看到自己竞赛的成绩信息
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


            if(!CommonUtil.isEmpty(resultDTO.getContestId())) {
                c1.andContestIdEqualTo(resultDTO.getContestId());
            }
            if(!CommonUtil.isEmpty(resultDTO.getUserId())) {
                c1.andUserIdEqualTo(resultDTO.getUserId());
            }
            if(!CommonUtil.isEmpty(resultDTO.getWorksId())) {
                c1.andWorksIdEqualTo(resultDTO.getWorksId());
            }


            if(RoleEnum.STUDENT.getCode().equals(loginUserDTO.getRoleId())) {
                // 学生用户只能看到自己的信息
                c1.andUserIdEqualTo(loginUserDTO.getId());
            }
        }
        if(PagingEnum.YES.getCode().equals(pageDTO.getPaging())) {
            PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        }
        // 分页查出成绩数据
        List<Result> resultList = resultMapper.selectByExample(resultExample);
        PageInfo<Result> pageInfo = new PageInfo<>(resultList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 将domain类型数据  转成 DTO类型数据
        List<ResultDTO> resultDTOList = CopyUtil.copyList(resultList, ResultDTO.class);
        for(ResultDTO resultDTO : resultDTOList) {
            User user = userMapper.selectByPrimaryKey(resultDTO.getUserId());
            if(user == null) {
                resultDTO.setUserDTO(new UserDTO());
            } else {
                resultDTO.setUserDTO(CopyUtil.copy(user, UserDTO.class));
            }
            Contest contest = contestMapper.selectByPrimaryKey(resultDTO.getContestId());
            if(contest == null) {
                resultDTO.setContestDTO(new ContestDTO());
                resultDTO.setCredit(new BigDecimal("0"));
            } else {
                resultDTO.setContestDTO(CopyUtil.copy(contest, ContestDTO.class));
                if(resultDTO.getScore().compareTo(resultDTO.getContestDTO().getCondition()) >= 0) {
                    resultDTO.setCredit(resultDTO.getContestDTO().getCredit());
                } else {
                    resultDTO.setCredit(new BigDecimal("0"));
                }
            }
            Work work = workMapper.selectByPrimaryKey(resultDTO.getWorksId());
            if(work == null) {
                resultDTO.setWorkDTO(new WorkDTO());
            } else {
                resultDTO.setWorkDTO(CopyUtil.copy(work, WorkDTO.class));
            }
        }
        pageDTO.setList(resultDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 导出Excel文件
     * @param pageDTO
     */
    @Override
    public ResponseDTO<Map<String, Object>> exportExcel(PageDTO<ResultDTO> pageDTO) {
        pageDTO.setPaging(PagingEnum.NO.getCode());
        ResponseDTO<PageDTO<ResultDTO>> resultList = getResultList(pageDTO);
        List<ResultDTO> resultDTOList = resultList.getData().getList();
        for(ResultDTO resultDTO : resultDTOList) {
            resultDTO.setUsername(resultDTO.getUserDTO().getUsername());
            resultDTO.setNo(resultDTO.getUserDTO().getNo());
            resultDTO.setTitle(resultDTO.getContestDTO().getTitle());
            resultDTO.setName(resultDTO.getWorkDTO().getName());
        }
        // Excel配置
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("竞赛成绩汇总表","竞赛成绩汇总表"), ResultDTO.class, resultDTOList);
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
            responseMap.put("fileName", "竞赛成绩汇总表.xlsx");
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
}
