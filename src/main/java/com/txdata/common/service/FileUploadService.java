package com.txdata.common.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.txdata.common.config.BootdoConfig;

@Service
public class FileUploadService {
	
	@Autowired
	private BootdoConfig bootdoConfig;

	/**
	 * 获取文件
	 * 
	 * @param FilePath 文件请求URI
	 * @param splitStr 需要截取的字段，如“userfiles”
	 * @param request
	 * @param response
	 */
	public void download(String requestURI, String splitStr, HttpServletRequest request, HttpServletResponse response) {
		// 获取文件上传保存的路径
		String classPath = bootdoConfig.getUploadPath();// env.getProperty("bootdo.uploadPath");
		String split = System.getProperty("file.separator");
		String[] pathStr = requestURI.split(splitStr);
		String path = "/userfiles" + pathStr[1];
		String realPath = path;
		if ("/".equals(split)) {
			split = "/";
		} else if ("\\".equals(split)) {
			split = "\\\\";
		}
		String[] ss = path.split(split);
		String filename = ss[ss.length - 1];
		OutputStream os = null;
		FileInputStream fis = null;
		realPath = classPath + realPath;
		try {
			File file = new File(realPath);
			os = response.getOutputStream();
			response.setContentType("application/octet-stream; charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment; filename=" + new String(filename.getBytes(), "iso-8859-1"));
			fis = new FileInputStream(file);
			byte[] b = new byte[1024];
			int len = 0;
			while ((len = fis.read(b)) != -1) {
				os.write(b, 0, len);
			}
			os.flush();
			os.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
