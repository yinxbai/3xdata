/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.txdata.modules.cms.utils;

import java.util.Map;

import com.txdata.common.mapper.JsonMapper;
import com.txdata.common.utils.SpringContextHolder;
import com.txdata.common.utils.StringUtils;
import com.txdata.modules.cms.domain.Article;
import com.txdata.modules.cms.service.ArticleService;
import com.txdata.modules.cms.service.CategoryService;

import javax.servlet.ServletContext;

import org.springframework.ui.Model;

/**
 * 内容管理工具类
 * @author 
 * @version 2013-5-29
 */
public class CmsUtils {
	
	private static CategoryService categoryService = SpringContextHolder.getBean(CategoryService.class);
	private static ArticleService articleService = SpringContextHolder.getBean(ArticleService.class);
    private static ServletContext context = SpringContextHolder.getBean(ServletContext.class);

	private static final String CMS_CACHE = "cmsCache";
	
    /**
     * 获得文章动态URL地址
   	 * @param article
   	 * @return url
   	 */
    public static String getUrlDynamic(Article article) {
        if(StringUtils.isNotBlank(article.getLink())){
            return article.getLink();
        }
        StringBuilder str = new StringBuilder();
//        str.append(context.getContextPath()).append(Global.getFrontPath());
//        str.append("/view-").append(article.getCategory().getId()).append("-").append(article.getId()).append(Global.getUrlSuffix());
        return str.toString();
    }

    /**
     * 从图片地址中去除ContextPath地址
   	 * @param src
   	 * @return src
   	 */
    public static String formatImageSrcToDb(String src) {
        if(StringUtils.isBlank(src)) return src;
        if(src.startsWith(context.getContextPath() + "/userfiles")){
            return src.substring(context.getContextPath().length());
        }else{
            return src;
        }
    }

    /**
     * 从图片地址中加入ContextPath地址
   	 * @param src
   	 * @return src
   	 */
    public static String formatImageSrcToWeb(String src) {
        if(StringUtils.isBlank(src)) return src;
        if(src.startsWith(context.getContextPath() + "/userfiles")){
            return src;
        }else{
            return context.getContextPath()+src;
        }
    }
    
    public static void addViewConfigAttribute(Model model, String param){
        if(StringUtils.isNotBlank(param)){
            @SuppressWarnings("rawtypes")
			Map map = JsonMapper.getInstance().fromJson(param, Map.class);
            if(map != null){
                for(Object o : map.keySet()){
                    model.addAttribute("viewConfig_"+o.toString(), map.get(o));
                }
            }
        }
    }
}