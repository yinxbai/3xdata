package com.txdata.system.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.txdata.common.persistence.proxy.CrudDao;
import com.txdata.system.domain.InterfaceInfo;
import com.txdata.system.domain.InterfaceParam;

/**
 * 接口管理DAO接口
 * 
 * @author xieyc
 * @version 2018-07-24
 */
@Mapper
public interface InterfaceInfoDao extends CrudDao<InterfaceInfo> {

	public void deleteParam(InterfaceInfo interfaceInfo);

	/**
	 * 保存接口参数
	 * 
	 * @param interfaceParam
	 */
	public void insertParam(InterfaceParam interfaceParam);

	/**
	 * 根据砂场id，启用状态获取接口信息
	 * 
	 * @param sandFarmId
	 * @param status
	 * @return
	 */
	public InterfaceInfo queryInfo(@Param("sandFarmId") String sandFarmId, @Param("status") String status);

	/**
	 * 名称验证
	 * 
	 * @param interfaceInfo
	 * @return
	 */
	public List<InterfaceInfo> nameValidate(InterfaceInfo interfaceInfo);

	List<InterfaceInfo> list(Map<String, Object> map);

	int count(Map<String, Object> map);

}