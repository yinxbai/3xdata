package com.txdata.modules.dailyProblem.domain;


import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class DailyProblemDO {

  private String id; // 项目问题id
  private String problem; // 项目问题
  private String projectName; // 项目名称
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date expectedTime; // 期望时间
  private String support; // 支持
  private String remarks; // 描述
  private String dailyId; // 日报id
  private String delFlag = "0"; // 删除标识

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getProblem() {
    return problem;
  }

  public void setProblem(String problem) {
    this.problem = problem;
  }


  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }


  public Date getExpectedTime() {
    return expectedTime;
  }

  public void setExpectedTime(Date expectedTime) {
    this.expectedTime = expectedTime;
  }


  public String getSupport() {
    return support;
  }

  public void setSupport(String support) {
    this.support = support;
  }


  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }


  public String getDailyId() {
    return dailyId;
  }

  public void setDailyId(String dailyId) {
    this.dailyId = dailyId;
  }

  public String getDelFlag() {
    return delFlag;
  }

  public void setDelFlag(String delFlag) {
    this.delFlag = delFlag;
  }
}
