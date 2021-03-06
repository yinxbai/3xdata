package ${package}.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${package}.domain.${className}DO;
import com.txdata.common.persistence.proxy.CrudDao;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * ${comments}
 * @author ${author}
 * @email ${email}
 * @date ${datetime}
 */
@Mapper
public interface ${className}Dao extends CrudDao<${className}DO> {

	${className}DO get(${pk.attrType} ${pk.attrname});
	
	Page<${className}DO> list(Page<${className}DO> page, @Param("entity")Map<String,Object> map);
	
	List<${className}DO> list(@Param("entity")Map<String,Object> map);
	
	int insert(${className}DO ${classname});
	
	int update(${className}DO ${classname});
	
	int remove(${pk.attrType} ${pk.columnName});
	
	int batchRemove(${pk.attrType}[] ${pk.attrname}s);
	
	int delete(${pk.attrType} ${pk.columnName});
	
	int batchDelete(${pk.attrType}[] ${pk.attrname}s);
	
	int batchInsert(List<${className}DO> ${classname}s);
	
	int batchUpdate(List<${className}DO> ${classname}s);
	
	int updateByWhere(@Param("param")${className}DO ${classname}, @Param("where")Map<String,Object> whereMap);
	
	int removeByWhere(@Param("where")Map<String,Object> whereMap);
	
	int deleteByWhere(@Param("where")Map<String,Object> whereMap);
}
