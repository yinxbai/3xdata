/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.txdata.modules.cms.domain;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
//import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.txdata.common.domain.DataEntity;
import com.txdata.modules.cms.utils.CmsUtils;
import com.txdata.system.domain.UserDO;

/**
 * 文章Entity
 * @author ThinkGem
 * @version 2013-05-15
 */

/**
 * elasticsearch 全文检索插件
 * indexName 对应索引库名称 相当于表
 * Document 行 相当于记录
 */
//@Document(indexName = "arricle")
public class Article extends DataEntity<Article> {

    public static final String DEFAULT_TEMPLATE = "frontViewArticle";
	
	private static final long serialVersionUID = 1L;
	private String title;	// 标题
	private String titleImg;//标题图
	private String link;	// 外部链接
	private String color;	// 标题颜色（red：红色；green：绿色；blue：蓝色；yellow：黄色；orange：橙色）
	private String levelType;  //1.国家 2.省级
	private String image;	// 文章图片
	private String keywords;// 关键字
	private String description;// 描述、摘要
	private String weight;	// 权重，越大越靠前
	@JsonFormat(pattern="yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")  
	private Date weightDate;// 权重期限，超过期限，将weight设置为0
	private Integer hits;	// 点击数
	private String posid;	// 推荐位，多选（1：首页焦点图；2：栏目页文章推荐；）
	private String customContentView;//自定义内容视图
	private String viewConfig;//视图配置
   	private String content;		// 文章内容
   	private String filePath;	// 文件地址，逗号分隔
   	private String videoPath;//视频地址
	private String videoTime;//视频时长
	private String preImage;//视频预览图
	public String getCustomContentView() {
		return customContentView;
	}

	public void setCustomContentView(String customContentView) {
		this.customContentView = customContentView;
	}

	public String getViewConfig() {
		return viewConfig;
	}

	public void setViewConfig(String viewConfig) {
		this.viewConfig = viewConfig;
	}

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public String getVideoTime() {
		return videoTime;
	}

	public void setVideoTime(String videoTime) {
		this.videoTime = videoTime;
	}

	public String getPreImage() {
		return preImage;
	}

	public void setPreImage(String preImage) {
		this.preImage = preImage;
	}


//	private ArticleData articleData;	//文章副表
   	@JsonFormat(pattern="yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")  
	private Date beginDate;	// 开始时间
   	@JsonFormat(pattern="yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")  
	private Date endDate;	// 结束时间
	private String categoryId;	// 分类栏目ID
	private String categoryName;	// 分类栏目名称
	private String isTop;	// 是否置顶
	
	private String sortType;		// 排序方式 1-时间排序 2-权重排序
	
	
	/**
	 * 国家
	 */
	public static final String COUNTRY_TYPE = "1";
	
	/**
	 * 省级
	 */
	public static final String PROVINCIAL_TYPE = "2";
	private UserDO user;
    
	public Article() {
		super();
		this.weight = "0";
		this.hits = 0;
		this.posid = "";
	}

	public Article(String id){
		this();
		this.id = id;
	}
	
//	public Article(Category category){
//		this();
//		this.category = category;
//	}

//	public void prePersist(){
		//TODO 后续处理，暂不知有何用处
		//super.prePersist();
//		articleData.setId(this.id);
//	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

    @Length(min=0, max=255)
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

	@Length(min=0, max=50)
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Length(min=0, max=255)
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
        this.image = image;//CmsUtils.formatImageSrcToDb(image);
	}

	@Length(min=0, max=255)
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Length(min=0, max=255)
	public String getDescription() {
		return description;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public Date getWeightDate() {
		return weightDate;
	}

	public void setWeightDate(Date weightDate) {
		this.weightDate = weightDate;
	}

	public Integer getHits() {
		return hits;
	}

	public void setHits(Integer hits) {
		this.hits = hits;
	}

	@Length(min=0, max=10)
	public String getPosid() {
		return posid;
	}

	public void setPosid(String posid) {
		this.posid = posid;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
    public String getTitleImg() {
		return titleImg;
 	}

	public void setTitleImg(String titleImg) {
		this.titleImg = titleImg;
	}


	public List<String> getPosidList() {
		List<String> list = Lists.newArrayList();
		if (posid != null){
			for (String s : StringUtils.split(posid, ",")) {
				list.add(s);
			}
		}
		return list;
	}

	public UserDO getUser() {
		return user;
	}

	public void setUser(UserDO user) {
		this.user = user;
	}

	public void setPosidList(List<String> list) {
		posid = ","+StringUtils.join(list, ",")+",";
	}

   	public String getUrl() {
        return CmsUtils.getUrlDynamic(this);
   	}

   	public String getImageSrc() {
        return CmsUtils.formatImageSrcToWeb(this.image);
   	}

	public String getIsTop() {
		return isTop;
	}

	public void setIsTop(String isTop) {
		this.isTop = isTop;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getLevelType() {
		return levelType;
	}

	public void setLevelType(String levelType) {
		this.levelType = levelType;
	}
	
}


