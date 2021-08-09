package com.txdata.common.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.txdata.common.interceptor.AjaxInterceptor;

/**
 * token 拦截
 * 
 * @author lmh
 *
 */
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 可添加多个，Token拦截器（AjaxInterceptor）将不会拦截这些路径的请求
		List<String> excludePatterns = new ArrayList<String>();
		excludePatterns.add("/login");
		excludePatterns.add("/css/**");
		excludePatterns.add("/js/**");
		excludePatterns.add("/fonts/**");
		excludePatterns.add("/img/**");
		excludePatterns.add("/docs/**");
		excludePatterns.add("/druid/**");
		excludePatterns.add("/upload/**");
		excludePatterns.add("/files/**");
		excludePatterns.add("/sys/fileUpload/upload_image.do");
		excludePatterns.add("/logout");
		excludePatterns.add("/");
		excludePatterns.add("/blog");
		excludePatterns.add("/blog/open/**");
		excludePatterns.add("/report/**");
		excludePatterns.add("/userfiles/**");
		excludePatterns.add("/sys/fileUpload/downloadFile");
		excludePatterns.add("/act/**");
//		excludePatterns.add("/act/process/resource/read");
		excludePatterns.add("/common/generator/**");
		excludePatterns.add("/editor-app/**");
		excludePatterns.add("/modeler.html");
		excludePatterns.add("/fore/articleManage/**");
		excludePatterns.add("/fore/categoryManage/**");
		excludePatterns.add("/sys/dict/fore/getDictList");
		excludePatterns.add("/fore/sys/foreFileUpload");
		registry.addInterceptor(new AjaxInterceptor()).addPathPatterns("/**").excludePathPatterns(excludePatterns);
	}
}
