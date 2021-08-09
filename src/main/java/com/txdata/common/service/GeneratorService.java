package com.txdata.common.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.txdata.common.dao.GeneratorMapper;
import com.txdata.common.utils.GenUtils;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

/**
 * 代码生成相关Service
 * 
 * @author 1992lcg@163.com
 * @Time 2017年9月6日
 * @description
 * 
 */
@Service
public class GeneratorService {
	
	@Autowired
	GeneratorMapper generatorMapper;

	public List<Map<String, Object>> list() {
		List<Map<String, Object>> list = generatorMapper.list();
		return list;
	}

	public byte[] generatorCode(String[] tableNames) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);
		for (String tableName : tableNames) {
			// 查询表信息
			Map<String, String> table = generatorMapper.get(tableName);
			// 查询列信息
			List<Map<String, String>> columns = generatorMapper.listColumns(tableName);
			// 生成代码
			GenUtils.generatorCode(table, columns, zip);
		}
		IOUtils.closeQuietly(zip);
		return outputStream.toByteArray();
	}
}
