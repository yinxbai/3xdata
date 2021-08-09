package com.txdata.system.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.common.config.BootdoConfig;
import com.txdata.common.domain.FileDO;
import com.txdata.common.service.FileService;
import com.txdata.common.utils.*;
import com.txdata.common.utils.MD5Utils;
import com.txdata.common.utils.StringUtils;
import com.txdata.system.service.DeptService;
import com.txdata.system.vo.UserVO;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.txdata.common.domain.Tree;
import com.txdata.common.persistence.CrudService;
import com.txdata.system.dao.DeptDao;
import com.txdata.system.dao.OfficeDao;
import com.txdata.system.dao.RoleDao;
import com.txdata.system.dao.UserDao;
import com.txdata.system.dao.UserRoleDao;
import com.txdata.system.domain.DeptDO;
import com.txdata.system.domain.Office;
import com.txdata.system.domain.Role;
import com.txdata.system.domain.UserDO;
import com.txdata.system.domain.UserRoleDO;
import com.txdata.system.service.UserService;
import com.txdata.system.utils.UserUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;

/**
 * 用户管理相关Service
 * 
 * @author mark
 *
 */
@Transactional
@Service
public class UserService extends CrudService<UserDao, UserDO> {

	@Autowired
	UserDao userMapper;
	@Autowired
	UserRoleDao userRoleMapper;
	@Autowired
	DeptDao deptMapper;
	@Autowired
	private FileService sysFileService;
	@Autowired
	private BootdoConfig bootdoConfig;
	@Autowired
	DeptService deptService;
	@Autowired
	SessionDAO sessionDAO;
	@Autowired
	OfficeDao officeDao;
	@Autowired
	RoleDao roleDao;

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Override
	public UserDO get(String id) {
		List<String> roleIds = userRoleMapper.listRoleId(id);
		UserDO user = userMapper.get(id);
//		user.setDeptName(deptMapper.get(user.getDeptId()).getName());
		user.setRoleIds(roleIds);
		return user;
	}

	public UserDO getOffice(String id) {
		List<String> roleIds = userRoleMapper.listRoleId(id);
		UserDO user = userMapper.get(id);
		Office office = officeDao.get(user.getOfficeId());
		user.setOfficeName(office.getName());
		user.setRoleIds(roleIds);
		return user;
	}

	public UserDO getByUsername(String username) {
		UserDO user = userMapper.getByUsername(username);
		if (user != null) {
			List<String> roleIds = userRoleMapper.listRoleId(user.getId());
			Office office = officeDao.get(user.getOfficeId());
			if (office != null) {
				user.setOfficeName(office.getName());
			}
			user.setRoleIds(roleIds);
			Role role = new Role();
			role.setUserId(user.getId());
			role.setUserName(user.getUsername());
			user.setRoleList(roleDao.findList(role));
		}
		return user;
	}

	/**
	 * 根据登录名获取用户
	 * 
	 * @param loginName
	 * @return
	 */
	public UserDO getUserByLoginName(String loginName) {
		return UserUtils.getByLoginName(loginName);
	}

	@Override
	public List<UserDO> list(Map<String, Object> map) {
		Object obj = map.get("deptId");
		if (obj != null && StringUtils.isNotBlank(obj.toString())) {
			String deptId = obj.toString();
			List<String> childIds = deptService.listChildrenIds(deptId);
			childIds.add(deptId);
			map.put("deptId", null);
			map.put("deptIds", childIds);
		}
		return userMapper.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return userMapper.count(map);
	}

	@Transactional
	@Override
	public int save(UserDO user) {
		user.preInsert();
		int count = userMapper.save(user);
		String userId = user.getId();
		List<String> roles = user.getRoleIds();
		userRoleMapper.removeByUserId(userId);
		List<UserRoleDO> list = new ArrayList<>();
		for (String roleId : roles) {
			UserRoleDO ur = new UserRoleDO();
			ur.setUserId(userId);
			ur.setRoleId(roleId);
			ur.preInsert();
			list.add(ur);
		}
		if (list.size() > 0) {
			userRoleMapper.batchSave(list);
		}
		return count;
	}

	@Override
	public int update(UserDO user) {
		user.preUpdate();
		// 如果新密码为空，则不更换密码
		if (StringUtils.isNotBlank(user.getPassword())) {
			user.setPassword(MD5Utils.getMD5(user.getPassword()));
		}
		int r = userMapper.update(user);
		String userId = user.getId();
		List<String> roles = user.getRoleIds();
		userRoleMapper.removeByUserId(userId);
		List<UserRoleDO> list = new ArrayList<>();
		for (String roleId : roles) {
			UserRoleDO ur = new UserRoleDO();
			ur.setUserId(userId);
			ur.setRoleId(roleId);
			ur.preInsert();
			list.add(ur);
		}
		if (list.size() > 0) {
			userRoleMapper.batchSave(list);
		}
		return r;
	}

	@Override
	public int remove(String userId) {
		userRoleMapper.removeByUserId(userId);
		int row = userMapper.remove(userId);
		// 剔除该用户所有的session
		Collection<Session> sessions = sessionDAO.getActiveSessions();
		for (Session session : sessions) {
			PrincipalCollection pc = (PrincipalCollection) session
					.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
			if (userId.equals(pc != null ? pc.getPrimaryPrincipal().toString() : StringUtils.EMPTY)) {
				sessionDAO.delete(session);
			}
		}
		return row;
	}

	public boolean exit(Map<String, Object> params) {
		boolean exit;
		exit = userMapper.list(params).size() > 0;
		return exit;
	}

	public Set<String> listRoles(String userId) {
		return null;
	}

	/**
	 * 修改密码
	 * 
	 * @param userVO
	 * @param userDO
	 * @return
	 * @throws Exception
	 */
	public int resetPwd(UserVO userVO, UserDO userDO) throws Exception {
		if (Objects.equals(userVO.getUserDO().getId(), userDO.getId())) {
			if (Objects.equals(MD5Utils.getMD5(userVO.getPwdOld()), userDO.getPassword())) {
				userDO.setPassword(MD5Utils.getMD5(userVO.getPwdNew()));
				return userMapper.update(userDO);
			} else {
				throw new Exception("输入的旧密码有误！");
			}
		} else {
			throw new Exception("你修改的不是你登录的账号！");
		}
	}

	public int adminResetPwd(UserVO userVO) throws Exception {
		UserDO userDO = get(userVO.getUserDO().getId());
		if ("admin".equals(userDO.getUsername())) {
			throw new Exception("超级管理员的账号不允许直接重置！");
		}
//        userDO.setPassword(MD5Utils.encrypt(userDO.getUsername(), userVO.getPwdNew()));
		userDO.setPassword(MD5Utils.getMD5(userVO.getPwdNew()));
		return userMapper.update(userDO);
	}

	public void modifyPwd(String id, String newPassword) {
		UserDO user = new UserDO();
		user.setId(id);
		user.setPassword(MD5Utils.getMD5(newPassword));
		userMapper.update(user);
	}

	@Transactional
	public int batchremove(String[] userIds) {
		int count = userMapper.batchRemove(userIds);
		userRoleMapper.batchRemoveByUserId(userIds);
		return count;
	}

	public int updatePersonal(UserDO userDO) {
		return userMapper.update(userDO);
	}

	public Map<String, Object> updatePersonalImg(MultipartFile file, String avatar_data, String userId)
			throws Exception {
		String fileName = file.getOriginalFilename();
		fileName = FileUtil.renameToUUID(fileName);
		FileDO sysFile = new FileDO(String.valueOf(FileType.fileType(fileName)), "/files/" + fileName, new Date());
		// 获取图片后缀
		String prefix = fileName.substring((fileName.lastIndexOf(".") + 1));
		String[] str = avatar_data.split(",");
		// 获取截取的x坐标
		int x = (int) Math.floor(Double.parseDouble(str[0].split(":")[1]));
		// 获取截取的y坐标
		int y = (int) Math.floor(Double.parseDouble(str[1].split(":")[1]));
		// 获取截取的高度
		int h = (int) Math.floor(Double.parseDouble(str[2].split(":")[1]));
		// 获取截取的宽度
		int w = (int) Math.floor(Double.parseDouble(str[3].split(":")[1]));
		// 获取旋转的角度
		int r = Integer.parseInt(str[4].split(":")[1].replaceAll("}", ""));
		try {
			BufferedImage cutImage = ImageUtils.cutImage(file, x, y, w, h, prefix);
			BufferedImage rotateImage = ImageUtils.rotateImage(cutImage, r);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			boolean flag = ImageIO.write(rotateImage, prefix, out);
			// 转换后存入数据库
			byte[] b = out.toByteArray();
			FileUtil.uploadFile(b, bootdoConfig.getUploadPath(), fileName);
		} catch (Exception e) {
			throw new Exception("图片裁剪错误！！");
		}
		Map<String, Object> result = new HashMap<>();
		if (sysFileService.save(sysFile) > 0) {
			UserDO userDO = new UserDO();
			userDO.setId(userId);
//			userDO.setPicId(sysFile.getId());
			if (userMapper.update(userDO) > 0) {
				result.put("url", sysFile.getUrl());
			}
		}
		return result;
	}

	public boolean delUserRole(String userId, String roleId) {
		int a = userRoleMapper.removeByUserIdRoleId(userId, roleId);
		if (a > 0) {
			return true;
		} else {
			return false;
		}
	}

	public Page<UserDO> queryList(Page<UserDO> page, Map<String, Object> map) {
		return userMapper.queryList(page, map);
	}

	public List<UserDO> queryList(Map<String, Object> map) {
		return userMapper.queryList(map);
	}

	public int queryUserCount(String loginName) {
		return userMapper.userCount(loginName);
	}
	
	/**
	 * 
	 * @Description: 根据逗号分隔的用户ID，获取用户列表
	 *
	 * @param: userIds 用户ID，逗号分隔
	 * @return: 返回结果描述
	 * @throws: 异常描述
	 *
	 * @author: huangmk
	 * @date: 2019年1月15日 下午3:23:43
	 */
	public List<UserDO> listByUserIds(String userIds){
		if (StringUtils.isNotBlank(userIds)) {
			String[] userIdArr = userIds.split(",");
			return userMapper.listByUserIds(userIdArr);
		}
		return null;
	}

}