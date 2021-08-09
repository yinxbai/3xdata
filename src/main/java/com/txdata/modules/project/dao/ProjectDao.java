package com.txdata.modules.project.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.modules.project.domain.ProjectDO;
import com.txdata.modules.project.vo.ProjectSummarizeVO;
import com.txdata.common.persistence.proxy.CrudDao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 项目备案基本信息表
 * @author huangmk
 * @email huangmk@3xdata.cn
 * @date 2018-12-27 10:59:18
 */
@Mapper
public interface ProjectDao extends CrudDao<ProjectDO> {

	ProjectDO get(String id);
	
	Page<ProjectDO> list(Page<ProjectDO> page, @Param("entity")Map<String,Object> map);
	
	List<ProjectDO> list(@Param("entity")Map<String,Object> map);
	
	int insert(ProjectDO project);
	
	int update(ProjectDO project);
	
	int remove(String id);
	
	/**
	 * 
	 * @Description: 查询对应条件下的项目名称列表
	 *
	 * @param: isLeader 是否有负责人 1-是 0-否  默认不传,不传则查全部
	 * @param: status 项目状态 1-启用 2-关闭
	 * @param: userId 用户ID 查询项目成员中有这个用户的项目
	 * @return: 返回结果描述
	 * @throws: 异常描述
	 *
	 * @author: huangmk
	 * @date: 2018年12月27日 下午3:53:54
	 */
	List<Map<String, String>> nameList(@Param("status")String status, @Param("userId")String userId);
	
	/**
	 * 
	 * @Description: 查询当天创建的项目并按创建时间倒序排列
	 *
	 * @param: nowDate 当天日期 yyyy-MM-dd
	 * @return: 返回结果描述
	 * @throws: 异常描述
	 *
	 * @author: huangmk
	 * @date: 2018年12月27日 下午4:53:04
	 */
	List<ProjectDO> listByCreateDate(@Param("nowDate")String nowDate);
	
	/**
	 * 
	 * @Description: 获取项目综述列表
	 *
	 * @param: 参数描述
	 * @return: 返回结果描述
	 * @throws: 异常描述
	 *
	 * @author: huangmk
	 * @date: 2018年12月28日 下午4:41:24
	 */
	Page<ProjectSummarizeVO> listSummarize(Page<ProjectSummarizeVO> page, @Param("entity")Map<String,Object> map);
	
	/**
	 * 
	 * @Description: 获取项目综述详情
	 *
	 * @param: 项目ID
	 * @return: 返回结果描述
	 * @throws: 异常描述
	 *
	 * @author: huangmk
	 * @date: 2018年12月28日 下午5:53:02
	 */
	ProjectSummarizeVO getSummarize(String id);
}
