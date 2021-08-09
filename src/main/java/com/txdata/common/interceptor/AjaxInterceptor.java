package com.txdata.common.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSONObject;
import com.txdata.common.utils.R;
import com.txdata.common.utils.ShiroUtils;
import com.txdata.common.utils.StringUtils;
import com.txdata.system.domain.UserDO;

/**
 * 接口Token拦截器
 * 
 * @author mark
 * @version 2018-03-08
 */
public class AjaxInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String token = request.getHeader("Authorization");
		String userId = request.getHeader("UserId");
		if (StringUtils.isBlank(token)) {
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if ("JSESSIONID".equalsIgnoreCase(cookie.getName())) {
						token = cookie.getValue();
						break;
					}
				}
			}
		}
		UserDO user = ShiroUtils.getUser();
		if (user != null && StringUtils.isNotBlank(user.getId()) && StringUtils.isNotBlank(token)) {
			if(!user.getId().equals(userId)) {
				R r = R.error("998", "请重新登录");
				String string = JSONObject.toJSONString(r);
				response.setContentType("application/json; charset=UTF-8");
				response.setCharacterEncoding("utf-8");
				response.getWriter().print(string);
				return false;
			}
			return true;
		}
		R r = R.error("999", "登录信息已过期");
		String string = JSONObject.toJSONString(r);
		response.setContentType("application/json; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		response.getWriter().print(string);
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
