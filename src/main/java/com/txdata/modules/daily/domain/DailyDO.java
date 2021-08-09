package com.txdata.modules.daily.domain;


import com.txdata.modules.dailyArrange.domain.DailyArrangeDO;
import com.txdata.modules.dailyProblem.domain.DailyProblemDO;
import com.txdata.modules.dailyTask.domain.DailyTaskDO;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 日报实体类
 */
public class DailyDO implements Serializable {

  private String id; // 日报id
  private String writeUser; // 填写人
  private Date writeDate; // 填写日期
  private String writeDept; // 所属部门
  private String writeJob; // 职务
  private String title; // 日报名称
  private String status; // 日报状态
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date reportDate; // 汇报时间
  private String filePath; // 附件上传路径
  private String delFlag = "0"; // 删除标识

  private String dailyArrangeDOS;
  private String dailyProblemDOS;
  private String dailyTaskDOS;

  private List<DailyArrangeDO> dailyArranges;
  private List<DailyProblemDO> dailyProblems;
  private List<DailyTaskDO> dailyTasks;
  private String procId; // 用于接收流程实例ID



  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getWriteUser() {
    return writeUser;
  }

  public void setWriteUser(String writeUser) {
    this.writeUser = writeUser;
  }


  public Date getWriteDate() {
    return writeDate;
  }

  public void setWriteDate(Date writeDate) {
    this.writeDate = writeDate;
  }


  public String getWriteDept() {
    return writeDept;
  }

  public void setWriteDept(String writeDept) {
    this.writeDept = writeDept;
  }


  public String getWriteJob() {
    return writeJob;
  }

  public void setWriteJob(String writeJob) {
    this.writeJob = writeJob;
  }


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  public Date getReportDate() {
    return reportDate;
  }

  public void setReportDate(Date reportDate) {
    this.reportDate = reportDate;
  }


  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public String getDelFlag() {
    return delFlag;
  }

  public void setDelFlag(String delFlag) {
    this.delFlag = delFlag;
  }

  public String getDailyArrangeDOS() {
    return dailyArrangeDOS;
  }

  public void setDailyArrangeDOS(String dailyArrangeDOS) {
    this.dailyArrangeDOS = dailyArrangeDOS;
  }

  public String getDailyProblemDOS() {
    return dailyProblemDOS;
  }

  public void setDailyProblemDOS(String dailyProblemDOS) {
    this.dailyProblemDOS = dailyProblemDOS;
  }

  public String getDailyTaskDOS() {
    return dailyTaskDOS;
  }

  public void setDailyTaskDOS(String dailyTaskDOS) {
    this.dailyTaskDOS = dailyTaskDOS;
  }

  public List<DailyArrangeDO> getDailyArranges() {
    return dailyArranges;
  }

  public void setDailyArranges(List<DailyArrangeDO> dailyArranges) {
    this.dailyArranges = dailyArranges;
  }

  public List<DailyProblemDO> getDailyProblems() {
    return dailyProblems;
  }

  public void setDailyProblems(List<DailyProblemDO> dailyProblems) {
    this.dailyProblems = dailyProblems;
  }

  public List<DailyTaskDO> getDailyTasks() {
    return dailyTasks;
  }

  public void setDailyTasks(List<DailyTaskDO> dailyTasks) {
    this.dailyTasks = dailyTasks;
  }

  public String getProcId() {
    return procId;
  }

  public void setProcId(String procId) {
    this.procId = procId;
  }

}
