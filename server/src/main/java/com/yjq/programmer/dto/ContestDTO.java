package com.yjq.programmer.dto;

import com.yjq.programmer.annotation.ValidateEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2023-04-21 14:46
 */
public class ContestDTO {

    private String id;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=128,minLength=1,errorRequiredMsg="竞赛题目不能为空！",errorMaxLengthMsg="竞赛题目长度不能大于128！",errorMinLengthMsg="竞赛题目不能为空！")
    private String title;

    private String file;

    @ValidateEntity(required=true,errorRequiredMsg="竞赛发布老师不能为空！")
    private String userId;

    private Date createTime;

    private Integer state;

    private String token;

    private UserDTO userDTO;

    @ValidateEntity(required=true,requiredMinValue=true,requiredMaxValue=true,maxValue=9999,minValue=0,errorRequiredMsg="入学年份要求不能为空！", errorMaxValueMsg="入学年份的要求不能大于9999！",errorMinValueMsg="入学年份的要求不能小于0！")
    private Integer year;

    private String fileName;

    @ValidateEntity(requiredMaxLength=true,maxLength=256,errorMaxLengthMsg="备注长度不能大于256！")
    private String remark;

    @ValidateEntity(required=true,requiredMinValue=true,requiredMaxValue=true,maxValue=999.99,minValue=0.00,errorRequiredMsg="学分不能为空！", errorMaxValueMsg="学分不能大于999.99！",errorMinValueMsg="学分不能小于0.00！")
    private BigDecimal credit;

    @ValidateEntity(required=true,requiredMinValue=true,requiredMaxValue=true,maxValue=999.99,minValue=0.00,errorRequiredMsg="获得学分要求的分数不能为空！", errorMaxValueMsg="获得学分要求的分数不能大于999.99！",errorMinValueMsg="获得学分要求的分数不能小于0.00！")
    private BigDecimal condition;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public BigDecimal getCondition() {
        return condition;
    }

    public void setCondition(BigDecimal condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", title=").append(title);
        sb.append(", file=").append(file);
        sb.append(", userId=").append(userId);
        sb.append(", createTime=").append(createTime);
        sb.append(", state=").append(state);
        sb.append(", token=").append(token);
        sb.append(", userDTO=").append(userDTO);
        sb.append(", year=").append(year);
        sb.append(", fileName=").append(fileName);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}
