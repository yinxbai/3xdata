package com.txdata.system.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.txdata.common.domain.Tree;
import com.txdata.common.persistence.TreeService;
import com.txdata.common.utils.BuildTree;
import com.txdata.common.utils.CacheUtils;
import com.txdata.common.utils.R;
import com.txdata.common.utils.StringUtils;
import com.txdata.system.dao.AreaDao;
import com.txdata.system.domain.AjaxArea;
import com.txdata.system.domain.Area;
import com.txdata.system.utils.UserUtils;

import net.sf.json.JSONObject;

/**
 * 区域Service
 * 
 * @author ThinkGem
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class AreaService extends TreeService<AreaDao, Area> {

	/**
	 * 根据父级id查询列表
	 * 
	 * @param area
	 * @return
	 */
	public List<Map<String, Object>> findByParentId(Area area) {
		return dao.findByParentId(area);
	}

	/**
	 * 根据id获取区域信息
	 * 
	 * @param id
	 * @return
	 */
	public R<?> getArea(String id) {
		Area area = dao.get(id);// 获取区域对象
		JSONObject object = new JSONObject();
		object.put("parentId", area.getParentId());
		String parentName = "";
		if (StringUtils.isNotBlank(area.getParentId())) {
			Area parent = dao.get(area.getParentId());// 获取父级区域
			if (parent != null) {
				parentName = parent.getName();
			}
		}
		object.put("parentName", parentName);
		object.put("id", area.getId());
		object.put("name", area.getName());
		object.put("code", area.getCode());
		object.put("type", area.getType());
		object.put("remarks", area.getRemarks());
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("formObject", object);
		return R.success(data);
	}

	/**
	 * 获取区域树
	 * 
	 * @param rootArea   原始的数据
	 * @param isAllGrade
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AjaxArea> getAreaTree(List<Area> rootArea, Boolean isAllGrade) {
		// 最后的结果
		List<AjaxArea> areaList = new ArrayList<AjaxArea>();
		areaList= (List<AjaxArea>)UserUtils.getCache("AREA_LIST");
		if (areaList != null) {
			return areaList;
		}else {
			areaList = new ArrayList<AjaxArea>();
		}
		// 先找到所有的一级菜单
		for (int i = 0; i < rootArea.size(); i++) {
			// 一级菜单parentId为0
			if ("0".equals(rootArea.get(i).getParentId())) {
				Area area = rootArea.get(i);
				AjaxArea ajaxarea = new AjaxArea(area, false);
				areaList.add(ajaxarea);
			}
		}
		// 为一级地区设置子地区，getAreaChild是递归调用的
		for (AjaxArea area : areaList) {
			area.setSubs(getAreaChild(area.getId(), rootArea, isAllGrade));// 获取子区域
		}
		UserUtils.putCache("AREA_LIST", areaList);
		return areaList;
	}

	/**
	 * 获取子区域
	 * 
	 * @param id         父级ID
	 * @param rootArea   源数据，所有的地区
	 * @param isAllGrade 是否全部级别
	 * @return
	 */
	private static List<AjaxArea> getAreaChild(String id, List<Area> rootArea, Boolean isAllGrade) {
		// 子菜单
		List<AjaxArea> childList = new ArrayList<>();
		for (Area area : rootArea) {
			// 遍历所有节点，将父菜单id与传过来的id比较
			if (StringUtils.isNotBlank(area.getParentId())) {
				if (area.getParentId().equals(id)) {
					AjaxArea ajaxArea = new AjaxArea(area, true);
					childList.add(ajaxArea);
				}
			}
		}
		// 把子地区的子地区再循环一遍
		for (AjaxArea area : childList) {
			if (isAllGrade != null && !isAllGrade) { // 不显示四级地区
				String parentIds = area.getParentIds();
				String[] ps = parentIds.split(",");
				if (ps.length > 3) {
					break;
				}
			}
			// 递归
			area.setSubs(getAreaChild(area.getId(), rootArea, isAllGrade));
		} // 递归退出条件
		if (childList.size() == 0) {
			return null;
		}
		return childList;
	}
	
	/**
	 * 
	 * @Description: 获取区域树
	 *
	 * @param: 参数描述
	 * @return: 返回结果描述
	 * @throws: 异常描述
	 *
	 * @author: huangmk
	 * @date: 2019年1月10日 下午8:26:27
	 */
	public List<Tree<AjaxArea>> rootAreaTreeList(List<Area> list) {
		Object obj = CacheUtils.get(UserUtils.CACHE_AREA_ROOT_TREE);
		if (obj == null) {
			List<Tree<AjaxArea>> treeList = areaTreeListById(list, "0");
			CacheUtils.put(UserUtils.CACHE_AREA_ROOT_TREE, treeList);
			return treeList;
		} else {
			List<Tree<AjaxArea>> treeList = (List<Tree<AjaxArea>>)obj;
			return treeList;
		}
	}
	
	/**
	 * 
	 * @Description: 获取ID为idParam的区域的子区域树形列表
	 *
	 * @param: list 某个区域下的所有区域列表
	 * @param: idParam 要查询的某个区域的ID
	 * @return: 返回结果描述
	 * @throws: 异常描述
	 *
	 * @author: huangmk
	 * @date: 2019年1月10日 下午8:20:28
	 */
	public List<Tree<AjaxArea>> areaTreeListById(List<Area> list, String idParam) {
		List<Tree<AjaxArea>> trees = new ArrayList<Tree<AjaxArea>>();
		for (Area area : list) {
			AjaxArea ajaxarea = new AjaxArea(area, true);
			Tree<AjaxArea> tree = new Tree<AjaxArea>();
			tree.setId(ajaxarea.getId());
			tree.setText(ajaxarea.getName());
			tree.setParentId(ajaxarea.getParentId());
			Map<String, Object> state = new HashMap<>(16);
			state.put("opened", true);
			tree.setState(state);
			tree.setParentId(ajaxarea.getParentId());
			tree.setCode(ajaxarea.getCode());
			tree.setType(ajaxarea.getType());
			// 设置层级
			String parentIds = ajaxarea.getParentIds();
			parentIds = parentIds.substring(0, parentIds.length()-1);
			String[] arr = parentIds.split(",");
			tree.setLevel(arr.length);
			trees.add(tree);
		}
		List<Tree<AjaxArea>> t = BuildTree.buildList(trees, idParam);
		return t;
	}

	/**
	 * 获取当前用户授权的区域
	 * 
	 * @return
	 */
	public List<Area> findAll() {
		return UserUtils.getAreaList();
	}

	@Transactional(readOnly = false)
	public int save(Area area) {
		// 先移除缓存
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
		CacheUtils.remove(UserUtils.USER_CACHE, UserUtils.CACHE_AREA_ROOT_TREE);
		return super.save(area);
	}

	@Transactional(readOnly = false)
	public void delete(Area area) {
		dao.delete(area);
		// 移除缓存
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
		CacheUtils.remove(UserUtils.USER_CACHE, UserUtils.CACHE_AREA_ROOT_TREE);
	}
}
