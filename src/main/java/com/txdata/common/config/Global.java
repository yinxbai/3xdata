/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.txdata.common.config;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.springframework.core.io.DefaultResourceLoader;
import com.txdata.common.utils.PropertiesLoader;
import com.txdata.common.utils.StringUtils;
import com.google.common.collect.Maps;

/**
 * 全局配置类
 * 
 * @author ThinkGem
 * @version 2014-06-25
 */
public class Global {

	/**
	 * 当前对象实例
	 */
	private static Global global = new Global();

	/**
	 * 显示/隐藏
	 */
	public static final String SHOW = "1";
	public static final String HIDE = "0";

	/**
	 * 是/否
	 */
	public static final String YES = "1";
	public static final String NO = "0";

	/**
	 * 对/错
	 */
	public static final String TRUE = "true";
	public static final String FALSE = "false";

	/**
	 * 上传文件基础虚拟路径
	 */
	public static final String USERFILES_BASE_URL = "/userfiles/";

	/**
	 * 获取当前对象实例
	 */
	public static Global getInstance() {
		return global;
	}

}
