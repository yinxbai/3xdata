package com.txdata.system.dao;

import com.txdata.common.persistence.proxy.CrudDao;
import com.txdata.system.domain.Menu;
import com.txdata.system.domain.MenuDO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜单管理
 * @author huangmk
 * @date 2017-10-03 09:45:09
 */
@Mapper
public interface MenuDao extends CrudDao<Menu> {
	
	/**
	 * 根据父级id查询数据
	 * @param menu
	 * @return
	 */
	public List<Menu> findByParentId(Menu menu);
	
	/**
	 * 根据用户id查询数据
	 * @param menu
	 * @return
	 */
	public List<Menu> findByUserId(Menu menu);
	
	/**
	 * 查询所有
	 */
	public List<Menu> findAllList(Menu menu);
	
	/**
	 * 根据父级id查询列表
	 * @param menu
	 * @return
	 */
	public List<Menu> findByParentIdsLike(Menu menu);
	
	/**
	 * 修改父级id
	 * @param menu
	 * @return
	 */
	public int updateParentIds(Menu menu);
	
	/**
	 * 
	 * @Description: 查找用户所有的权限标识
	 *
	 * @param: 用户ID
	 * @return: 返回结果描述
	 *
	 * @author: huangmk
	 * @date: 2018年12月24日 下午2:16:22
	 */
	List<String> listUserPerms(String id);

}
