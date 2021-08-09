package ${package}.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${package}.domain.${className}DO;
import ${package}.dao.${className}Dao;
import com.txdata.common.persistence.CrudService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

/**
 * ${comments}
 * 
 * @author ${author}
 * @email ${email}
 * @date ${datetime}
 */
 @Service
public class ${className}Service extends CrudService<${className}Dao, ${className}DO> {

    @Autowired
    private ${className}Dao ${classname}Dao;
    
    /**
	 * 通过id查找数据
	 */
    public ${className}DO get(${pk.attrType} ${pk.attrname}){
        return ${classname}Dao.get(${pk.attrname});
    }
    
    /**
	 * 分页查询列表
	 */
    public Page<${className}DO> page(Page<${className}DO> page, Map<String, Object> map){
        return ${classname}Dao.list(page, map);
    }
    
    /**
	 * 查询列表
	 */
    public List<${className}DO> list(Map<String, Object> map){
        return ${classname}Dao.list(map);
    }
    
    /**
	 * 保存/修改
	 */
    @Transactional(readOnly=false)
    public int save(${className}DO ${classname}){
        return super.save(${classname});
    }
    
    /**
	 * 通过id逻辑删除
	 */
    @Transactional(readOnly=false)
    public int remove(${pk.attrType} ${pk.attrname}){
        return ${classname}Dao.remove(${pk.attrname});
    }
    
    /**
	 * 通过ids批量逻辑删除
	 */
    @Transactional(readOnly=false)
    public int batchRemove(${pk.attrType}[] ${pk.attrname}s){
        return ${classname}Dao.batchRemove(${pk.attrname}s);
    }
    
    /**
	 * 通过id物理删除
	 */
    @Transactional(readOnly=false)
    public int delete(${pk.attrType} ${pk.attrname}){
        return ${classname}Dao.delete(${pk.attrname});
    }
    
    /**
	 * 通过ids物理删除
	 */
    @Transactional(readOnly=false)
    public int batchDelete(${pk.attrType}[] ${pk.attrname}s){
        return ${classname}Dao.batchDelete(${pk.attrname}s);
    }
    
    /**
	 * 批量插入
	 */
    @Transactional(readOnly=false)
    public int batchInsert(List<${className}DO> ${classname}s){
    	return ${classname}Dao.batchInsert(${classname}s);
    }
    
    /**
	 * 批量修改
	 */
	@Transactional(readOnly=false)
	public int batchUpdate(List<${className}DO> ${classname}s){
		return ${classname}Dao.batchUpdate(${classname}s);
	}
    
    /**
	 * 通过id复制一条相同的数据
	 */
    @Transactional(readOnly=false)
    public int copy(${pk.attrType} ${pk.attrname}){
    	int result = 0;
    	${className}DO ${classname} = ${classname}Dao.get(${pk.attrname});
    	if (${classname} != null){
    		${classname}.setId(null);
    		${classname}.preInsert();
    		//表结构中name字段不一定存在，故此自动生成代码时注释下列代码，存在时取消注释即可
//    		if (StrUtil.isNotBlank(${classname}.getName())){ 
//    			${classname}.setName(${classname}.getName() + "-复制");
//    		}
    		result = ${classname}Dao.insert(${classname});
    	}
        return result;
    }
    
    /**
	 * 
	 * @Description: 修改（通过自定义的条件进行修改操作）
	 * @param ${classname} 要被修改的参数
	 * @param whereMap 修改条件
	 * @return: 返回修改数量
	 */
	@Transactional(readOnly=false)
    public int updateByWhere(${className}DO ${classname}, Map<String,Object> whereMap){
    	return ${classname}Dao.updateByWhere(${classname}, whereMap);
    }
    
    /**
	 * 
	 * @Description: 逻辑删除（通过自定义的条件进行逻辑删除操作）
	 * @param whereMap 逻辑删除条件
	 * @return: 返回逻辑删除数量
	 */
	@Transactional(readOnly=false)
    public int removeByWhere(Map<String,Object> whereMap){
    	return ${classname}Dao.removeByWhere(whereMap);
    }
	
	/**
	 * 
	 * @Description: 物理删除（通过自定义的条件进行物理删除操作）慎用
	 * @param whereMap 物理删除条件
	 * @return: 返回物理删除数量
	 */
	@Transactional(readOnly=false) 
	public int deleteByWhere(Map<String,Object> whereMap){
		return ${classname}Dao.deleteByWhere(whereMap);
	}
}
