package com.txdata.modules.project.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.modules.project.domain.ProjectBidInfoDO;
import com.txdata.common.persistence.proxy.CrudDao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 招投标信息表
 * @author huangmk
 * @email huangmk@3xdata.cn
 * @date 2018-12-27 11:21:10
 */
@Mapper
public interface ProjectBidInfoDao extends CrudDao<ProjectBidInfoDO> {

	ProjectBidInfoDO get(String id);
	
	Page<ProjectBidInfoDO> list(Page<ProjectBidInfoDO> page, @Param("entity")Map<String,Object> map);
	
	List<ProjectBidInfoDO> list(@Param("entity")Map<String,Object> map);
	
	int insert(ProjectBidInfoDO projectBidInfo);
	
	int update(ProjectBidInfoDO projectBidInfo);
	
	int remove(String id);
	
	int batchRemove(String[] ids);
	
	/**
	 * 
	 * @Description: 根据项目ID删除对应的招投标信息
	 *
	 * @param: 项目ID
	 * @return: 删除条数
	 * @throws: 异常描述
	 *
	 * @author: mark
	 * @date: 2018年12月27日 下午2:12:31
	 */
	int deleteByProject(String projectId);
	
	/**
	 * 
	 * @Description: 根据项目ID获取对应的招投标信息
	 *
	 * @param: 参数描述
	 * @return: 返回结果描述
	 * @throws: 异常描述
	 *
	 * @author: mark
	 * @date: 2018年12月27日 下午3:04:25
	 */
	ProjectBidInfoDO getByProject(String projectId);
}
