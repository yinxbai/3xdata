package com.txdata.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 	虚拟路径设置 访问预览图需要
 * 
 * @author lmh
 *
 */
@Component
class WebConfigurer implements WebMvcConfigurer {

	@Autowired
	BootdoConfig bootdoConfig;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String url = "file:///" + bootdoConfig.getUploadPath() + "/files/";
		registry.addResourceHandler("/files/**").addResourceLocations(url);
	}

}