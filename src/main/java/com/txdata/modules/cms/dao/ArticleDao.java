/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.txdata.modules.cms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.common.persistence.proxy.CrudDao;
import com.txdata.modules.cms.domain.Article;


/**
 * 文章DAO接口
 * @author ThinkGem
 * @version 2013-8-23
 */
@Mapper
public interface ArticleDao extends CrudDao<Article> {
	
	public List<Article> findByIdIn(String[] ids);
	
	public List<Article> list(@Param("entity")Map<String, Object> map);
	
	Page<Article> list(Page<Article> page, @Param("entity")Map<String,Object> map);
	
	public int updateHitsAddOne(String id);
	
	public int updateExpiredWeight(Article article);
	
}
