package com.yjq.programmer.dto;

import com.yjq.programmer.annotation.ValidateEntity;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2022-06-09 10:37
 */
public class UserDTO {

    private String id;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=8,minLength=1,errorRequiredMsg="用户名称不能为空！",errorMaxLengthMsg="用户名称长度不能大于8！",errorMinLengthMsg="用户名称不能为空！")
    private String username;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=16,minLength=6,errorRequiredMsg="用户密码不能为空！",errorMaxLengthMsg="用户密码长度不能大于16！",errorMinLengthMsg="用户密码长度不能小于6！")
    private String password;

    private String headPic;

    private Integer roleId;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=32,minLength=1,errorRequiredMsg="学号/学工号不能为空！",errorMaxLengthMsg="学号/学工号长度不能大于32！",errorMinLengthMsg="学号/学工号不能为空！")
    private String no;

    @ValidateEntity(requiredMaxLength=true,maxLength=32,errorMaxLengthMsg="学院名称长度不能大于32！")
    private String collegeName;

    private Integer sex;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=11,minLength=11,errorRequiredMsg="手机号码不能为空！",errorMaxLengthMsg="请输入11位手机号码！",errorMinLengthMsg="请输入11位手机号码！")
    private String phone;

    private String token;

    @ValidateEntity(requiredMinValue=true,requiredMaxValue=true,maxValue=9999,minValue=0,errorMaxValueMsg="入学年份不能大于9999！",errorMinValueMsg="入学年份不能小于0！")
    private Integer year;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", username=").append(username);
        sb.append(", password=").append(password);
        sb.append(", headPic=").append(headPic);
        sb.append(", roleId=").append(roleId);
        sb.append(", no=").append(no);
        sb.append(", collegeName=").append(collegeName);
        sb.append(", sex=").append(sex);
        sb.append(", phone=").append(phone);
        sb.append(", token=").append(token);
        sb.append(", year=").append(year);
        sb.append("]");
        return sb.toString();
    }

}
