package com.yjq.programmer.controller;

import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.ResultDTO;
import com.yjq.programmer.service.IResultService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2023-04-22 20:04
 */
@RestController
@RequestMapping("/result")
public class ResultController {

    @Resource
    private IResultService resultService;

    /**
     * 保存成绩信息
     * @param resultDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO<Boolean> saveResult(@RequestBody ResultDTO resultDTO){
        return resultService.saveResult(resultDTO);
    }

    /**
     * 分页获取成绩数据
     * @param pageDTO
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<ResultDTO>> getResultList(@RequestBody PageDTO<ResultDTO> pageDTO){
        return resultService.getResultList(pageDTO);
    }

    /**
     * 导出Excel文件
     * @param pageDTO
     * @return
     */
    @PostMapping("/export")
    public ResponseDTO<Map<String, Object>> exportExcel(@RequestBody PageDTO<ResultDTO> pageDTO){
        return resultService.exportExcel(pageDTO);
    }

}
