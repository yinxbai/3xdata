package com.txdata.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import com.txdata.common.persistence.TreeDao;
import com.txdata.system.domain.Office;
import org.apache.ibatis.annotations.Param;

/**
 * 机构DAO接口
 * 
 * @author ThinkGem
 * @version 2014-05-16
 */
@Mapper
public interface OfficeDao extends TreeDao<Office> {

	List<Office> list(@Param("entity") Map<String, Object> map);

	/**
	 * 根据公司id和地区id查找所有机构
	 * 
	 * @param entity
	 * @return
	 */
	public List<Office> getListByOfficeId(Office entity);

	/**
	 * 根据机构名称和地区id查找所有机构
	 * 
	 * @param entity
	 * @return
	 */
	public List<Office> findByNameLikeAndAreaId(Office entity);

	/**
	 * 根据名称和区域id查询条数
	 * 
	 * @param entity
	 * @return
	 */
	public int findByNameLikeAndAreaIdCount(Office entity);

	/**
	 * 根据机构ID查找它有几个下级机构
	 * 
	 * @param office
	 * @return
	 */
	public Integer countChildOffice(Office office);

	/**
	 * 获取两级以内的机构（江西省、南昌市、九江市。。。）
	 * 
	 * @param office
	 * @return
	 */
	public List<Office> findLevelOneList(Office office);

	/**
	 * 获取下属一级的机构和其自己
	 * 
	 * @param office
	 * @return
	 */
	public List<Office> findChildOffice(Office office);

	/**
	 * 根据机构编码获取机构
	 * 
	 * @param office
	 * @return
	 */
	public List<Office> findByCode(Office office);

	/**
	 * 根据父级id查找子部门
	 * 
	 * @param office
	 * @return
	 */
	public List<Office> findByparentId(Office office);

	/**
	 * 根据部门code获取部门信息
	 * 
	 * @param code
	 * @return
	 */
	public Office getOfficeNameByCode(String code);

	/**
	 * 根据名称查询获取列表
	 * 
	 * @param office
	 * @return
	 */
	public List<Office> findByName(Office office);

}
