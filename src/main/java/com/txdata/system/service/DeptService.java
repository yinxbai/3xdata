package com.txdata.system.service;

import com.txdata.common.domain.Tree;
import com.txdata.common.persistence.CrudService;
import com.txdata.common.utils.BuildTree;
import com.txdata.system.dao.DeptDao;
import com.txdata.system.domain.DeptDO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 部门管理
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-27 14:28:36
 */
@Service
public class DeptService extends CrudService<DeptDao, DeptDO> {

	@Autowired
	private DeptDao sysDeptMapper;

	@Override
	public DeptDO get(String deptId) {
		return sysDeptMapper.get(deptId);
	}

	@Override
	public List<DeptDO> list(Map<String, Object> map) {
		return sysDeptMapper.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return sysDeptMapper.count(map);
	}

	public int save(DeptDO sysDept) {
		sysDept.preInsert();
		return sysDeptMapper.save(sysDept);
	}

	public int update(DeptDO sysDept) {
		sysDept.preUpdate();
		return sysDeptMapper.update(sysDept);
	}

	public int remove(String deptId) {
		return sysDeptMapper.remove(deptId);
	}

	public int batchRemove(String[] deptIds) {
		return sysDeptMapper.batchRemove(deptIds);
	}

	public Tree<DeptDO> getTree() {
		List<Tree<DeptDO>> trees = new ArrayList<Tree<DeptDO>>();
		List<DeptDO> sysDepts = sysDeptMapper.list(new HashMap<String, Object>(16));
		for (DeptDO sysDept : sysDepts) {
			Tree<DeptDO> tree = new Tree<DeptDO>();
			tree.setId(sysDept.getId());
			tree.setParentId(sysDept.getParentId().toString());
			tree.setText(sysDept.getName());
			Map<String, Object> state = new HashMap<>(16);
			state.put("opened", true);
			tree.setState(state);
			trees.add(tree);
		}
		// 默认顶级菜单为０，根据数据库实际情况调整
		Tree<DeptDO> t = BuildTree.build(trees);
		return t;
	}

	public boolean checkDeptHasUser(String deptId) {
		// TODO Auto-generated method stub
		// 查询部门以及此部门的下级部门
		int result = sysDeptMapper.getDeptUserNumber(deptId);
		return result == 0 ? true : false;
	}

	public List<String> listChildrenIds(String parentId) {
		List<DeptDO> deptDOS = list(null);
		return treeMenuList(deptDOS, parentId);
	}

	List<String> treeMenuList(List<DeptDO> menuList, String pid) {
		List<String> childIds = new ArrayList<>();
		for (DeptDO mu : menuList) {
			// 遍历出父id等于参数的id，add进子节点集合
			if (pid.equals(mu.getParentId())) {
				// 递归遍历下一级
				treeMenuList(menuList, mu.getId());
				childIds.add(mu.getId());
			}
		}
		return childIds;
	}
}
