package com.txdata.modules.project.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.modules.project.domain.ProjectMemberDO;
import com.txdata.common.persistence.proxy.CrudDao;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 项目团队成员关联表
 * 
 * @author huangmk
 * @email huangmk@3xdata.cn
 * @date 2018-12-27 11:44:22
 */
@Mapper
public interface ProjectMemberDao extends CrudDao<ProjectMemberDO> {

	ProjectMemberDO get(String id);

	Page<ProjectMemberDO> list(Page<ProjectMemberDO> page, @Param("entity") Map<String, Object> map);

	List<ProjectMemberDO> list(@Param("entity") Map<String, Object> map);

	int insert(ProjectMemberDO projectMember);

	int update(ProjectMemberDO projectMember);

	int remove(String id);

	int batchRemove(String[] ids);
	
	/**
	 * 
	 * @Description: 根据项目ID删除关联的项目成员信息
	 *
	 * @param: 项目ID
	 * @return: 删除条数
	 *
	 * @author: huangmk
	 * @date: 2018年12月27日 下午2:17:11
	 */
	int deleteByProject(String projectId);
}
