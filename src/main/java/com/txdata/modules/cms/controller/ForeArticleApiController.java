/**
 * @Copyright: 2019 www.3xdata.cn Inc. All rights reserved. 
 * @description：用一句话描述该文件做什么
 * @author: mark   
 * @date: 2019年3月5日 下午3:41:48 
 */
package com.txdata.modules.cms.controller;

import java.util.HashMap;
import java.util.Map;


import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.common.config.Global;
import com.txdata.common.utils.Query;
import com.txdata.common.utils.R;
import com.txdata.common.utils.StringUtils;
import com.txdata.modules.cms.domain.Article;
import com.txdata.modules.cms.service.ArticleService;


/**
 * @ClassName: ArticleApiController
 * @Description: 文章Controller(前后端分离)
 * 
 * @author: czy
 * @date: 2019年3月5日 下午3:41:48
 */
@Controller
@RequestMapping(value = "/fore/articleManage/")
public class ForeArticleApiController {

	@Autowired
	private ArticleService articleService;
	
	@RequestMapping(value = "list", method=RequestMethod.POST)
	@ResponseBody
	public R list(@RequestParam Map<String, Object> params){
		if (!params.isEmpty() && null != params.get("createDate") && !"".equals(params.get("createDate"))){
			String[] createDates = String.valueOf(params.get("createDate")).split(",");
			params.put("createDateStart",createDates[0]);
			params.put("createDateEnd",createDates[1]);
			params.put("createDate","");
		}
		//查询列表数据
        Query query = new Query(params);
        Page<Article> page = new Page<Article>(query.getPageNo(), query.getPageSize());
		page = articleService.page(page, query);
		// 封装分页数据
		Map<String,Object> jsonMap = new HashMap<String,Object>();
        jsonMap.put("rows", page.getRecords());
        jsonMap.put("pageSize", page.getSize());
        jsonMap.put("pageNo", page.getCurrent());
        jsonMap.put("count", page.getTotal());
        return R.success(jsonMap);
	}

	@RequestMapping(value = "form", method=RequestMethod.POST)
	@ResponseBody
	public R form(String id) {
		Article article = null;
		if (StringUtils.isNotBlank(id)){
			articleService.updateHitsAddOne(id);//点击数加1
			article = articleService.get(id);
			String weight = article.getWeight();
			if ("999" == weight) {
				article.setIsTop(Global.YES);
			} else {
				article.setIsTop(Global.NO);
			}
		}else{
			article = new Article();
		}
		Map<String,Object> jsonMap = new HashMap<String,Object>();
	    jsonMap.put("formObject", article);
	    return R.success(jsonMap);
	}
}
