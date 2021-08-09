package com.txdata.activiti.vo;

import java.util.Date;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 工作流 流程实体类
 * 只给前端展示如下属性
 * @author mark
 *
 */
public class ProcessVO {
    private String id;
    
    private String name;		// 流程名称
    
    private String deploymentId;	// 流程部署ID
    
    private String key;		// 流程标识
    
    private int version;		// 流程版本
    
	private String resourceName;		// 流程XML名称
	
	private String diagramResourceName;	// 流程图片名称
	
	private String suspended;	// 是否已挂起
	
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date deploymentTime;		// 部署时间

    public ProcessVO(Deployment processDefinition) {
        this.setId(processDefinition.getId());
        this.name = processDefinition.getName();
    }

    public ProcessVO(ProcessDefinition processDefinition) {
        this.setId(processDefinition.getId());
        this.name = processDefinition.getName();
        this.deploymentId = processDefinition.getDeploymentId();
    }
    
    public ProcessVO(ProcessDefinition processDef, Deployment deployment) {
		this.id = processDef.getId();
		this.key = processDef.getKey();
		this.name = processDef.getName();
		this.version = processDef.getVersion();
		this.resourceName = processDef.getResourceName();
		this.diagramResourceName = processDef.getDiagramResourceName();
		this.suspended = processDef.isSuspended()?"1":"0";
		this.deploymentId = processDef.getDeploymentId();
		this.deploymentTime = deployment.getDeploymentTime();
    }

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

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getDiagramResourceName() {
		return diagramResourceName;
	}

	public void setDiagramResourceName(String diagramResourceName) {
		this.diagramResourceName = diagramResourceName;
	}

	public String getSuspended() {
		return suspended;
	}

	public void setSuspended(String suspended) {
		this.suspended = suspended;
	}

	public Date getDeploymentTime() {
		return deploymentTime;
	}

	public void setDeploymentTime(Date deploymentTime) {
		this.deploymentTime = deploymentTime;
	}
}
