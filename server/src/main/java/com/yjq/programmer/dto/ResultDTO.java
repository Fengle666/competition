package com.yjq.programmer.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.yjq.programmer.annotation.ValidateEntity;

import java.math.BigDecimal;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2023-04-22 17:49
 */
@ExcelTarget("Result")
public class ResultDTO {

    private String id;

    private String userId;

    private UserDTO userDTO;

    @Excel(name="学生名称",orderNum="1",width=20.0)
    private String username;

    @Excel(name="学号/学工号",orderNum="2",width=40.0)
    private String no;

    private String contestId;

    private ContestDTO contestDTO;

    @Excel(name="竞赛题目",orderNum="3",width=40.0)
    private String title;

    private String worksId;

    private WorkDTO workDTO;

    @Excel(name="作品名称",orderNum="4",width=40.0)
    private String name;

    @ValidateEntity(required=true,requiredMinValue=true,requiredMaxValue=true,maxValue=999.99,minValue=0.00,errorRequiredMsg="打分分数不能为空！", errorMaxValueMsg="打分分数不能大于999.99！",errorMinValueMsg="打分分数不能小于0.00！")
    @Excel(name="作品得分",orderNum="5",width=20.0)
    private BigDecimal score;

    @Excel(name="作品排名",orderNum="6",width=20.0)
    private Integer rank;

    @Excel(name="作品点评",orderNum="8",width=40.0)
    @ValidateEntity(requiredMaxLength=true,maxLength=256,errorMaxLengthMsg="点评长度不能大于256！")
    private String remark;

    @Excel(name="获得学分",orderNum="7",width=20.0)
    private BigDecimal credit;

    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContestId() {
        return contestId;
    }

    public void setContestId(String contestId) {
        this.contestId = contestId;
    }

    public String getWorksId() {
        return worksId;
    }

    public void setWorksId(String worksId) {
        this.worksId = worksId;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public ContestDTO getContestDTO() {
        return contestDTO;
    }

    public void setContestDTO(ContestDTO contestDTO) {
        this.contestDTO = contestDTO;
    }

    public WorkDTO getWorkDTO() {
        return workDTO;
    }

    public void setWorkDTO(WorkDTO workDTO) {
        this.workDTO = workDTO;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", contestId=").append(contestId);
        sb.append(", worksId=").append(worksId);
        sb.append(", score=").append(score);
        sb.append(", rank=").append(rank);
        sb.append(", userDTO=").append(userDTO);
        sb.append(", contestDTO=").append(contestDTO);
        sb.append(", workDTO=").append(workDTO);
        sb.append("]");
        return sb.toString();
    }
}
