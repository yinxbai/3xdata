package com.txdata.activiti.service;

import com.txdata.activiti.config.ActivitiConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.txdata.activiti.dao.SalaryDao;
import com.txdata.activiti.domain.SalaryDO;
import com.txdata.activiti.service.ActTaskService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SalaryService {
	
	@Autowired
	private SalaryDao salaryDao;
	
	@Autowired
	private ActTaskService actTaskService;
	
	public SalaryDO get(String id){
		return salaryDao.get(id);
	}
	
	public List<SalaryDO> list(Map<String, Object> map){
		return salaryDao.list(map);
	}
	
	public int count(Map<String, Object> map){
		return salaryDao.count(map);
	}

	@Transactional(rollbackFor=Exception.class)
	public int save(SalaryDO salary){
			salary.setId(UUID.randomUUID().toString().replace("-",""));
			actTaskService.startProcess(ActivitiConstant.ACTIVITI_SALARY[0],ActivitiConstant.ACTIVITI_SALARY[1],salary.getId(),salary.getContent(),new HashMap<>());
			return salaryDao.save(salary);
	}
	
	@Transactional(rollbackFor=Exception.class)
	public int update(SalaryDO salary){
		Map<String,Object> vars = new HashMap<>(16);
		vars.put("pass",  salary.getTaskPass() );
		vars.put("title","");
		actTaskService.complete(salary.getTaskId(),vars);
		return salaryDao.update(salary);
	}
	
	public int remove(String id){
		return salaryDao.remove(id);
	}
	
	public int batchRemove(String[] ids){
		return salaryDao.batchRemove(ids);
	}
	
}
