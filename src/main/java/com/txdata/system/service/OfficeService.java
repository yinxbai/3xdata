package com.txdata.system.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.txdata.common.persistence.TreeService;
import com.txdata.common.service.BaseService;
import com.txdata.common.utils.CacheUtils;
import com.txdata.common.utils.R;
import com.txdata.system.dao.OfficeDao;
import com.txdata.system.dao.UserDao;
import com.txdata.system.domain.AjaxOffice;
import com.txdata.system.domain.Office;
import com.txdata.system.domain.UserDO;
import com.txdata.system.utils.UserUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 机构Service
 *
 * @author huangmk
 * @version 2018-11-26
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends TreeService<OfficeDao, Office> {

	@Autowired
	private UserDao userDao;

	public List<Office> list(Map<String, Object> map) {
		return dao.list(map);
	}

	public R<?> findOffices(Office office) {
		Map<String, Object> data = new HashMap<String, Object>();
		// 根据机构名称和地区id查找所有机构
		List<Office> list = dao.findByNameLikeAndAreaId(office);
		// 根据名称和区域id查询条数
		int total = dao.findByNameLikeAndAreaIdCount(office);
		JSONArray officeList = new JSONArray();
		for (Office org : list) {
			JSONObject object = new JSONObject();
			object.put("id", org.getId());
			object.put("name", org.getName());
			object.put("areaName", org.getArea().getName());
			object.put("code", org.getCode());
			object.put("useable", org.getUseable());
			String sealPath = org.getSealPath();
			if (StringUtils.isNotBlank(sealPath)) {
				object.put("sealPath", sealPath);
				String[] ss = sealPath.split("/");
				object.put("sealName", ss[ss.length - 1]);
			} else {
				object.put("sealPath", "");

				object.put("sealName", "");
			}
			String szjcSealPath = org.getSzjcSealPath();
			if (StringUtils.isNotBlank(szjcSealPath)) {
				object.put("szjcSealPath", szjcSealPath);
				String[] ss = szjcSealPath.split("/");
				object.put("szjcSealName", ss[ss.length - 1]);
			} else {
				object.put("szjcSealPath", "");

				object.put("szjcSealName", "");
			}
			officeList.add(object);
		}
		data.put("rows", officeList);
		data.put("pageSize", Integer.valueOf(office.getPageSize()));
		data.put("pageNo", Integer.valueOf(office.getPageNo()));
		data.put("count", total);
		return R.success(data);
	}

	/**
	 * 获取机构树
	 *
	 * @param off
	 * @param rootOffice 原始数据
	 * @param isShow     隐藏的是否查询
	 * @return
	 */
	public List<AjaxOffice> getOfficeTree(Office off, List<Office> rootOffice, Boolean isShow) {
		// 最后的结果
		List<AjaxOffice> officeList = new ArrayList<AjaxOffice>();
		if (StringUtils.isNotBlank(off.getName())) {
			for (int i = 0; i < rootOffice.size(); i++) {
				Office office = rootOffice.get(i);
				AjaxOffice ajaxoffice = new AjaxOffice(office, isShow);
				officeList.add(ajaxoffice);
			}
			return officeList;
		}
		Boolean isAll = false;
		// 先找到所有的一级菜单
		for (int i = 0; i < rootOffice.size(); i++) {
			// 一级菜单parentId为0
			if ("0".equals(rootOffice.get(i).getParentId())) {
				Office office = rootOffice.get(i);
				AjaxOffice ajaxoffice = new AjaxOffice(office, isShow);
				officeList.add(ajaxoffice);
				isAll = true;
			}
		}
		if (!isAll) {
			for (int i = 0; i < rootOffice.size(); i++) {
				Office office = rootOffice.get(i);
				AjaxOffice ajaxoffice = new AjaxOffice(office, isShow);
				officeList.add(ajaxoffice);
			}
		}
		// 为一级菜单设置子菜单，getOfficeChild是递归调用的
		for (AjaxOffice office : officeList) {
			office.setChildren(getOfficeChild(office.getId(), rootOffice, isShow));
		}
		return officeList;
	}

	/**
	 * 获取ID为id的机构的子机构
	 *
	 * @param id
	 * @param rootOffice
	 * @param isShow
	 * @return
	 */
	private List<AjaxOffice> getOfficeChild(String id, List<Office> rootOffice, Boolean isShow) {
		// 子菜单
		List<AjaxOffice> childList = new ArrayList<>();
		for (Office office : rootOffice) {
			// 遍历所有节点，将父菜单id与传过来的id比较
			if (StringUtils.isNotBlank(office.getParentId())) {
				if (office.getParentId().equals(id)) {
					AjaxOffice ajaxOffice = new AjaxOffice(office, true);
					childList.add(ajaxOffice);
				}
			}
		}
		// 把子菜单的子菜单再循环一遍
		for (AjaxOffice office : childList) {// 没有url子菜单还有子菜单
			// 递归
			office.setChildren(getOfficeChild(office.getId(), rootOffice, isShow));
		} // 递归退出条件
		if (childList.size() == 0) {
			return null;
		}
		return childList;
	}

	public R<?> getOffice(String id) {
		Office office = dao.get(id);
		if (office == null) {
			return R.error("400", "找不到数据");
		}
		Map<String, Object> data = new HashMap<String, Object>();
		JSONObject object = new JSONObject();
		object.put("parentId", office.getParent() != null ? office.getParentId() : "");
		object.put("parentName", office.getParent() != null ? office.getParent().getName() : "");
		object.put("areaId", office.getArea() != null ? office.getArea().getId() : "");
		object.put("areaName", office.getArea() != null ? office.getArea().getName() : "");
		object.put("name", office.getName());
		object.put("code", office.getCode());
		object.put("uscreditCode", office.getUscreditCode());
		object.put("id", office.getId());
		object.put("useable", office.getUseable());
		object.put("leader",office.getLeader());
		object.put("leaderName",office.getLeaderName());
		object.put("principal",office.getPrincipal());
		object.put("principalName",office.getPrincipalName());
		String sealPath = office.getSealPath();
		if (StringUtils.isNotBlank(sealPath)) {
			object.put("sealPath", sealPath);
			String[] ss = sealPath.split("/");
			object.put("sealName", ss[ss.length - 1]);
		} else {
			object.put("sealPath", "");
			object.put("sealName", "");
		}
		String szjcealPath = office.getSzjcSealPath();
		if (StringUtils.isNotBlank(szjcealPath)) {
			object.put("szjcSealPath", szjcealPath);
			String[] ss = szjcealPath.split("/");
			object.put("szjcSealName", ss[ss.length - 1]);
		} else {
			object.put("szjcSealPath", "");
			object.put("szjcSealName", "");
		}
		data.put("formObject", object);
		return R.success(data);
	}

	public List<Office> findAll() {
		return UserUtils.getOfficeList();
	}

	public List<Office> findList(Boolean isAll) {
		if (isAll != null && isAll) {
			return UserUtils.getOfficeAllList();
		} else {
			return UserUtils.getOfficeList();
		}
	}

	public String getOfficeName(String id) {
		Office office = dao.get(id);
		return office.getName();
	}

	@Transactional(readOnly = true)
	public List<Office> findList(Office office) {
		if (office != null) {
			UserDO user = UserUtils.getUser();
			// 进行数据访问权限过滤
			// office.getSqlMap().put("dsf", BaseService.dataScopeFilter(user, "a", ""));
			office.setParentIds(office.getParentIds());
			return dao.findByParentIdsLike(office);
		}
		return new ArrayList<Office>();
	}

	/**
	 * 根据组织机构id查询该机构及其下属机构下的机构列表
	 *
	 * @param office
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Office> searchList(Office office) {
		if (office != null) {
			UserDO user = UserUtils.getUser();
			// 进行数据访问权限过滤
			// office.getSqlMap().put("dsf", BaseService.dataScopeFilter(user, "a", ""));
			return dao.getListByOfficeId(office);
		}
		return new ArrayList<Office>();
	}

	/**
	 * 根据查询条件(名称和区域)查询机构列表
	 *
	 * @param office
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Office> findByNameAndArea(Office office) {
		if (office != null) {
			UserDO user = UserUtils.getUser();
			// 进行数据访问权限过滤
			office.getSqlMap().put("dsf", BaseService.dataScopeFilter(user, "a", ""));
			return dao.findByNameLikeAndAreaId(office);
		}
		return new ArrayList<Office>();
	}

	public boolean isRoot(Office office) {
		if (StringUtils.isBlank(office.getId()) || (dao.countChildOffice(office) > 0)) {
			return true;
		}
		return false;
	}

	@Transactional(readOnly = false)
	public int save(Office office) {
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
		CacheUtils.remove(UserUtils.USER_CACHE, UserUtils.CACHE_OFFICE_ALL_LIST);
		UserUtils.removeCache("officeTree");
		UserUtils.removeCache("officeUserTree");
		return super.save(office);
	}

	@Transactional(readOnly = false)
	public void delete(Office office) {
		super.delete(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
		CacheUtils.remove(UserUtils.USER_CACHE, UserUtils.CACHE_OFFICE_ALL_LIST);
		UserUtils.removeCache("officeTree");
		UserUtils.removeCache("officeUserTree");
	}

	// public String findChildOffice(String officeId, String ctxStatic){
	// if(StringUtils.isBlank(officeId)){
	// officeId = UserUtils.UNKONW_OFFICE_ID;
	// }
	// return UserUtils.getOfficeTree(ctxStatic, officeId);
	// }
	/**
	 * 按资源分类获取树级菜单
	 *
	 * @param officeId
	 * @param ctxStatic
	 * @return
	 */
	// public String findChildOfficeNav(String officeId, String ctxStatic){
	// if(StringUtils.isBlank(officeId)){
	// officeId = UserUtils.UNKONW_OFFICE_ID;
	// }
	// return UserUtils.getOfficeTreeNav(ctxStatic, officeId);
	// }

	/**
	 * 根据父级id查找子部门
	 *
	 * @param office
	 * @return
	 */
	public List<Office> findByparentId(Office office) {
		List<Office> list = dao.findByparentId(office);
		return list;
	}

	/**
	 * 根据机构编码来获取机构列表（主要用来做机构唯一性判断）
	 *
	 * @param office
	 * @return
	 */
	public List<Office> findByCode(Office office) {
		List<Office> list = dao.findByCode(office);
		return list;
	}

	public List<Office> findByName(Office office) {
		List<Office> list = dao.findByName(office);
		return list;
	}

	/**
	 * 根据部门code获取部门信息
	 *
	 * @param code
	 * @return
	 */
	public Office getOfficeNameByCode(String code) {
		return dao.getOfficeNameByCode(code);
	}

	/**
	 * 根据机构ID查找它有几个下级机构
	 *
	 * @param office
	 * @return
	 */
	public Integer countChildOffice(Office office) {
		return dao.countChildOffice(office);
	}

	/**
	 * 获取机构数
	 *
	 * @param rootOffice
	 * @param isUser     是否查询机构下的用户
	 * @return
	 */
	public List<AjaxOffice> getNewOfficeTree(List<AjaxOffice> rootOffice, Boolean isUser) {
		// 最后的结果
		List<AjaxOffice> officeList = new ArrayList<AjaxOffice>();
		// 先找到所有的一级菜单
		for (int i = 0; i < rootOffice.size(); i++) {
			// 一级菜单parentId为0
			if ("0".equals(rootOffice.get(i).getParentId())) {
				AjaxOffice office = rootOffice.get(i);
				officeList.add(office);
			}
		}
//		List<UserDO> users = userDao.findUserByNotInOfficeId();
//		boolean flag = true;
		// 为一级菜单设置子菜单，getChild是递归调用的
		for (AjaxOffice office : officeList) {
			office.setChildren(getNewOfficeChild(office.getId(), rootOffice, isUser));
			if (isUser && isUser != null) { // 机构数下加上相关人员
				UserDO user = new UserDO();
				user.setOfficeId(office.getId());
				List<UserDO> userList = userDao.findUserByOfficeId(user);
				List<AjaxOffice> uaList = new ArrayList<AjaxOffice>();
				for (UserDO u : userList) {
					AjaxOffice aa = new AjaxOffice(u, office.getId());
					uaList.add(aa);
				}
//				if (flag) {
//					for (UserDO u : users) {
//						AjaxOffice aa = new AjaxOffice(u);
//						uaList.add(aa);
//					}
//					flag = false;
//				}
				List<AjaxOffice> childList = office.getChildren();
				if (childList != null) {
					childList.addAll(uaList);
					office.setChildren(childList);
				} else {
					office.setChildren(uaList);
				}
			}
		}
		return officeList;
	}

	/**
	 * 获取ID为id的子菜单
	 *
	 * @param id
	 * @param rootOffice 源机构，所有机构
	 * @param isUser
	 * @return
	 */
	private List<AjaxOffice> getNewOfficeChild(String id, List<AjaxOffice> rootOffice, Boolean isUser) {
		// 子菜单
		List<AjaxOffice> ajaxChildList = new ArrayList<>();
		List<Office> childList = findByparentId(new Office(id));
		// 把子菜单的子菜单再循环一遍
		for (Office office : childList) {// 没有url子菜单还有子菜单
			// 递归
			AjaxOffice ao = new AjaxOffice(office, true);
//			office.setChildren(getNewOfficeChild(office.getId(), rootOffice, isShow, isUser));
			ao.setChildren(getNewOfficeChild(office.getId(), rootOffice, isUser));
			if (isUser != null && isUser) {
				UserDO user = new UserDO();
				user.setOfficeId(office.getId());
				List<UserDO> userList = userDao.findUserByOfficeId(user);
				List<AjaxOffice> uaList = new ArrayList<AjaxOffice>();
				for (UserDO u : userList) {
					AjaxOffice aa = new AjaxOffice(u, office.getId());
					uaList.add(aa);
				}
				List<AjaxOffice> cList = ao.getChildren();
				if (cList == null) {
					cList = new ArrayList<AjaxOffice>();
				}
				cList.addAll(uaList);
				ao.setChildren(cList);
			}
			ajaxChildList.add(ao);
		} // 递归退出条件
		if (ajaxChildList.size() == 0) {
			return null;
		}
		return ajaxChildList;
	}

	/**
	 * 校验机构名称和机构编码
	 *
	 * @param id   id
	 * @param name 机构名称
	 * @param code 机构编码
	 * @return
	 */
	public List<Office> checkNameCode(String id, String name, String code) {
		List<Office> officeList = new ArrayList<Office>();
		if (StringUtils.isNotBlank(name)) { // 校验name
			Office office = new Office();
			office.setName(name);
			// 查询机构列表
			officeList = dao.findByName(office);
			if (StringUtils.isNotBlank(id)) {
				for (Office o : officeList) {
					if (id.equals(o.getId())) {
						officeList.remove(o);
						break;
					}
				}
			}
			return officeList;
		}

		if (StringUtils.isNotBlank(code)) { // 校验code
			Office office = new Office();
			office.setCode(code);
			officeList = dao.findByCode(office);
			if (StringUtils.isNotBlank(id)) {
				for (Office o : officeList) {
					if (id.equals(o.getId())) {
						officeList.remove(o);
						break;
					}
				}
			}
			return officeList;
		}
		return officeList;
	}
}
