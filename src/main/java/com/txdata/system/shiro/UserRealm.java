package com.txdata.system.shiro;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.txdata.common.config.ApplicationContextRegister;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import com.txdata.common.utils.ShiroUtils;
import com.txdata.system.dao.UserDao;
import com.txdata.system.domain.UserDO;
import com.txdata.system.service.MenuService;
import com.txdata.system.utils.UserUtils;

public class UserRealm extends AuthorizingRealm {

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		String userId = ShiroUtils.getUserId();
		MenuService menuService = ApplicationContextRegister.getBean(MenuService.class);
		Set<String> perms = menuService.listPerms(userId);
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(perms);
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal();
		Map<String, Object> map = new HashMap<>(16);
		map.put("username", username);
		String password = new String((char[]) token.getCredentials());
		UserDao userMapper = ApplicationContextRegister.getBean(UserDao.class);
		// 查询用户信息
		UserDO user = userMapper.getByUsername(username);
		// 账号不存在
		if (user == null) {
			throw new UnknownAccountException("账号或密码不正确");
		}
//		UserDO user = userList.get(0);
		// 密码错误
		if (!password.equals(user.getPassword())) {
			throw new IncorrectCredentialsException("账号或密码不正确");
		}
		// 账号锁定
		if (user.getStatus() == 0) {
			throw new LockedAccountException("账号已被锁定,请联系管理员");
		}
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
		return info;
	}

	/**
	 * 授权用户信息
	 */
	public static class Principal implements Serializable {

		private static final long serialVersionUID = 1L;
		private String id; // 编号
		private String loginName; // 登录名
		private String name; // 姓名
//		private boolean mobileLogin; // 是否手机登录
//		private Map<String, Object> cacheMap;

		public Principal(UserDO user) {
			this.id = user.getId();
			this.loginName = user.getUsername();
			this.name = user.getName();
//			this.mobileLogin = mobileLogin;
		}

		public String getId() {
			return id;
		}

		public String getLoginName() {
			return loginName;
		}

		public String getName() {
			return name;
		}

//		public boolean isMobileLogin() {
//			return mobileLogin;
//		}

		/**
		 * 获取SESSIONID
		 */
		public String getSessionid() {
			try {
				return (String) UserUtils.getSession().getId();
			} catch (Exception e) {
				return "";
			}
		}

		@Override
		public String toString() {
			return id;
		}

	}
}