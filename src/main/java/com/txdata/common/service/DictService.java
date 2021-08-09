package com.txdata.common.service;

import com.txdata.common.dao.DictDao;
import com.txdata.common.domain.DictDO;
import com.txdata.common.persistence.CrudService;
import com.txdata.common.utils.StringUtils;
import com.txdata.system.domain.UserDO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 字典表
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-29 18:28:07
 */
@Service
public class DictService extends CrudService<DictDao, DictDO> {
	
	@Autowired
	private DictDao dictDao;

	@Override
	public DictDO get(String id) {
		return dictDao.get(id);
	}

	@Override
	public List<DictDO> list(Map<String, Object> map) {
		return dictDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return dictDao.count(map);
	}

	@Override
	@Transactional(readOnly = false)
	public int save(DictDO dict) {
		dict.preInsert();
		return dictDao.save(dict);
	}

	@Override
	@Transactional(readOnly = false)
	public int update(DictDO dict) {
		dict.preUpdate();
		return dictDao.update(dict);
	}

	@Override
	@Transactional(readOnly = false)
	public int remove(String id) {
		return dictDao.remove(id);
	}

	@Transactional(readOnly = false)
	public int batchRemove(String[] ids) {
		return dictDao.batchRemove(ids);
	}

	public List<DictDO> listType() {
		return dictDao.listType();
	}

	/**
	 * 根据字典的类型以及对应的value值获取字典名称
	 * 
	 * @param type
	 * @param value
	 * @return
	 */
	public String getName(String type, String value) {
		Map<String, Object> param = new HashMap<String, Object>(16);
		param.put("type", type);
		param.put("value", value);
		List<DictDO> list = dictDao.list(param);
		String rString = "";
		if (list != null && list.size() > 0) {
			rString = list.get(0).getName();
		}
		return rString;
	}

	/**
	 * 获取类型为sex的字典列表
	 * 
	 * @return
	 */
	public List<DictDO> getSexList() {
		Map<String, Object> param = new HashMap<>(16);
		param.put("type", "sex");
		return dictDao.list(param);
	}

	/**
	 * 根据字典的类型获取字典列表
	 * 
	 * @param type 字典类型
	 * @return
	 */
	public List<DictDO> listByType(String type) {
		Map<String, Object> param = new HashMap<>(16);
		param.put("type", type);
		return dictDao.list(param);
	}

	/**
	 * 获取所有的字典类型
	 * 
	 * @return
	 */
	public List<String> findTypeList() {
		return dictDao.findTypeList(new DictDO());
	}
}
