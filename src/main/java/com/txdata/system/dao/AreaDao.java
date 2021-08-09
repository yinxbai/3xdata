package com.txdata.system.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.txdata.common.persistence.TreeDao;
import com.txdata.system.domain.Area;

/**
 * 区域DAO接口
 * 
 * @author ThinkGem
 * @version 2014-05-16
 */
public interface AreaDao extends TreeDao<Area> {

	/**
	 * 根据code获取
	 * 
	 * @param area
	 * @return
	 */
	public Area getByCode(Area area);

	/**
	 * 根据父级id查询列表
	 * 
	 * @param area
	 * @return
	 */
	public List<Map<String, Object>> findByParentId(Area area);

	/**
	 * 获取子区域id
	 * 
	 * @param areaId
	 * @return
	 */
	public List getAreaAndChildrenId(String areaId);

	/**
	 * 根据区域的名称查询区域的id集合
	 */
	public List<String> findListByName(@Param("name") String name);

}
