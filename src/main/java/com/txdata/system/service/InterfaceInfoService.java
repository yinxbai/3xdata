package com.txdata.system.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.txdata.common.persistence.CrudService;
import com.txdata.system.dao.InterfaceInfoDao;
import com.txdata.system.domain.InterfaceInfo;
import com.txdata.system.domain.InterfaceParam;

/**
 * 接口管理Service
 * 
 * @author lmh
 * @version 2018-07-24
 */
@Service
public class InterfaceInfoService extends CrudService<InterfaceInfoDao, InterfaceInfo> {

	@Autowired
	private InterfaceInfoDao dao;

	protected boolean isNewRecord = false;

	public InterfaceInfo get(String id) {
		return dao.get(id);
	}

	public List<InterfaceInfo> findList(InterfaceInfo interfaceInfo) {
		return dao.findList(interfaceInfo);
	}

	@Transactional(readOnly = false)
	public int save(InterfaceInfo interfaceInfo) {
		int row = 0;
		// 判断是否为新记录
		if (interfaceInfo.getIsNewRecord()) {
			super.save(interfaceInfo);
		} else {
			dao.deleteParam(interfaceInfo);
			super.save(interfaceInfo);
		}
		List<InterfaceParam> paramList = interfaceInfo.getParamList();
		for (InterfaceParam interfaceParam : paramList) {
			interfaceParam.setInterfaceId(interfaceInfo.getId());
			interfaceParam.preInsert();
			dao.insertParam(interfaceParam);
		}
		return row;
	}

	@Transactional(readOnly = false)
	public int delete(String id) {
		InterfaceInfo info = new InterfaceInfo();
		info.setId(id);
		dao.deleteParam(info);
		return dao.delete(info);
	}

	/**
	 * 接口名称校验
	 * 
	 * @param interfaceInfo
	 * @return
	 */
	public boolean nameValidate(InterfaceInfo interfaceInfo) {
		if (interfaceInfo.getInterfaceType() == null) {
			return true;
		}
		// 验证接口名称是否重复
		List<InterfaceInfo> list = this.dao.nameValidate(interfaceInfo);
		if (list.size() < 1) {
			return true;
		}
		return false;
	}

	@Override
	public List<InterfaceInfo> list(Map<String, Object> map) {
		List<InterfaceInfo> list = dao.list(map);
		return list;
	}

	@Override
	public int count(Map<String, Object> map) {
		return dao.count(map);
	}
}