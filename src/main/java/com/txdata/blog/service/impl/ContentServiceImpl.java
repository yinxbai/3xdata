package com.txdata.blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.txdata.blog.dao.ContentDao;
import com.txdata.blog.domain.ContentDO;
import com.txdata.blog.service.ContentService;



@Service
public class ContentServiceImpl implements ContentService {
	@Autowired
	private ContentDao bContentMapper;
	
	@Override
	public ContentDO get(String cid){
		return bContentMapper.get(cid);
	}
	
	@Override
	public List<ContentDO> list(Map<String, Object> map){
		return bContentMapper.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return bContentMapper.count(map);
	}
	
	@Override
	public int save(ContentDO bContent){
		bContent.preInsert();
		return bContentMapper.save(bContent);
	}
	
	@Override
	public int update(ContentDO bContent){
		bContent.preUpdate();
		return bContentMapper.update(bContent);
	}
	
	@Override
	public int remove(String cid){
		return bContentMapper.remove(cid);
	}
	
	@Override
	public int batchRemove(String[] cids){
		return bContentMapper.batchRemove(cids);
	}
	
}
