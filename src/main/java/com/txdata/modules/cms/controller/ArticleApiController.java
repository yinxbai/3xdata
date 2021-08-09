/**
 * @Copyright: 2019 www.3xdata.cn Inc. All rights reserved. 
 * @description：用一句话描述该文件做什么
 * @author: mark   
 * @date: 2019年3月5日 下午3:41:48 
 */
package com.txdata.modules.cms.controller;

import java.util.HashMap;
import java.util.Map;


import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping(value = "/articleManage/")
public class ArticleApiController {

	@Autowired
	private ArticleService articleService;
	
	@RequiresPermissions("cms:article:manage")
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

	@RequiresPermissions("cms:article:manage")
	@RequestMapping(value = "form", method=RequestMethod.POST)
	@ResponseBody
	public R form(String id) {
		Article article = null;
		if (StringUtils.isNotBlank(id)){
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
	
	@RequestMapping(value = "save", method=RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("cms:article:manage")
	public R save(Article article) {
		if (articleService.save(article) > 0) {
			return R.success();
		}
		return R.error();
	}

	@RequiresPermissions("cms:article:manage")
	@RequestMapping(value = "delete", method=RequestMethod.POST)
	@ResponseBody
	public R delete(String id) {
		if (articleService.remove(id) > 0) {
			return R.success();
		}
		return R.error();
	}

//	@PostMapping("search")
//	@ResponseBody
//	public R search(Integer pageNo, Integer pageSize, String keyWord,
//					   String categoryId, String beginDate, String endDate){
//		org.springframework.data.domain.Page<Article> page = articleService.search(pageNo, pageSize, keyWord, categoryId, beginDate, endDate);
//		JSONObject jsonMap = new JSONObject();
//		jsonMap.put("rows", page.getContent());
//		jsonMap.put("pageSize", page.getSize());
//		jsonMap.put("pageNo", page.getNumber());
//		jsonMap.put("count", page.getTotalElements());
//		return R.success(jsonMap);
//	}
}
