package com.txdata.common.service;

import com.txdata.common.config.BootdoConfig;
import com.txdata.common.dao.FileDao;
import com.txdata.common.domain.FileDO;
import com.txdata.common.persistence.CrudService;
import java.io.File;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 文件上传
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-19 16:02:20
 */
@Service
public class FileService extends CrudService<FileDao, FileDO> {
	
	@Autowired
	private FileDao sysFileMapper;
	@Autowired
	private BootdoConfig bootdoConfig;

	@Override
	public FileDO get(String id) {
		return sysFileMapper.get(id);
	}

	@Override
	public List<FileDO> list(Map<String, Object> map) {
		return sysFileMapper.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return sysFileMapper.count(map);
	}

	@Override
	public int save(FileDO sysFile) {
		sysFile.preInsert();
		return sysFileMapper.save(sysFile);
	}

	@Override
	public int update(FileDO sysFile) {
		sysFile.preUpdate();
		return sysFileMapper.update(sysFile);
	}

	@Override
	public int remove(String id) {
		return sysFileMapper.remove(id);
	}

	public int batchRemove(String[] ids) {
		return sysFileMapper.batchRemove(ids);
	}

	public Boolean isExist(String url) {
		Boolean isExist = false;
		if (!StringUtils.isEmpty(url)) {
			String filePath = url.replace("/files/", "");
			filePath = bootdoConfig.getUploadPath() + filePath;
			File file = new File(filePath);
			if (file.exists()) {
				isExist = true;
			}
		}
		return isExist;
	}
}
