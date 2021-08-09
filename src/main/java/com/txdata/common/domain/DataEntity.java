package com.txdata.common.domain;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.txdata.common.utils.IdGen;
import com.txdata.common.utils.ShiroUtils;
import com.txdata.common.utils.StringUtils;
import com.txdata.system.domain.UserDO;
import com.fasterxml.jackson.annotation.JsonFormat;

public class DataEntity<T>  extends BaseEntity<T> {
    private static final long serialVersionUID = 1L;

    protected String remarks;	    // 备注
    protected String createBy;	// 创建者
    
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createDate;	    // 创建日期
    
    protected String updateBy;	// 更新者
    
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date updateDate;	    // 更新日期
    
    protected String delFlag; 	    // 删除标记（0：正常；1：删除；2：审核）
    
    /**
     * 删除标记（0：正常；1：删除；2：审核；）
     */
    public static final String DEL_FLAG_NORMAL = "0";
    public static final String DEL_FLAG_DELETE = "1";
    public static final String DEL_FLAG_AUDIT = "2";

	public DataEntity() {
		super();
		this.delFlag = DEL_FLAG_NORMAL;
	}
	
	public DataEntity(String id) {
		this();
		this.id = id;
	}

    public void preInsert(){
        if(StringUtils.isBlank(this.getId())){
            setId(IdGen.uuid());
        }
        UserDO user = ShiroUtils.getUser();
        if(user != null && StringUtils.isNotBlank(user.getId())){
            this.updateBy = user.getId();
            this.createBy = user.getId();
        }else{
            this.updateBy = "-1";
            this.createBy = "-1";
        }
        this.updateDate = new Date();
        this.createDate = this.updateDate;
    }

    public void preUpdate(){
        UserDO user = ShiroUtils.getUser();
        if(user != null && StringUtils.isNotBlank(user.getId())){
            this.updateBy = user.getId();
        }
        this.updateDate = new Date();
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

}
