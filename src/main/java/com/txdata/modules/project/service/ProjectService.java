package com.txdata.modules.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.modules.project.domain.ProjectBidInfoDO;
import com.txdata.modules.project.domain.ProjectDO;
import com.txdata.modules.project.domain.ProjectMemberDO;
import com.txdata.modules.project.vo.MemberVO;
import com.txdata.system.dao.OfficeDao;
import com.txdata.system.dao.UserDao;
import com.txdata.system.domain.Office;
import com.txdata.system.domain.UserDO;
import com.txdata.system.utils.UserUtils;
import com.txdata.modules.project.dao.ProjectBidInfoDao;
import com.txdata.modules.project.dao.ProjectDao;
import com.txdata.modules.project.dao.ProjectMemberDao;
import com.txdata.common.persistence.CrudService;
import com.txdata.common.utils.DateUtils;
import com.txdata.common.utils.IdGen;
import com.txdata.common.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

/**
 * 项目备案相关Service
 * 
 * @author huangmk
 * @email huangmk@3xdata.cn
 * @date 2018-12-27 10:59:18
 */
 @Service
public class ProjectService extends CrudService<ProjectDao, ProjectDO> {

    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private ProjectBidInfoDao projectBidInfoDao;
    @Autowired
    private ProjectMemberDao projectMemberDao;
    @Autowired
    private UserDao userDao;
    public ProjectDO get(String id){
        return projectDao.get(id);
    }
    
    /**
     * 
     * @Description: 获取完整项目信息
     * 包括基本信息、招投标信息、关联项目成员信息
     *
     * @param: id 项目ID
     * @return: 返回结果描述
     * @throws: 异常描述
     *
     * @author: mark
     * @date: 2018年12月27日 下午4:16:32
     */
    public ProjectDO getInfo(String id){
    		// 获取项目基本信息以及关联的招投标信息
    		ProjectDO project = projectDao.get(id);
    		return project;
    }
    
    
    public Page<ProjectDO> page(Page<ProjectDO> page, Map<String, Object> map){
        Page<ProjectDO> projecPage = projectDao.list(page, map);
        List<ProjectDO> projectList = projecPage.getRecords();
        return projecPage;
    }
    
    public List<ProjectDO> list(Map<String, Object> map){
        return projectDao.list(map);
    }
    
    /**
     * 
     * @Description: 保存项目备案信息
     *
     * @param: 参数描述
     * @return: 返回结果描述
     * @throws: 异常描述
     *
     * @author: huangmk
     * @date: 2018年12月27日 下午3:16:00
     */
    @Transactional(readOnly=false)
    public int save(ProjectDO project){
//    		if (StringUtils.isBlank(project.getId())) {
//    			project.preInsert();
//    		} else {
//    			project.preUpdate();
//    		}
    		// 保存到项目备案主表
    		int row = super.save(project);
    		if(row < 1) {
    			return row;
    		}
    		// 获取招投标信息
    		ProjectBidInfoDO projectBidInfo = new ProjectBidInfoDO(project);
    		// 获取关联的用户ID
    		String memberids = project.getMemberIds();
    		List<ProjectMemberDO> memberList = new ArrayList<ProjectMemberDO>();
    		if (StringUtils.isNotBlank(memberids)) {
    			String[] ids = memberids.split(",");
    			for (String id : ids) {
    				ProjectMemberDO member = new ProjectMemberDO();
    				member.setUserId(id);
    				member.setProjectId(project.getId());
    				memberList.add(member);
			}
    		}
    		// 把旧的招投标信息删除
    		projectBidInfoDao.deleteByProject(project.getId());
    		// 保存招投标信息
    		projectBidInfo.setId(IdGen.uuid());
    		projectBidInfoDao.insert(projectBidInfo);
    		// 把旧的关联用户删除
    		projectMemberDao.deleteByProject(project.getId());
    		// 保存新的关联用户
    	    for (ProjectMemberDO member : memberList) {
    	    		member.setId(IdGen.uuid());
    	    		projectMemberDao.insert(member);
		}
        return row;
    }
    
    @Transactional(readOnly=false)
    public int remove(String id){
        return projectDao.remove(id);
    }
    
    /**
     * 
     * @Description: 更新项目状态 启用、停用
     *
     * @param: id 项目ID
     * @param: status 项目状态 1-启用，2-关闭
     * @return: 返回结果描述
     * @throws: 异常描述
     *
     * @author: mark
     * @date: 2018年12月27日 下午3:17:26
     */
    @Transactional(readOnly=false)
    public int updateStatus(String id, String status) {
    		ProjectDO project = new ProjectDO();
    		project.setId(id);
    		project.setStatus(status);
    		return projectDao.update(project);
    }
    
    /**
     * 
     * @Description: 获取项目编号
	 * 编号规则：X3DATA年月日01，如X3DATA2018122701
     *
     * @param: 参数描述
     * @return: 返回结果描述
     * @throws: 异常描述
     *
     * @author: mark
     * @date: 2018年12月27日 下午4:23:17
     */
    public String getProjectCode() {
    		// 获取当前日期
    		String nowDate = DateUtils.getDate("yyyyMMdd");
    		// 获取当前日期已经创建好了几个项目
    		List<ProjectDO> list = projectDao.listByCreateDate(nowDate);
    		if(list == null || list.size() == 0) {
    			return "XM"+nowDate+"01";
    		}else {
    			// 取最新的一条，查询出的列表是按创建时间排序的
    			ProjectDO project = list.get(0);
    			String code = project.getCode();
    			String numStr = code.substring(code.length()-2);
    			Integer num = Integer.valueOf(numStr);
    			num++;
			if(num.toString().length() < 2) {
				numStr = "0"+num;
			}else {
				numStr = ""+num;
			}
			return "XM"+nowDate+numStr;
    		}
    }
}
