package com.yjq.programmer.controller;

import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.SignDTO;
import com.yjq.programmer.service.ISignService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2023-04-22 8:09
 */
@RestController
@RequestMapping("/sign")
public class SignController {

    @Resource
    private ISignService signService;

    /**
     * 报名操作
     * @param signDTO
     * @return
     */
    @PostMapping("/add")
    public ResponseDTO<Boolean> signContest(@RequestBody SignDTO signDTO){
        return signService.signContest(signDTO);
    }

    /**
     * 审核报名信息
     * @param signDTO
     * @return
     */
    @PostMapping("/edit")
    public ResponseDTO<Boolean> editSign(@RequestBody SignDTO signDTO){
        return signService.editSign(signDTO);
    }

    /**
     * 删除报名信息
     * @param signDTO
     * @return
     */
    @PostMapping("/delete")
    public ResponseDTO<Boolean> deleteSign(@RequestBody SignDTO signDTO){
        return signService.deleteSign(signDTO);
    }


    /**
     * 分页获取报名数据
     * @param pageDTO
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<SignDTO>> getSignList(@RequestBody PageDTO<SignDTO> pageDTO){
        return signService.getSignList(pageDTO);
    }

    /**
     * 导出Excel文件
     * @param pageDTO
     * @return
     */
    @PostMapping("/export")
    public ResponseDTO<Map<String, Object>> exportExcel(@RequestBody PageDTO<SignDTO> pageDTO){
        return signService.exportExcel(pageDTO);
    }

    /**
     * 根据时间范围获取报名人数
     * @return
     */
    @PostMapping("/count-date")
    public ResponseDTO<List<Integer>> getSignCountByDate(){
        return signService.getSignCountByDate();
    }

}
