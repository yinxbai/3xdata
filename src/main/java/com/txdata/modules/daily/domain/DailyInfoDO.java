package com.txdata.modules.daily.domain;


import java.io.Serializable;
import java.util.Date;

/**
 * @author InRoota
 * @2021-08-06 10:41:14
 */
public class DailyInfoDO implements Serializable {
    // 实例化配置
    private static final long serialVersionUID = 1L;
    private String userId; // 用户名
    private String procId; // 用于接收流程实例ID
    private String job; // 用户职位
    private String taskId; // 任务id
    private String approver1; // 部门经理
    private String approver2; // 高层领导
    private String status; // 任务状态

    private String activityName; // 活动名称
    private Date startDate;	// 开始日期
    private String assigneeName; // 人物姓名
    private Date endDate; // 结束日期
    private String remark; // 备注
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProcId() {
        return procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getApprover1() {
        return approver1;
    }

    public void setApprover1(String approver1) {
        this.approver1 = approver1;
    }

    public String getApprover2() {
        return approver2;
    }

    public void setApprover2(String approver2) {
        this.approver2 = approver2;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
