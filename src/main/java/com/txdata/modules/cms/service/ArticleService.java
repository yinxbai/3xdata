/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.txdata.modules.cms.service;

import java.util.List;
import java.util.Map;

import cn.hutool.core.util.StrUtil;
//import com.txdata.modules.cms.dao.ArticleRepository;
//import com.txdata.modules.cms.mapper.MyResultMapper;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
//import org.springframework.data.elasticsearch.core.query.Field;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
//import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.common.persistence.CrudService;
import com.txdata.common.utils.StringUtils;
import com.txdata.modules.cms.dao.ArticleDao;
import com.txdata.modules.cms.dao.CategoryDao;
import com.txdata.modules.cms.domain.Article;
import com.txdata.modules.cms.domain.CategoryDO;

import javax.annotation.Resource;

//import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * 文章Service
 * @author ThinkGem
 * @version 2013-05-15
 */
@Service
@Transactional(readOnly = true)
public class ArticleService extends CrudService<ArticleDao, Article> {

	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private CategoryDao categoryDao;
	/*@Autowired
	private ArticleRepository articleRepository;*/
//	@Resource
//	private ElasticsearchTemplate elasticsearchTemplate;
//	@Resource
//	private MyResultMapper myResultMapper;

	public List<Article> list(Map<String, Object> map) {
		return articleDao.list(map);
	}

	@Transactional(readOnly = false)
	public Page<Article> page(Page<Article> page, Map<String, Object> map){
		Article article = new Article();
		if (StringUtils.isNotBlank(article.getCategoryId()) && !CategoryDO.isRoot(article.getCategoryId())){
			CategoryDO category = categoryDao.get(article.getCategoryId());
			if (category==null){
				category = new CategoryDO();
			}
			category.setParentIds(category.getId());
			article.setCategoryName(category.getName());
		}
        return articleDao.list(page, map);
    }

	@Transactional(readOnly = false)
	public int save(Article article){
		int row = super.save(article);
//		articleRepository.save(article);
		return row;
	}

	@Transactional(readOnly = false)
	public void delete(Article article, Boolean isRe) {
//		dao.updateDelFlag(id, isRe!=null&&isRe?Article.DEL_FLAG_NORMAL:Article.DEL_FLAG_DELETE);
		// 使用下面方法，以便更新索引。
		//Article article = dao.get(id);
		//article.setDelFlag(isRe!=null&&isRe?Article.DEL_FLAG_NORMAL:Article.DEL_FLAG_DELETE);
		//dao.insert(article);
		super.delete(article);
//		articleRepository.delete(article);
	}

	/**
	 * 点击数加一
	 */
	@Transactional(readOnly = false)
	public void updateHitsAddOne(String id) {
		dao.updateHitsAddOne(id);
	}

	/**
	 * 更新索引
	 */
	public void createIndex(){
		//dao.createIndex();
	}

	/**
	 * elasticsearch全文检索
	 * @param pageNo 页码
	 * @param pageSize 每页的长度
	 * @param keyWord 关键字
	 * @param categoryId 科室id
	 * @param beginDate 发布时间起
	 * @param endDate 发布时间止
	 * @return
	 */
//	public org.springframework.data.domain.Page<Article> search(Integer pageNo, Integer pageSize, String keyWord,
//																String categoryId, String beginDate, String endDate) {
//		BoolQueryBuilder boolQueryBuilder = boolQuery(); // 查询类的初始化 用于拼接
//		HighlightBuilder highlightBuilder = new HighlightBuilder(); // 高亮类的申明
//		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder(); //查询类的申明
//		highlightBuilder.preTags("<font color='red'>");
//		highlightBuilder.postTags("</font>");
//		if (StrUtil.isNotBlank(keyWord)){
//			highlightBuilder.field("title"); // 高亮字段
//			highlightBuilder.field("keywords"); // 高亮字段
//			highlightBuilder.field("content"); // 高亮字段
//			highlightBuilder.field("description"); // 高亮字段
//			//添加查询的字段内容
//			String [] fileds = {"title", "keywords", "content", "description"};
//			boolQueryBuilder.must(multiMatchQuery(keyWord,fileds));
//		}
//		if(StrUtil.isNotBlank(categoryId)){
//			boolQueryBuilder.must(matchPhraseQuery("categoryId", categoryId));
//		}
//		if(StrUtil.isNotBlank(beginDate) && StrUtil.isNotBlank(endDate)){
//			highlightBuilder.field("createDate");
//			boolQueryBuilder.must(rangeQuery("createDate").from(beginDate).to(endDate)); // 特定范围内查询
//		}
//		if (null != pageNo && null != pageSize) {
//			nativeSearchQueryBuilder.withPageable(PageRequest.of(pageNo, pageSize));
//		}
//		if (!highlightBuilder.fields().isEmpty()) {
//			nativeSearchQueryBuilder.withHighlightBuilder(highlightBuilder);
//		}
//		SearchQuery searchQuery = nativeSearchQueryBuilder.withQuery(boolQueryBuilder).build();
//		return elasticsearchTemplate.queryForPage(searchQuery, Article.class, myResultMapper);
//	}

	/**
	 * 全文检索
	 */
	//FIXME 暂不提供检索功能
//	public Page<Article> search(Page<Article> page, String q, String categoryId, String beginDate, String endDate){

		// 设置查询条件
//		BooleanQuery query = dao.getFullTextQuery(q, "title","keywords","description","articleData.content");
//
//		// 设置过滤条件
//		List<BooleanClause> bcList = Lists.newArrayList();
//
//		bcList.add(new BooleanClause(new TermQuery(new Term(Article.FIELD_DEL_FLAG, Article.DEL_FLAG_NORMAL)), Occur.MUST));
//		if (StringUtils.isNotBlank(categoryId)){
//			bcList.add(new BooleanClause(new TermQuery(new Term("category.ids", categoryId)), Occur.MUST));
//		}
//
//		if (StringUtils.isNotBlank(beginDate) && StringUtils.isNotBlank(endDate)) {
//			bcList.add(new BooleanClause(new TermRangeQuery("updateDate", beginDate.replaceAll("-", ""),
//					endDate.replaceAll("-", ""), true, true), Occur.MUST));
//		}

		//BooleanQuery queryFilter = dao.getFullTextQuery((BooleanClause[])bcList.toArray(new BooleanClause[bcList.size()]));

//		System.out.println(queryFilter);

		// 设置排序（默认相识度排序）
		//FIXME 暂时不提供lucene检索
		//Sort sort = null;//new Sort(new SortField("updateDate", SortField.DOC, true));
		// 全文检索
		//dao.search(page, query, queryFilter, sort);
		// 关键字高亮
		//dao.keywordsHighlight(query, page.getList(), 30, "title");
		//dao.keywordsHighlight(query, page.getList(), 130, "description","articleData.content");

//		return page;
//	}

}
