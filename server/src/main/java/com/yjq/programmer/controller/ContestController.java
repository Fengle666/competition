package com.yjq.programmer.controller;

import com.yjq.programmer.domain.Contest;
import com.yjq.programmer.dto.ContestDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.IContestService;
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
 * @create 2023-04-21 14:46
 */
@RestController
@RequestMapping("/contest")
public class ContestController {

    @Resource
    private IContestService contestService;

    /**
     * 分页获取竞赛数据
     * @param pageDTO
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<ContestDTO>> getContestList(@RequestBody PageDTO<ContestDTO> pageDTO){
        return contestService.getContestList(pageDTO);
    }

    /**
     * 保存竞赛信息
     * @param contestDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO<Boolean> saveContest(@RequestBody ContestDTO contestDTO){
        return contestService.saveContest(contestDTO);
    }

    /**
     * 获取所有竞赛信息
     * @param contestDTO
     * @return
     */
    @PostMapping("/all")
    public ResponseDTO<List<Contest>> getAllContest(@RequestBody ContestDTO contestDTO){
        return contestService.getAllContest(contestDTO);
    }


    /**
     * 删除竞赛信息
     * @param contestDTO
     * @return
     */
    @PostMapping("/delete")
    public ResponseDTO<Boolean> deleteContest(@RequestBody ContestDTO contestDTO){
        return contestService.deleteContest(contestDTO);
    }

    /**
     * 根据时间范围获取竞赛个数
     * @return
     */
    @PostMapping("/count-date")
    public ResponseDTO<List<Integer>> getContestCountByDate(){
        return contestService.getContestCountByDate();
    }

}
