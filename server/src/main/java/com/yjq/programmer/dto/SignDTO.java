package com.yjq.programmer.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.yjq.programmer.annotation.ValidateEntity;

import java.util.Date;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2023-04-22 8:09
 */
@ExcelTarget("Sign")
public class SignDTO {

    private String id;

    private String contestId;

    private String userId;

    @Excel(name="报名时间",orderNum="5",width=40.0, format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Integer state;

    @ValidateEntity(requiredMaxLength=true,maxLength=256,errorMaxLengthMsg="备注长度不能大于256！")
    @Excel(name="备注",orderNum="6",width=40.0)
    private String remark;

    private ContestDTO contestDTO;

    private UserDTO userDTO;

    @Excel(name="学生名称",orderNum="2",width=20.0)
    private String username;

    @Excel(name="学号/学工号",orderNum="1",width=40.0)
    private String no;

    @Excel(name="竞赛题目",orderNum="3",width=40.0)
    private String title;

    @Excel(name="状态",orderNum="4",width=20.0)
    private String stateName;

    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContestId() {
        return contestId;
    }

    public void setContestId(String contestId) {
        this.contestId = contestId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ContestDTO getContestDTO() {
        return contestDTO;
    }

    public void setContestDTO(ContestDTO contestDTO) {
        this.contestDTO = contestDTO;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
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
        sb.append(", contestId=").append(contestId);
        sb.append(", userId=").append(userId);
        sb.append(", createTime=").append(createTime);
        sb.append(", state=").append(state);
        sb.append(", remark=").append(remark);
        sb.append(", contestDTO=").append(contestDTO);
        sb.append(", userDTO=").append(userDTO);
        sb.append("]");
        return sb.toString();
    }
}
