package com.txdata.common.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.txdata.common.beanvalidator.BeanValidators;
import com.txdata.common.mapper.JsonMapper;
import com.txdata.common.utils.ShiroUtils;
import com.txdata.system.domain.UserDO;

/**
 * 
 * @ClassName: BaseController
 * @Description: 基础Controller类，提供Controller的常用方法
 * 
 * @version: v1.0.0
 * @author: huangmk
 * @date: 2018年12月24日 上午9:47:23
 */
@Controller
public class BaseController {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	public UserDO getUser() {
		return ShiroUtils.getUser();
	}

	public String getUserId() {
		return getUser().getId();
	}

	public String getUsername() {
		return getUser().getUsername();
	}

	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;

	protected String wssBeanValidator(Object object, Class<?>... groups) {
		try {
			// 调用JSR303的validate方法, 验证失败时抛出ConstraintViolationException.
			BeanValidators.validateWithException(validator, object, groups);
		} catch (ConstraintViolationException ex) {
			// 辅助方法, 转换ConstraintViolationException中的Set为List.
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			StringBuffer sb = new StringBuffer();
			for (String string : list) {
				sb.append(string.split(":")[1]);
			}
			sb.insert(0, "数据验证失败：");
			return sb.toString();
		}
		return null;
	}

	/**
	 * 客户端返回JSON字符串
	 * 
	 * @param response
	 * @param object
	 * @return
	 */
	protected String renderString(HttpServletResponse response, Object object) {
		return renderString(response, JsonMapper.toJsonString(object), "application/json");
	}

	/**
	 * 客户端返回字符串
	 * 
	 * @param response
	 * @param string
	 * @return
	 */
	protected String renderString(HttpServletResponse response, String string, String type) {
		try {
//			response.reset();
			response.setContentType(type);
			response.setCharacterEncoding("utf-8");
			response.getWriter().print(string);
			return null;
		} catch (IOException e) {
			return null;
		}
	}
}