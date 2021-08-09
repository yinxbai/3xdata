package com.txdata.system.dao;

import com.txdata.common.persistence.proxy.CrudDao;
import com.txdata.system.domain.DeptDO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * 部门管理(前端对接已弃用)
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-03 15:35:39
 */
@Mapper
public interface DeptDao extends CrudDao<DeptDO> {

	DeptDO get(String deptId);
	
	List<DeptDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(DeptDO dept);
	
	int update(DeptDO dept);
	
	int remove(String deptId);
	
	int batchRemove(String[] deptIds);

	String[] listParentDept();
	
	int getDeptUserNumber(String deptId);
}
