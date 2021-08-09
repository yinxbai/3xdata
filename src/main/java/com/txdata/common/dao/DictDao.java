package com.txdata.common.dao;

import com.txdata.common.domain.DictDO;
import com.txdata.common.persistence.proxy.CrudDao;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典表
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-03 15:45:42
 */
@Mapper
public interface DictDao extends CrudDao<DictDO> {

	DictDO get(String id);

	List<DictDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(DictDO dict);

	int update(DictDO dict);

	int remove(String id);

	int batchRemove(String[] ids);

	List<DictDO> listType();

	/**
	 * 获取字典类型列表
	 * 
	 * @param dict
	 * @return
	 */
	List<String> findTypeList(DictDO dict);
}
