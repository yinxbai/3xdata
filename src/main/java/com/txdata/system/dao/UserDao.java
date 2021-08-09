package com.txdata.system.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.common.persistence.proxy.CrudDao;
import com.txdata.system.domain.UserDO;

/**
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-03 09:45:11
 */
@Mapper
public interface UserDao extends CrudDao<UserDO> {

	UserDO get(String userId);

	UserDO getByUsername(String username);

//	List<UserDO> list(Map<String, Object> map);

	int count(@Param("entity") Map<String, Object> map);

	int save(UserDO user);

	int update(UserDO user);

	int remove(String userId);

	int batchRemove(String[] userIds);

	/**
	 * 根据roleId查询用户
	 * 
	 * @param roleId
	 * @return
	 */
	List<UserDO> listUserByRole(String roleId);

	/**
	 * 查询全部用户数目
	 * 
	 * @return
	 */
	long findAllCount(UserDO user);

	/**
	 * 通过OfficeId获取用户列表，仅返回用户id和name（树查询用户时用）
	 * 
	 * @param user
	 * @return
	 */
	public List<UserDO> findUserByOfficeId(UserDO user);

	/**
	 * 查询没有机构的用户
	 * 
	 * @return
	 */
	public List<UserDO> findUserByNotInOfficeId();

	/**
	 * 根据登录名称查询用户
	 * 
	 * @param loginName
	 * @return
	 */
	public UserDO getByLoginName(UserDO user);

	/**
	 * 删除用户角色关联数据
	 * 
	 * @param user
	 * @return
	 */
	public int deleteUserRole(UserDO user);

	/**
	 * 插入用户角色关联数据
	 * 
	 * @param user
	 * @return
	 */
	public int insertUserRole(UserDO user);

	public List<UserDO> findUserList(UserDO user);

	Page<UserDO> queryList(Page<UserDO> page, @Param("entity") Map<String, Object> map);
	
	List<UserDO> queryList(@Param("entity") Map<String, Object> map);

//	int queryListCount(Map<String, Object> map);

	/**
	 * 查找用户名是否重复
	 * 
	 * @param loginName
	 * @return
	 */
	int userCount(String loginName);

	/**
	 * 更新用户密码
	 * 
	 * @param user
	 * @return
	 */
	public int updatePasswordById(UserDO user);

	/**
	 * 更新用户信息
	 * 
	 * @param user
	 * @return
	 */
	public int updateUserInfo(UserDO user);

	/**
	 * 查询现场管理人员名单
	 * 
	 * @param user
	 * @return
	 */
	List<UserDO> findUser(UserDO user);
	
	/**
	 * 
	 * @Description: 根据用户ID数组，获取用户列表
	 *
	 * @param: 参数描述
	 * @return: 返回结果描述
	 * @throws: 异常描述
	 *
	 * @author: haungmk
	 * @date: 2019年1月15日 下午3:24:58
	 */
	List<UserDO> listByUserIds(String[] userIds);

	/**
	 * 查找用户
	 * @param id
	 * @return
	 */
	UserDO findById(String id);
}
