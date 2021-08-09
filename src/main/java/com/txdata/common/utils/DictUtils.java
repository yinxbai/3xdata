package com.txdata.common.utils;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.txdata.common.dao.DictDao;
import com.txdata.common.domain.DictDO;
import com.txdata.common.mapper.JsonMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 字典工具类
 * 
 * @author ThinkGem
 * @version 2013-5-29
 */
public class DictUtils {

	private static DictDao dictDao = SpringContextHolder.getBean(DictDao.class);

	public static final String CACHE_DICT_MAP = "dictMap";

	/**
	 * 获取字典类型名称
	 * 
	 * @param value
	 * @param type
	 * @param defaultValue
	 * @return
	 */
	public static String getDictLabel(String value, String type, String defaultValue) {
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)) {
			for (DictDO dict : getDictList(type)) {
				if (type.equals(dict.getType()) && value.equals(dict.getValue())) {
					return dict.getName();
				}
			}
		}
		return defaultValue;
	}

	// 根据标签值和字典类型查找对应的字典ID
	public static String getDictId(String label, String type, String defaultValue) {
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)) {
			for (DictDO dict : getDictList(type)) {
				if (type.equals(dict.getType()) && label.equals(dict.getName())) {
					return dict.getId();
				}
			}
		}
		return defaultValue;
	}

	public static String getDictLabels(String values, String type, String defaultValue) {
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(values)) {
			List<String> valueList = Lists.newArrayList();
			for (String value : StringUtils.split(values, ",")) {
				valueList.add(getDictLabel(value, type, defaultValue));
			}
			return StringUtils.join(valueList, ",");
		}
		return defaultValue;
	}

	public static String getDictValue(String label, String type, String defaultLabel) {
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)) {
			for (DictDO dict : getDictList(type)) {
				if (type.equalsIgnoreCase(dict.getType()) && label.equalsIgnoreCase(dict.getName())) {
					return dict.getValue();
				}
			}
		}
		return defaultLabel;
	}

	public static List<DictDO> getDictList(String type) {
		@SuppressWarnings("unchecked")
		Map<String, List<DictDO>> dictMap = (Map<String, List<DictDO>>) CacheUtils.get(CACHE_DICT_MAP);
		if (dictMap == null) {
			dictMap = Maps.newHashMap();
			for (DictDO dict : dictDao.findAllList(new DictDO())) {
				List<DictDO> dictList = dictMap.get(dict.getType());
				if (dictList != null) {
					dictList.add(dict);
				} else {
					dictMap.put(dict.getType(), Lists.newArrayList(dict));
				}
			}
			CacheUtils.put(CACHE_DICT_MAP, dictMap);
		}
		List<DictDO> dictList = dictMap.get(type);
		if (dictList == null) {
			dictList = Lists.newArrayList();
		}
		return dictList;
	}

	/**
	 * 返回字典列表（JSON）
	 * 
	 * @param type
	 * @return
	 */
	public static String getDictListJson(String type) {
		return JsonMapper.toJsonString(getDictList(type));
	}

	/**
	 * 获取字典描述
	 * 
	 * @param value
	 * @param type
	 * @return
	 */
	public static String getDictDiscription(String value, String type) {
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)) {
			for (DictDO dict : getDictList(type)) {
				if (type.equals(dict.getType()) && value.equals(dict.getValue())) {
					return dict.getDescription();
				}
			}
		}
		return "";
	}

	/**
	 * 根据字典ID获取字典名称
	 * 
	 * @param id
	 * @return
	 */
	public static String getDictById(String id) {
		DictDO dict = dictDao.get(id);
		return dict == null ? "" : dict.getName();
	}

	/**
	 * 根据字典ID和类型获取字典名称
	 * 
	 * @param id
	 * @param type
	 * @param defaultValue
	 * @return
	 */
	public static String getDictById(String id, String type, String defaultValue) {
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(id)) {
			for (DictDO dict : getDictList(type)) {
				if (type.equals(dict.getType()) && id.equals(dict.getId())) {
					return dict.getName();
				}
			}
		}
		return defaultValue;
	}
}
