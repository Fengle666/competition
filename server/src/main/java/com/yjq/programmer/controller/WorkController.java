package com.yjq.programmer.controller;

import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.WorkDTO;
import com.yjq.programmer.service.IWorkService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2023-04-22 12:42
 */
@RestController
@RequestMapping("/work")
public class WorkController {

    @Resource
    private IWorkService workService;

    /**
     * 保存作品信息
     * @param workDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO<Boolean> saveWork(@RequestBody WorkDTO workDTO){
        return workService.saveWork(workDTO);
    }

    /**
     * 分页获取作品数据
     * @param pageDTO
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<WorkDTO>> getWorkList(@RequestBody PageDTO<WorkDTO> pageDTO){
        return workService.getWorkList(pageDTO);
    }

    /**
     * 获取所有作品信息
     * @param workDTO
     * @return
     */
    @PostMapping("/all")
    public ResponseDTO<List<WorkDTO>> getAllWork(@RequestBody WorkDTO workDTO){
        return workService.getAllWork(workDTO);
    }

    /**
     * 删除作品信息
     * @param workDTO
     * @return
     */
    @PostMapping("/delete")
    public ResponseDTO<Boolean> deleteWork(@RequestBody WorkDTO workDTO){
        return workService.deleteWork(workDTO);
    }

}
