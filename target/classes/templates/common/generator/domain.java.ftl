package ${package}.domain;

import com.txdata.common.domain.DataEntity;
<#if hasDate>
import java.util.Date;
</#if>
<#if hasBigDecimal>
import java.math.BigDecimal;
</#if>

/**
 * ${comments}
 * 
 * @author ${author}
 * @email ${email}
 * @date ${datetime}
 */
public class ${className}DO extends DataEntity<${className}DO> {
	private static final long serialVersionUID = 1L;

<#list columns as column>
	<#if column.attrname != 'id' && column.attrname != 'createDate' && column.attrname != 'createBy' && column.attrname != 'updateDate' && column.attrname != 'updateBy' && column.attrname != 'delFlag' && column.attrname != 'remarks'>
	private ${column.attrType} ${column.attrname};  //${column.comments} 
	</#if>
</#list>
<#list columns as column>
	<#if column.attrname != 'id' && column.attrname != 'createDate' && column.attrname != 'createBy' && column.attrname != 'updateDate' && column.attrname != 'updateBy' && column.attrname != 'delFlag' && column.attrname != 'remarks'>
	
	public void set${column.attrName}(${column.attrType} ${column.attrname}) {
		this.${column.attrname} = ${column.attrname};
	}
	
	public ${column.attrType} get${column.attrName}() {
		return ${column.attrname};
	} 
	</#if>
</#list>
}
