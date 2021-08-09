package com.txdata.system.controller;

import com.txdata.common.config.BootdoConfig;
import com.txdata.common.controller.BaseController;
import com.txdata.common.domain.FileDO;
import com.txdata.common.domain.Tree;
import com.txdata.common.service.FileService;
import com.txdata.common.utils.CacheUtils;
import com.txdata.common.utils.MD5Utils;
import com.txdata.common.utils.R;
import com.txdata.common.utils.ShiroUtils;
import com.txdata.common.websocket.WebSocketSendMsgService;
import com.txdata.system.config.UserConfig;
import com.txdata.system.domain.AjaxMenu;
import com.txdata.system.domain.MenuDO;
import com.txdata.system.domain.UserDO;
import com.txdata.system.service.MenuService;
import com.txdata.system.service.UserService;
import com.txdata.system.utils.UserUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController extends BaseController {

	@Autowired
	private SessionDAO sessionDAO;
	@Autowired
	MenuService menuService;
	@Autowired
	FileService fileService;
	@Autowired
	BootdoConfig bootdoConfig;
	@Autowired
	UserService userService;
	@Autowired
	WebSocketSendMsgService websocketSendMsgService;

	@GetMapping({ "/", "" })
	String welcome(Model model) {
		return "redirect:/index";
	}

	@GetMapping({ "/index" })
	String index(Model model) {
		model.addAttribute("name", getUser().getName());
		model.addAttribute("picUrl", "/img/photo_s.jpg");
		model.addAttribute("username", getUser().getUsername());
		return "index_v1";
	}

	@GetMapping("/login")
	String login(Model model) {
		model.addAttribute("username", bootdoConfig.getUsername());
		model.addAttribute("password", bootdoConfig.getPassword());
		return "login";
	}

	@PostMapping("/login")
	@ResponseBody
	R ajaxLogin(String username, String password, HttpServletRequest request) {
		if (password.length() != 32) {
			password = MD5Utils.getMD5(password);
		}
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
		} catch (AuthenticationException e) {
			return R.error("用户名或密码错误");
		}
		String sessionId = subject.getSession().getId().toString();
		UserDO user = userService.getByUsername(username);
		if(UserConfig.LOGIN_FLAG_FALSE.equals(user.getLoginFlag())) {
			return R.error("用户被禁止登录,请联系管理员！");
		}
		HttpSession session = request.getSession(false);
		session.setAttribute("SHIRO_SESSION_ID", sessionId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("token", sessionId);
		if (user != null) {
			map.put("id", user.getId());
			map.put("name", user.getName());
			map.put("loginName", username);
			map.put("roleEname", user.getRoleEnames());
			map.put("officeId", user.getOfficeId());
			map.put("officeName", user.getOfficeName());
			map.put("position", user.getPosition());
			map.put("userType", user.getUserType());
		}
		return R.success(map);
	}

	/**
	 * 退出登录
	 * 
	 * @return
	 */
	@PostMapping("/logout")
	@ResponseBody
	R postLogout() {
		// 清除当前用户缓存
		UserUtils.clearCache();
		ShiroUtils.logout();
		Session session = SecurityUtils.getSubject().getSession();
		// 清除session
		sessionDAO.delete(session);
		return R.ok();
	}

	@GetMapping("/logout")
	String logout() {
		ShiroUtils.logout();
		return "redirect:/login";
	}

	@GetMapping("/main")
	String main() {
		return "main";
	}

	/**
	 * 获取当前登录用户菜单
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getMenuList", method = RequestMethod.POST)
	public String getMenuList(HttpServletRequest request, HttpServletResponse response) {
		List<AjaxMenu> menuTree = UserUtils.getMenuTree(false);
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("menu", menuTree);
		return renderString(response, R.success(jsonMap));
	}
}
