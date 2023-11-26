package com.yjq.programmer.dto;

import com.yjq.programmer.annotation.ValidateEntity;

import java.util.Date;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2023-04-22 12:46
 */
public class WorkDTO {

    private String id;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=128,minLength=1,errorRequiredMsg="作品名称不能为空！",errorMaxLengthMsg="作品名称长度不能大于128！",errorMinLengthMsg="作品名称不能为空！")
    private String name;

    private String file;

    private String fileName;

    private String contestId;

    private String userId;

    private Date updateTime;

    private ContestDTO contestDTO;

    private UserDTO userDTO;

    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
        sb.append(", name=").append(name);
        sb.append(", file=").append(file);
        sb.append(", fileName=").append(fileName);
        sb.append(", contestId=").append(contestId);
        sb.append(", userId=").append(userId);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", userDTO=").append(userDTO);
        sb.append(", contestDTO=").append(contestDTO);
        sb.append("]");
        return sb.toString();
    }
}
