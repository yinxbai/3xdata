package com.txdata.modules.dailyTask.domain;


import com.txdata.modules.daily.domain.DailyDO;

public class DailyTaskDO {

  private String id; // 日报明细id
  private String dailyId; // 日报id
  private String dailyTaskType; // 日报类型
  private String projectId; //项目Id
  private String projectName; // 项目名称
  private double usetime; // 使用时间
  private long percentComplete; // 完成度
  private String remarks; // 描述
  private String delFlag = "0"; // 删除标识



    public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getDailyId() {
    return dailyId;
  }

  public void setDailyId(String dailyId) {
    this.dailyId = dailyId;
  }


  public String getDailyTaskType() {
    return dailyTaskType;
  }

  public void setDailyTaskType(String dailyTaskType) {
    this.dailyTaskType = dailyTaskType;
  }

  public double getUsetime() {
    return usetime;
  }

  public void setUsetime(double usetime) {
    this.usetime = usetime;
  }


  public long getPercentComplete() {
    return percentComplete;
  }

  public void setPercentComplete(long percentComplete) {
    this.percentComplete = percentComplete;
  }


  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public String getProjectId() {
    return projectId;
  }

  public void setProjectId(String projectId) {
    this.projectId = projectId;
  }

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public String getDelFlag() {
    return delFlag;
  }

  public void setDelFlag(String delFlag) {
    this.delFlag = delFlag;
  }
}
