package com.txdata.modules.cms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.common.controller.BaseController;
import com.txdata.common.domain.Tree;
import com.txdata.common.utils.Query;
import com.txdata.common.utils.R;
import com.txdata.common.utils.StringUtils;
import com.txdata.modules.cms.domain.Article;
import com.txdata.modules.cms.domain.CategoryDO;
import com.txdata.modules.cms.service.ArticleService;
import com.txdata.modules.cms.service.CategoryService;

/**
 * 栏目表
 * 
 * @author 3xdata
 * @email 3xdata@3xdata.cn
 * @date 2019-06-14 19:36:17
 */
@Controller
@RequestMapping("/fore/categoryManage")
public class ForeCategoryController extends BaseController {
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ArticleService articleService;
	
	@ResponseBody
	@PostMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
        String id =(String) params.get("id");
        List<CategoryDO> list = categoryService.findList(new CategoryDO(id));
		List<Tree<CategoryDO>> treeList = categoryService.categoryTree(list);
        Page<CategoryDO> page = new Page<CategoryDO>(query.getPageNo(), query.getPageSize());
		page = categoryService.page(page, query);
		// 封装分页数据
		Map<String,Object> jsonMap = new HashMap<String,Object>();
        jsonMap.put("rows", treeList);
        jsonMap.put("pageSize", page.getSize());
        jsonMap.put("pageNo", page.getCurrent());
        jsonMap.put("count", page.getTotal());
        return R.success(jsonMap);
	}
	
    @ResponseBody
    @PostMapping("/form")
    public R form(@RequestParam(required = true)String id ,String parentId){
        CategoryDO category = null;
        if (StringUtils.isNotBlank(id)) {
			category = categoryService.get(id);
		}else{
			category = new CategoryDO();
			if (StringUtils.isNotBlank(parentId)) {
				category.setParentId(parentId);
			}else{
				category = categoryService.get(parentId);
			}
		}
        CategoryDO parent = categoryService.get(category.getParentId());
		category.setParent(parent);
		category.setParentName(parent.getName());
        Map<String,Object> jsonMap = new HashMap<String,Object>();
        jsonMap.put("formObject", category);
        return R.success(jsonMap);
    }
	
}
