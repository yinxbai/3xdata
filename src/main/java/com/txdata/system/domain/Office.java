package com.txdata.system.domain;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import com.txdata.common.persistence.TreeEntity;
import com.txdata.common.utils.StringUtils;
import com.txdata.common.utils.excel.annotation.ExcelField;

/**
 * 机构Entity
 * 
 * @author ThinkGem
 * @version 2013-05-15
 */
public class Office extends TreeEntity<Office> {

	private static final long serialVersionUID = 1L;
//	private String id;
//	private Office parent;	// 父级编号
//	private String parentIds; // 所有父级编号
	private Area area; // 归属区域
	private String areaId;
	private String areaName; // 区域名称
	private String code; // 机构编码
	private String pcode; // 父级编码
//	private String name; 	// 机构名称
//	private Integer sort;		// 排序
	private String type; // 机构类型（1：公司；2：部门；3：小组）
	private String grade; // 机构等级（1：一级；2：二级；3：三级；4：四级）
	private String address; // 联系地址
	private String zipCode; // 邮政编码
	private String master; // 负责人
	private String phone; // 电话
	private String fax; // 传真
	private String email; // 邮箱
	private String useable;// 是否可用
	private String primaryPerson;// 主负责人
	private String deputyPerson;// 副负责人
	private List<String> childDeptList;// 快速添加子部门
	private String uscreditCode; // created by Zoe 2017-7-12 统一社会信用代码 18位 (现改为商户号，支付时使用)
	private String sealPath; // 公司印章图片地址
	private String szjcSealPath; // 水政监察图片地址
	private String sealName; // 公司印章图片文件名
	private String szjcSealName; // 水政监察图片文件名
//	private List<Office> children;
	private String leader; //分管领导
	private String principal; //部门负责人
	private String leaderName; //分管领导名称
	private String principalName; //机构负责人名称
	public Office(UserDO user) {
		this.id = user.getId();
		this.name = user.getName();
		this.type = "0"; // 用户还是机构，用户为0，机构为1
	}

	public Office(AjaxOffice ajaxOffice) {
		super(ajaxOffice.getId());
		Office parent = new Office(ajaxOffice.getParentId());
		this.parent = parent;
//        this.parentId = ajaxOffice.getParentId();
		Area area = new Area(ajaxOffice.getAreaId());
		this.area = area;
//        this.areaId = ajaxOffice.getAreaId();
		this.name = ajaxOffice.getName();
		this.code = ajaxOffice.getCode();
		this.useable = ajaxOffice.getUseable();
		this.uscreditCode = ajaxOffice.getUscreditCode();
		this.sealPath = ajaxOffice.getSealPath();
		this.szjcSealPath = ajaxOffice.getSzjcSealPath();
		// this.remarks = ajaxOffice.getRemarks();
		this.delFlag = DEL_FLAG_NORMAL;
		this.type = "2";
		this.leader = ajaxOffice.getLeader();
		this.principal = ajaxOffice.getPrincipal();
	}

	public Office() {
		super();
		this.sort = 30;
		this.type = "2";
	}

	public Office(String id) {
		super(id);
		this.sort = 30;
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getChildDeptList() {
		return childDeptList;
	}

	public void setChildDeptList(List<String> childDeptList) {
		this.childDeptList = childDeptList;
	}

	@ExcelField(title = "是否可用", align = 2, sort = 40, dictType = "yes_no")
	public String getUseable() {
		return useable;
	}

	public void setUseable(String useable) {
		this.useable = useable;
	}

	public String getPrimaryPerson() {
		return primaryPerson;
	}

	public void setPrimaryPerson(String primaryPerson) {
		this.primaryPerson = primaryPerson;
	}

	public String getDeputyPerson() {
		return deputyPerson;
	}

	public void setDeputyPerson(String deputyPerson) {
		this.deputyPerson = deputyPerson;
	}

//	@JsonBackReference
//	@NotNull
	@ExcelField(title = "上级机构", type = 0, align = 2, sort = 10)
	public Office getParent() {
		return parent;
	}

	public void setParent(Office parent) {
		this.parent = parent;
	}

	@Length(min = 1, max = 2000)
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	@NotNull
	@ExcelField(title = "归属地区", type = 0, align = 2, sort = 15)
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getAreaId() {
		return area != null ? area.getId() : "";
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@Length(min = 1, max = 100)
	@ExcelField(title = "机构名称", type = 0, align = 2, sort = 20)
	public String getName() {
		return name;
	}

//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public Integer getSort() {
//		return sort;
//	}
//
//	public void setSort(Integer sort) {
//		this.sort = sort;
//	}

	@Length(min = 1, max = 1)
	@ExcelField(title = "机构类型", align = 2, sort = 30, dictType = "sys_office_type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min = 1, max = 1)
	@ExcelField(title = "机构等级", align = 2, sort = 35, dictType = "sys_office_grade")
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Length(min = 0, max = 255)
	@ExcelField(title = "联系地址", align = 1, sort = 60)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Length(min = 0, max = 100)
	@ExcelField(title = "邮政编码", align = 1, sort = 65)
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Length(min = 0, max = 100)
	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	@Length(min = 0, max = 200)
	@ExcelField(title = "电话", align = 1, sort = 70)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min = 0, max = 200)
	@ExcelField(title = "传真", align = 1, sort = 75)
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Length(min = 0, max = 200)
	@ExcelField(title = "邮箱", align = 1, sort = 80)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Length(min = 0, max = 100)
	@ExcelField(title = "机构编码", align = 2, sort = 25)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

//	public String getParentId() {
//		return parent != null && parent.getId() != null ? parent.getId() : "0";
//	}

	public String getPcode() {
//		return parentCode;
		String pcode = "";
		if (parent != null) {
			pcode = parent.getCode();
		}
		return StringUtils.isNotBlank(pcode) ? pcode : "";
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	@ExcelField(title = "创建时间", type = 1, align = 1, sort = 90)
	public Date getCreateDate() {
		return createDate;
	}

	@ExcelField(title = "备注", align = 1, sort = 900)
	public String getRemarks() {
		return remarks;
	}

	@Override
	public String toString() {
		return name;
	}

	@Length(min = 0, max = 18)
	public String getUscreditCode() {
		return uscreditCode;
	}

	public void setUscreditCode(String uscreditCode) {
		this.uscreditCode = uscreditCode;
	}

//	public List<Office> getChildren() {
//		return children;
//	}
//
//	public void setChildren(List<Office> children) {
//		this.children = children;
//	}

	public String getSealPath() {
		return sealPath;
	}

	public void setSealPath(String sealPath) {
		this.sealPath = sealPath;
	}

	public String getSealName() {
		return sealName;
	}

	public void setSealName(String sealName) {
		this.sealName = sealName;
	}

	public String getSzjcSealPath() {
		return szjcSealPath;
	}

	public void setSzjcSealPath(String szjcSealPath) {
		this.szjcSealPath = szjcSealPath;
	}

	public String getSzjcSealName() {
		return szjcSealName;
	}

	public void setSzjcSealName(String szjcSealName) {
		this.szjcSealName = szjcSealName;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}

	public String getPrincipalName() {
		return principalName;
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

}