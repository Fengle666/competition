package com.yjq.programmer.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Contest {
    private String id;

    private String title;

    private String file;

    private String userId;

    private Date createTime;

    private Integer state;

    private String fileName;

    private Integer year;

    private String remark;

    private BigDecimal credit;

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
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
        sb.append(", fileName=").append(fileName);
        sb.append(", year=").append(year);
        sb.append(", remark=").append(remark);
        sb.append(", credit=").append(credit);
        sb.append(", condition=").append(condition);
        sb.append("]");
        return sb.toString();
    }
}