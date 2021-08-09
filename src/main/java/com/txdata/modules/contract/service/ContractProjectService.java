package com.txdata.modules.contract.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.modules.contract.domain.ContractProjectDO;
import com.txdata.modules.contract.dao.ContractProjectDao;
import com.txdata.common.persistence.CrudService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

/**
 * 
 * 
 * @author 3xdata
 * @email 3xdata@3xdata.cn
 * @date 2021-08-02 16:42:56
 */
 @Service
public class ContractProjectService extends CrudService<ContractProjectDao, ContractProjectDO> {

    @Autowired
    private ContractProjectDao projectDao;
    
    /**
	 * 通过id查找数据
	 */
    public ContractProjectDO get(String id){
        return projectDao.get(id);
    }
    
    /**
	 * 分页查询列表
	 */
    public Page<ContractProjectDO> page(Page<ContractProjectDO> page, Map<String, Object> map){
        return projectDao.list(page, map);
    }
    
    /**
	 * 查询列表
	 */
    public List<ContractProjectDO> list(Map<String, Object> map){
        return projectDao.list(map);
    }
    
    /**
	 * 保存/修改
	 */
    @Transactional(readOnly=false)
    public int save(ContractProjectDO project){
        return projectDao.save(project);
    }
    
    /**
	 * 通过id逻辑删除
	 */
    @Transactional(readOnly=false)
    public int remove(String id){
        return projectDao.remove(id);
    }

    
    /**
	 * 通过id物理删除
	 */
    @Transactional(readOnly=false)
    public int delete(String id){
        return projectDao.delete(id);
    }




}
