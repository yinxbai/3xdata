package com.txdata.modules.dailyArrange.domain;


import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class DailyArrangeDO {

  private String id; // id
  private String dailyId; // 日报id
  private String dailyTaskType; // 任务类型
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date finishDate; // 完成时间
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


  public Date getFinishDate() {
    return finishDate;
  }

  public void setFinishDate(Date finishDate) {
    this.finishDate = finishDate;
  }


  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public String getDelFlag() {
    return delFlag;
  }

  public void setDelFlag(String delFlag) {
    this.delFlag = delFlag;
  }
}
