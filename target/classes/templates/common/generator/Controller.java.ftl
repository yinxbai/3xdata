package ${package}.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txdata.common.controller.BaseController;
import ${package}.domain.${className}DO;
import ${package}.service.${className}Service;
import com.txdata.common.utils.Query;
import com.txdata.common.utils.R;
import com.alibaba.fastjson.JSONObject;

/**
 * ${comments}
 * 
 * @author ${author}
 * @email ${email}
 * @date ${datetime}
 */
@RestController
@RequestMapping("/${pathName}/${classname}")
public class ${className}Controller extends BaseController {
	@Autowired
	private ${className}Service ${classname}Service;
	
	@PostMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
        Page<${className}DO> page = new Page<${className}DO>(query.getPageNo(), query.getPageSize());
		page = ${classname}Service.page(page, query);
		// 封装分页数据
		JSONObject jsonMap = new JSONObject();
        jsonMap.put("rows", page.getRecords());
        jsonMap.put("pageSize", page.getSize());
        jsonMap.put("pageNo", page.getCurrent());
        jsonMap.put("count", page.getTotal());
        return R.success(jsonMap);
	}
	
    @PostMapping("/form")
    public R form(@RequestParam(required = true)String id){
        ${className}DO ${classname} = ${classname}Service.get(id);
        JSONObject jsonMap = new JSONObject();
        jsonMap.put("formObject", ${classname});
        return R.success(jsonMap);
    }
	
	@PostMapping("/save")
	public R save(${className}DO ${classname}){
		if (${classname}Service.save(${classname}) > 0){
			return R.success();
		}
		return R.error();
	}
	
	@PostMapping("/remove")
	public R remove(${pk.attrType} ${pk.attrname}){
		if (${classname}Service.remove(${pk.attrname}) > 0){ //逻辑删除
		    return R.success();
		}
		return R.error();
	}
	
	@PostMapping("/batchRemove")
	public R batchRemove(@RequestParam("ids[]") ${pk.attrType}[] ${pk.attrname}s){
		${classname}Service.batchRemove(${pk.attrname}s);//批量逻辑删除
		return R.success();
	}
	
	@PostMapping("/delete")
	public R delete(${pk.attrType} ${pk.attrname}){
		if (${classname}Service.delete(${pk.attrname}) > 0){//物理删除
		    return R.success();
		}
		return R.error();
	}
	
	@PostMapping("/batchDelete")
	public R batchDelete(@RequestParam("ids[]") ${pk.attrType}[] ${pk.attrname}s){
		${classname}Service.batchDelete(${pk.attrname}s);//批量物理删除
		return R.success();
	}
	
	@PostMapping("/copy")
	public R copy(${pk.attrType} ${pk.attrname}){
		if (${classname}Service.copy(${pk.attrname}) > 0){
			return R.success();
		}
		return R.error();
	}
}
