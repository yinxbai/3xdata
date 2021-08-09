package com.txdata.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.txdata.common.interceptor.ImageShowInterceptor;

/**
 * 前端url直接获取图片地址拦截
 * @author lmh
 *
 */
@SpringBootConfiguration
public class ImageConfigurer implements WebMvcConfigurer {
	@Autowired
	private ImageShowInterceptor imageInter;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		registry.addInterceptor(imageInter).addPathPatterns("/userfiles/**");
	}

}
