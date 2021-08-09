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
@RequestMapping("/categoryManage")
public class CategoryController extends BaseController {
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ArticleService articleService;
	
	@ResponseBody
	@PostMapping("/list")
	@RequiresPermissions("cms:category:manage")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
        List<CategoryDO> list = categoryService.findList(new CategoryDO());
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
	@RequiresPermissions("cms:category:manage")
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
	
	@PostMapping("/delete")
	@ResponseBody
	@RequiresPermissions("cms:category:manage")
	public R remove(String id){
		Map<String, Object> map = new HashMap<>();
		map.put("parentId" , id);
		List<CategoryDO> list = categoryService.list(map);
		map.clear();
		if (list.isEmpty()){
			map.put("categoryId", id);
			List<Article> articles = articleService.list(map);
			if(articles.isEmpty()) {
				if (categoryService.remove(id) > 0){
					return R.success();
				}
			}else {
				return R.error("该栏目下存在文章，需移除文章后才能进行删除操作");
			}
		}else {
			return R.error("该栏目下存在子级栏目，需移除子级栏目后才能进行删除操作");
		}
		return R.error();
	}
	
	@PostMapping("/batchRemove")
	@ResponseBody
	@RequiresPermissions("cms:category:manage")
	public R remove(@RequestParam("ids[]") String[] ids){
		categoryService.batchRemove(ids);
		return R.success();
	}
	
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("cms:category:manage")
	public R save(String id, String name, String image,Integer sort, String parentId,String remarks,String href,String imageAnti) {
		if(StringUtils.isBlank(id)){
			// 校验是否重名
			if (!categoryService.validateTitle(id, name, parentId)) {
				return R.error("400","栏目名称已存在");
			}
		}
		// 校验父级栏目是否和自身是同级
		if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(parentId)) {
			if (id.equals(parentId)){
				return R.error("400","不能选择自身为父级");
			}
		}
		CategoryDO category = new CategoryDO(id);
		category.setImage(image);
		category.setImageAnti(imageAnti);
		category.setSort(sort);
		category.setHref(href);
		category.setName(name);
		category.setRemarks(remarks);
		if (StringUtils.isBlank(parentId)) {
			parentId = "1";
		}
		category.setParentId(parentId);
		CategoryDO parent = categoryService.get(parentId);
		category.setParent(parent);
		categoryService.save(category);
		return R.success();
	}
	
}
