package com.txdata.modules.project.controller;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.common.controller.BaseController;
import com.txdata.common.utils.StringUtils;
import com.txdata.modules.project.domain.ProjectDO;
import com.txdata.modules.project.service.ProjectService;
import com.txdata.common.utils.Query;
import com.txdata.common.utils.R;

/**
 * 项目备案基本信息表
 * 
 * @author huangmk
 * @email huangmk@3xdata.cn
 * @date 2018-12-27 10:59:18
 */
@Controller
@RequestMapping("/project")
public class ProjectController extends BaseController {
	@Autowired
	private ProjectService projectService;
	
	/**
	 * 
	 * @Description: 获取项目分页列表
	 * 只查询项目主表
	 *
	 * @param: 查询参数
	 * @return: 返回结果描述
	 * @throws: 异常描述
	 *
	 * @author: huangmk
	 * @date: 2018年12月27日 上午11:56:00
	 */
	@ResponseBody
	@PostMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
        Page<ProjectDO> page = new Page<ProjectDO>(query.getPageNo(), query.getPageSize());
		page = projectService.page(page, query);
		// 封装分页数据
		Map<String,Object> jsonMap = new HashMap<String,Object>();
        jsonMap.put("rows", page.getRecords());
        jsonMap.put("pageSize", page.getSize());
        jsonMap.put("pageNo", page.getCurrent());
        jsonMap.put("count", page.getTotal());
        return R.success(jsonMap);
	}
	
	/**
	 * 
	 * @Description: 新增、修改项目备案信息
	 *
	 * @param: 参数描述
	 * @return: 返回结果描述
	 * @throws: 异常描述
	 *
	 * @author: huangmk
	 * @date: 2018年12月27日 下午3:02:01
	 */
	@ResponseBody
	@PostMapping("/save")
	public R save(ProjectDO project){
		if(projectService.save(project)>0){
			return R.success();
		}
		return R.error();
	}
	
	@PostMapping( "/delete")
	@ResponseBody
	public R remove(String id){
		if(projectService.remove(id)>0){
			return R.success();
		}
		return R.error();
	}
	
	/**
	 * 
	 * @Description: 查看项目备案详情
	 *
	 * @param: id 项目ID
	 * @return: 返回结果描述
	 * @throws: 异常描述
	 *
	 * @author: huangmk
	 * @date: 2018年12月27日 下午3:14:22
	 */
	@PostMapping( "/form")
	@ResponseBody
	public R form(@RequestParam(required = true)String id) {
		ProjectDO project = projectService.getInfo(id);
		Map<String, Object> map = new HashMap<String, Object>();
		if (project == null) {
			return R.error("查无此数据！");
		}
		map.put("project", project);
		return R.success(map);
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
	 * @author: huangmk
	 * @date: 2018年12月27日 下午3:21:01
	 */
	@PostMapping( "/updateStatus")
	@ResponseBody
	public R updateStatus(String id, String status) {
		int row = projectService.updateStatus(id, status);
		if(row > 0){
			return R.success();
		}
		return R.error();
	}
	
	/**
	 * 
	 * @Description: 获取项目编号
	 * 编号规则：XM+年月日+两位数字，如XM2018122701
	 *
	 * @param: 参数描述
	 * @return: 返回结果描述
	 * @throws: 异常描述
	 *
	 * @author: mark
	 * @date: 2018年12月27日 下午4:04:46
	 */
	@PostMapping( "/getProjectCode")
	@ResponseBody
	public R getProjectCode () {
		String code = projectService.getProjectCode();
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("code", code);
		return R.success(jsonMap);
	}
	
	/**
	 * 
	 * @Description: 项目名称唯一性校验
	 *
	 * @param: 参数描述
	 * @return: 返回结果描述
	 * @throws: 异常描述
	 *
	 * @author: mark
	 * @date: 2019年1月22日 下午2:48:12
	 */
	@PostMapping( "/validateName")
	@ResponseBody
	public R validateName(String id, String name) {
		if (StringUtils.isBlank(name)) {
			return R.error("缺少参数");
		}
		boolean flag = true;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("projectName", name);
		List<ProjectDO> list = projectService.list(map);
		if (StringUtils.isBlank(id)) {	// 无ID（新增时）
			if (list != null && list.size() > 0) {
				flag = false;
			}
		} else {		// 有ID（编辑时）
			if (list != null && list.size() > 0) {
				for (ProjectDO project : list) {
					if (!id.equals(project.getId())) {
						flag = false;
					}
				}
			}
		}
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("flag", flag);
		return R.success(jsonMap);
	}
}
