package com.txdata.common.utils;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import com.txdata.common.config.BootdoConfig;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class PDFUtil {

	@Autowired
	private static BootdoConfig bootdoConfig = SpringContextHolder.getBean(BootdoConfig.class);
	// 临时文件路径
	private static final String TEMPORARY = bootdoConfig.getUploadPath() + "\\temporary";
	// 合同文件存储路径
	private static String resoucePath = "";

	public static String getResoucePath() {
		if (StringUtils.isBlank(resoucePath)) {
			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			Resource[] resources;
			try {
				resources = resolver.getResources("application-dev.yml");
				if (resources != null && resources.length > 0) {
					Resource resource = resources[0];
					File file = resource.getFile();
					resoucePath = file.getAbsolutePath().replace("application-dev.yml", "");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resoucePath;
	}

	public static void contractHandler(String templateName, Map<String, Object> paramMap, String fileName)
			throws Exception {
		templateName = templateName + ".ftl";
		String separator = System.getProperty("file.separator");
		String year = DateUtils.getYear(); // 年份
		String month = DateUtils.getMonth(); // 月份
		String templatePath = ""; // 模板文件路径
		String temporaryPath = "";// 临时文件保存路径
		templatePath = getResoucePath() + "templates"+System.getProperty("file.separator")+"static"+System.getProperty("file.separator");
		temporaryPath = TEMPORARY + separator + year + separator + month + separator;
		// 判断本地路径是否存在如果不存在则创建
		FileUtils.createDirectory(temporaryPath);
		// 组装html和pdf合同的全路径URL
		String localHtmlUrl = temporaryPath + separator + fileName + ".html";
		String localPdfUrl = temporaryPath + separator + fileName + ".pdf";
		htmHandler(templatePath, templateName, localHtmlUrl, paramMap);// 生成html合同
		pdfHandler(localHtmlUrl, localPdfUrl);// 根据html合同生成pdf合同
		deleteFile(localHtmlUrl);// 删除html格式合同
		System.out.println("PDF生成成功，地址：" + localPdfUrl);
	}

	/**
	 * 生成html格式合同
	 */
	private static void htmHandler(String templatePath, String templateName, String htmUrl,
			Map<String, Object> paramMap) throws Exception {
		Configuration cfg = new Configuration();
		cfg.setDefaultEncoding("UTF-8");
		cfg.setDirectoryForTemplateLoading(new File(templatePath));
		Template template = cfg.getTemplate(templateName);
		File outHtmFile = new File(htmUrl);
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outHtmFile)));
		template.process(paramMap, out);
		out.close();
	}

	/**
	 * 生成pdf格式合同
	 */
	private static void pdfHandler(String htmUrl, String pdfUrl) throws DocumentException, IOException {
		File htmFile = new File(htmUrl);
		File pdfFile = new File(pdfUrl);
		// 删掉之前已经存在的文件
		pdfFile.delete();
		String url = htmFile.toURI().toURL().toString();
		OutputStream os = new FileOutputStream(pdfFile);
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocument(url);
		ITextFontResolver fontResolver = renderer.getFontResolver();
		// 解决中文支持问题
		System.out.println(getResoucePath());
		fontResolver.addFont(getResoucePath() + "templates"+System.getProperty("file.separator")+"static"+System.getProperty("file.separator")+"simhei.ttf", BaseFont.IDENTITY_H,
				BaseFont.NOT_EMBEDDED);
		renderer.layout();
		renderer.createPDF(os);
		os.close();
	}

	/**
	 * 删除文件
	 */
	private static void deleteFile(String fileUrl) {
		File file = new File(fileUrl);
		file.delete();
	}

	/**
	 * pdf文档导出
	 * 
	 * @param request
	 * @param ftlName 文件名
	 * @param root    导出数据
	 * @return
	 * @throws Exception
	 */
	public static ByteArrayOutputStream createPDF(HttpServletRequest request, String ftlName, Map root)
			throws Exception {
//		String basePath = request.getSession().getServletContext().getRealPath("/");// 绝对路径
		Configuration cfg = new Configuration();
		try {
			cfg.setLocale(Locale.CHINA);
			cfg.setEncoding(Locale.CHINA, "UTF-8");
			// 设置编码
			cfg.setDefaultEncoding("UTF-8");
			// 设置模板路径
			cfg.setDirectoryForTemplateLoading(new File(getResoucePath() + "templates/static/"));
			// 获取模板
			Template template = cfg.getTemplate(ftlName, "UTF-8");
//			template.setEncoding();

			Writer writer = new StringWriter();
			// 数据填充模板
			template.process(root, writer);
			String str = writer.toString();
			// pdf生成
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			ITextRenderer iTextRenderer = new ITextRenderer();
			iTextRenderer.setDocument(str);
			// 设置字体 其他字体需要添加字体库
			ITextFontResolver fontResolver = iTextRenderer.getFontResolver();
			fontResolver.addFont(getResoucePath() + "templates/static/" + "simsun.ttc", BaseFont.IDENTITY_H,
					BaseFont.NOT_EMBEDDED);
			// System.out.println(basePath);
			// System.out.println(str);
			iTextRenderer.setDocument(builder.parse(new ByteArrayInputStream(str.getBytes("utf-8"))), null);
			iTextRenderer.layout();
			// 生成PDF
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			iTextRenderer.createPDF(baos);
			baos.close();
			return baos;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	/**
	 * 客户端下载pdf
	 * 
	 * @author 创建时间
	 * @param response
	 * @param bytes
	 * @param filename
	 */
	public static void renderPdf(HttpServletResponse response, final byte[] bytes, final String filename) {
		setFileDownloadHeader(response, filename, ".pdf");
		if (null != bytes) {
			try {
				response.getOutputStream().write(bytes);
				response.getOutputStream().flush();
			} catch (IOException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}

	/**
	 * 设置让浏览器弹出下载对话框的Header
	 * 
	 * @author 创建时间
	 * @param response
	 * @param fileName
	 * @param fileType
	 */
	public static void setFileDownloadHeader(HttpServletResponse response, String fileName, String fileType) {
		try {
			// 中文文件名支持
			String encodedfileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + fileType + "\"");
		} catch (UnsupportedEncodingException e) {
		}
	}

	/**
	 * 删除指定文件夹下所有文件
	 * 
	 * @param path 文件夹完整绝对路径
	 * @return
	 */
	public static boolean delAllFile(String path, String suffix) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile() && temp.getName().contains(suffix)) {
				temp.delete();
			}
			/*
			 * if (temp.isDirectory()) { delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
			 * flag = true; }
			 */
		}
		return flag;
	}
}