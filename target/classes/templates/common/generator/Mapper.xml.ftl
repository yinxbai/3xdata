<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${package}.dao.${className}Dao">

    <sql id="${classname}Columns">
    <#list columns as column>
    	a.`${column.columnName}` AS "${column.attrname}"<#if column_has_next>,</#if>
    </#list>
    </sql>
    
    <sql id="leftJion">
    </sql>
    
	<select id="get" resultType="${className}DO">
		SELECT  
		<include refid="${classname}Columns"/>
		FROM ${tableName} a
		<include refid="leftJion"/>
		WHERE a.${pk.columnName} = ${r"#{"}value}
	</select>

	<select id="list" resultType="${className}DO">
		SELECT 
        <include refid="${classname}Columns"/>
        FROM ${tableName} a
        <include refid="leftJion"/>
        <where> 
           <#list columns as column>
           <#if column.columnName == 'del_flag'>
           <if test="entity.${column.attrname} != null and entity.${column.attrname} != ''"> 
		  		AND a.${column.columnName} = ${r"#{"}entity.${column.attrname}}
		   </if>
		   <if test="entity.${column.attrname} == null or entity.${column.attrname} == ''"> 
		  		AND a.${column.columnName} = '0'
		   </if>
           <#elseif  column.columnName == 'name'>
           <if test="entity.${column.attrname} != null and entity.${column.attrname} != ''"> 
		  		AND a.${column.columnName} = ${r"#{"}entity.${column.attrname}}
		   </if>
		   <if test="entity.${column.attrname}AllLike != null and entity.${column.attrname}AllLike != ''"> 
		  		AND a.${column.columnName} LIKE CONCAT('%',${r"#{"}entity.${column.attrname}AllLike},'%') 
		   </if>
           <#else>
           <if test="entity.${column.attrname} != null and entity.${column.attrname} != ''"> 
		  		AND a.${column.columnName} = ${r"#{"}entity.${column.attrname}}
		   </if>		
           </#if>
          </#list>	 
		</where>
        ORDER BY a.update_date DESC,a.${pk.columnName} DESC
	</select>
	
	<insert id="insert">
		INSERT INTO ${tableName}
		<trim prefix="(" suffix=")" suffixOverrides="," >
			<#list columns as column>
				<#if column.columnName != pk.columnName || pk.extra != 'auto_increment'>
					<if test="${column.attrname} != null">`${column.columnName}`,</if>
				</#if>
			</#list>
		</trim>
		<trim prefix="values (" suffix=")" suffixOverrides="," >
			<#list columns as column>
				<#if column.columnName != pk.columnName || pk.extra != 'auto_increment'>
			<if test="${column.attrname} != null">${r"#{"}${column.attrname}},</if>
				</#if>
			</#list>
		</trim>
	</insert>
	 
	<update id="update">
		UPDATE ${tableName} 
		<set>
		<#list columns as column>
			<#if column.columnName != pk.columnName>
			<if test="${column.attrname} != null">`${column.columnName}` = ${r"#{"}${column.attrname}}</if>
			</#if>
		</#list>
		</set>
		WHERE ${pk.columnName} = ${r"#{"}${pk.attrname}} 
	</update>
	
	<update id="remove">
		UPDATE ${tableName} 
		SET `del_flag` = '1'
		WHERE ${pk.columnName} = ${r"#{"}value}
	</update>
	
	<update id="batchRemove">
		UPDATE ${tableName} 
		SET `del_flag` = '1'
		WHERE ${pk.columnName} IN 
		<foreach item="${pk.attrname}" collection="array" open="(" separator="," close=")">
			${r"#{"}${pk.attrname}}
		</foreach>
	</update>
	
	<delete id="delete">
		DELETE FROM ${tableName} 
		WHERE ${pk.columnName} = ${r"#{"}value}
	</delete>
	
	<delete id="batchDelete">
		DELETE FROM ${tableName} 
		WHERE ${pk.columnName} IN 
		<foreach item="${pk.attrname}" collection="array" open="(" separator="," close=")">
			${r"#{"}${pk.attrname}}
		</foreach>
	</delete>
	
	<insert id="batchInsert">
		INSERT INTO ${tableName}
		(
		<#list columns as column>
			<#if column.columnName != pk.columnName || pk.extra != 'auto_increment'>
			`${column.columnName}`<#if column_has_next>,</#if>
			</#if>
		</#list>
		)
		VALUES
		<foreach item="${classname}" collection="list" separator=",">
		(
		<#list columns as column>
			<#if column.columnName != pk.columnName || pk.extra != 'auto_increment'>
			${r"#{"}${classname}.${column.attrname}}<#if column_has_next>,</#if>
			</#if>
		</#list>
		)
		</foreach>
	</insert>
	 
	<update id="batchUpdate">
		<foreach item="${classname}" collection="list" separator=";">
			UPDATE ${tableName} 
			<set>
			<#list columns as column>
				<#if column.columnName != pk.columnName>
				<if test="${classname}.${column.attrname} != null">`${column.columnName}` = ${r"#{"}${classname}.${column.attrname}}<#if column_has_next>,</#if></if>
				</#if>
			</#list>
			</set>
			WHERE ${pk.columnName} = ${r"#{"}${classname}.${pk.attrname}} 
		</foreach>
	</update>
	
	<update id="updateByWhere">
		UPDATE ${tableName} 
		<set>
		<#list columns as column>
			<#if column.columnName != pk.columnName>
			<if test="param.${column.attrname} != null">`${column.columnName}` = ${r"#{"}param.${column.attrname}}<#if column_has_next>,</#if></if>
			</#if>
		</#list>
		</set>
		<where> 
           <#list columns as column>
           <if test="where.${column.attrname} != null and where.${column.attrname} != ''"> 
		  		AND ${column.columnName} = ${r"#{"}where.${column.attrname}}
		   </if>
          </#list>	 
		</where>
	</update>
	
	<update id="removeByWhere">
		UPDATE ${tableName} 
		SET `del_flag` = '1'
		<where> 
           <#list columns as column>
           <if test="where.${column.attrname} != null and where.${column.attrname} != ''"> 
		  		AND ${column.columnName} = ${r"#{"}where.${column.attrname}}
		   </if>
          </#list>	 
		</where>
	</update>
	
	<delete id="deleteByWhere">
		DELETE FROM ${tableName} 
		<where> 
           <#list columns as column>
           <if test="where.${column.attrname} != null and where.${column.attrname} != ''"> 
		  		AND ${column.columnName} = ${r"#{"}where.${column.attrname}}
		   </if>
          </#list>	 
		</where>
	</delete>
</mapper>