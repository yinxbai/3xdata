package com.txdata.common.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.txdata.common.domain.FileDO;
import com.txdata.common.service.FileService;
import com.txdata.common.utils.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.txdata.common.config.BootdoConfig;
import com.txdata.system.domain.UserDO;

import cn.hutool.core.io.FileUtil;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

/**
 * 文件上传、下载
 * 
 * @author lmh
 *
 */
@Controller
public class FileUploadController extends BaseController {

	@Autowired
	private BootdoConfig bootdoConfig;
	@Autowired
	private FileService fileService;
	
	
	/**
	 * 获取视频时长
	 * @Description: 该方法的功能描述
	 * @param: 参数描述
	 * @return: 返回结果描述
	 * @throws: 异常描述
	 * @author: 谢文辉
	 * @date: 2019年9月29日 下午4:28:56
	 */
	private Time ReadVideoTime(File source) {
		Encoder encoder = new Encoder();
		String length = "";
		Time time=null;
		try {
			MultimediaInfo m = encoder.getInfo(source);
			long ls = m.getDuration() / 1000;
			int hour = (int) (ls / 3600);
			int minute = (int) (ls % 3600) / 60;
			int second = (int) (ls - hour * 3600 - minute * 60);	
			time=new Time(hour,minute,second);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}
	
	/**
	 * 文件上传
	 * 
	 * @param file
	 * @param isRename
	 *            是否重命名
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/fore/sys/foreFileUpload", method = RequestMethod.POST)
	@ResponseBody
	public R<?> foreFileUpload(MultipartFile file, String isRename, HttpServletRequest request,
			HttpServletResponse response) {
		String msg = "";
		String relativePath = "";
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if (file != null) {// 判断上传的文件是否为空
			String fileName = file.getOriginalFilename();// 文件原名称

			System.out.println("上传的文件原名称:" + fileName);
			// 项目在容器中实际发布运行的根路径
			UserDO user = ShiroUtils.getUser();
			String principal = user.getId();
			String year = DateUtils.getYear(); // 年份
			String month = DateUtils.getMonth(); // 月份
			String separator = System.getProperty("file.separator");
			// 相对路径
			relativePath = separator + "files" + separator + principal + separator + "files" + separator + year
					+ separator + month;
			// 绝对路径
			String realPath = bootdoConfig.getUploadPath() + separator + relativePath;
			// 创建目录
			FileUtils.createDirectory(FileUtils.path(realPath));
			String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
			String base64Name = IdGen.uuid();
			// 自定义的文件名称
			String trueFileName = base64Name + "." + suffix;
			// 设置存放文件的路径
			String path = null;
			File newFile = null;
			// 转存文件到指定的路径
			try {
				if (StringUtils.isBlank(isRename) || "0".equals(isRename)) {	// 不重命名
					String prefix = fileName.substring(0, fileName.lastIndexOf("."));
					int i = checkFileIsExists(realPath, separator, prefix, suffix, 0);
					if (i > 0) {
						fileName = prefix + "(" + i + ")." + suffix;
					}
					path = realPath + separator + fileName;
					newFile = new File(path);
					file.transferTo(newFile);
				} else {		// 重命名
					String prefix = trueFileName.substring(0, trueFileName.lastIndexOf("."));
					int i = checkFileIsExists(realPath, separator, prefix, suffix, 0);
					if (i > 0) {
						trueFileName = prefix + "(" + i + ")." + suffix;
					}
					path = realPath + separator + trueFileName;
					newFile = new File(path);
					file.transferTo(newFile);
				}

				int type = FileType.fileType(fileName);
				/**根据视频文件获取时长**/
				if (type == 2) {
					Time videoTime =ReadVideoTime(newFile);
					jsonMap.put("videoTime", videoTime);
				}

				logger.info("=============>>存放文件的路径:" + path);
			} catch (IllegalStateException e) {
				e.printStackTrace();
				msg = "文件上传失败" + e.getMessage();
				return R.error("500", msg);
			} catch (IOException e) {
				e.printStackTrace();
				msg = "文件上传失败" + e.getMessage();
				return R.error("500", msg);
			}
			if (StringUtils.isBlank(isRename) || "0".equals(isRename)) {	// 不重命名
				relativePath = relativePath + separator + fileName;
			} else {
				relativePath = relativePath + separator + trueFileName;
			}
		} else {
			msg = "没有找到文件！";
			return R.error("400", msg);
		}
		relativePath = relativePath.replace("\\", "/");
		jsonMap.put("path", relativePath);
		return R.success(jsonMap);
	}

	/**
	 * 文件上传
	 * @param file 文件流
	 * @param isRename 是否重命名
	 * @param fileType 文件类型 （file_type字典）
	 * @return
	 */
	@RequestMapping(value = "/sys/fileUpload", method = RequestMethod.POST)
	@ResponseBody
	public R<?> fileUpload(MultipartFile file, String isRename, String fileType) {
		String msg = "";
		String relativePath = "";
		JSONObject jsonMap = new JSONObject();
		if (file != null) {// 判断上传的文件是否为空
			String fileName = file.getOriginalFilename();// 文件原名称
			System.out.println("上传的文件原名称:" + fileName);
			// 项目在容器中实际发布运行的根路径
			UserDO user = ShiroUtils.getUser();
			String principal = user.getId();
			String year = DateUtils.getYear(); // 年份
			String month = DateUtils.getMonth(); // 月份
			String separator = System.getProperty("file.separator");
			// 相对路径
			relativePath = separator + "userfiles" + separator + fileType + separator + principal + separator + "files" + separator + year
					+ separator + month + separator + System.currentTimeMillis();			// 绝对路径s
			String realPath = bootdoConfig.getUploadPath() + separator + relativePath;
			// 创建目录
			FileUtils.createDirectory(FileUtils.path(realPath));
			String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
			String base64Name = IdGen.uuid();
			// 自定义的文件名称
			String trueFileName = base64Name + "." + suffix;
			// 设置存放文件的路径
			String path = null;
			JSONObject param = new JSONObject();
			param.put("realPath", realPath);
			param.put("separator", separator);
			param.put("suffix", suffix);
			// 转存文件到指定的路径
			try {
				File newFile = null;
				if (StringUtils.isBlank(isRename) || "0".equals(isRename)) {	// 不重命名
					param.put("fileName", fileName);
				} else { // 重命名
					param.put("fileName", trueFileName);
				}
				newFile = isReName(param, file, newFile, jsonMap);
				if ("mp4".equals(suffix) && null != newFile){
					String tempPath = realPath + separator + IdGen.uuid()+ "." + suffix;
					File tempFile = new File(tempPath);
					Files.copy(newFile.toPath(), tempFile.toPath());
					try {
						CvonvertVideo cvonvertVideo = new CvonvertVideo(tempFile, newFile);
						Thread thread = new Thread(cvonvertVideo);
						thread.start();
					} catch (Exception e) {
						e.printStackTrace();
						msg = "文件上传失败" + e.getMessage();
						return R.error("500", msg);
					}
				}
				logger.info("=============>>存放文件的路径:" + path);
			} catch (IllegalStateException e) {
				e.printStackTrace();
				msg = "文件上传失败" + e.getMessage();
				return R.error("500", msg);
			} catch (IOException e) {
				e.printStackTrace();
				msg = "文件上传失败" + e.getMessage();
				return R.error("500", msg);
			}
			relativePath = relativePath + separator + param.getString("fileName");
			FileDO fileDo = new FileDO();
			fileDo.setType(fileType);
			fileDo.setType(relativePath);
			if (fileService.save(fileDo) > 0){
				jsonMap.put("fileId", fileDo.getId()); //上传的文件id
				return R.success(jsonMap);
			}else {
				msg = "文件保存时出现问题！";
				return R.error("500", msg);
			}
		} else {
			msg = "没有找到文件！";
			return R.error("400", msg);
		}
//		relativePath = relativePath.replace("\\", "/");
//		jsonMap.put("path", relativePath);

	}


	public int checkFileIsExists(String realPath, String separator, String prefix, String suffix, int i) {
		String fileName = "";
		if (i > 0) {
			fileName = prefix + "(" + i + ")." + suffix;
		} else {
			fileName = prefix + "." + suffix;
		}
		String path = realPath + separator + fileName;
		if (FileUtil.exist(path)) {
			i++;
			return checkFileIsExists(realPath, separator, prefix, suffix, i);
		} else {
			return i;
		}
	}

	@RequestMapping(value = "/sys/fileUploads", method = RequestMethod.POST)
	public R<?> fileUploads(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {
		String msg = "";
		String relativePath = "";
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		if (!file.isEmpty()) {
			String fileName = file.getOriginalFilename();
			// 项目在容器中实际发布运行的根路径
			UserDO user = ShiroUtils.getUser();
			String principal = user.getId();
			String year = DateUtils.getYear(); // 年份
			String month = DateUtils.getMonth(); // 月份
			String separator = System.getProperty("file.separator");
			// 相对路径
			relativePath = separator + "files" + separator + principal + separator + "files" + separator + year
					+ separator + month;
			// 绝对路径
			String realPath = bootdoConfig.getUploadPath() + separator + relativePath;
			// 创建目录
			FileUtils.createDirectory(FileUtils.path(realPath));
			String path = realPath + separator + fileName;
			File savedFile = new File(path);
			boolean isCreateSuccess = true;
			try {
				isCreateSuccess = savedFile.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if (isCreateSuccess) {
				try {
					file.transferTo(savedFile);
				} catch (IllegalStateException e) {
					e.printStackTrace();
					msg = "文件上传失败" + e.getMessage();
					return R.error("500", msg);
				} catch (IOException e) {
					e.printStackTrace();
					msg = "文件上传失败" + e.getMessage();
					return R.error("500", msg);
				}
			}
			relativePath = relativePath + separator + fileName;
			relativePath = relativePath.replace("\\", "/");
			jsonMap.put("path", relativePath);
			return R.success(jsonMap);
		} else {
			return R.error("406", "业务异常");
		}
	}

	/**
	 * 文件下载
	 * 
	 * @param path
	 * @param response
	 */
	@RequestMapping(value = "/sys/fileUpload/downloadFile")
	public void download(String path, HttpServletResponse response) {
		logger.info("==============>>文件路径：" + path);
		String realPath = path;
		// 文件上传所在路径
		String classPath = bootdoConfig.getUploadPath();
		String split = System.getProperty("file.separator");
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

	// @RequiresPermissions("user")
	@RequestMapping(value = "/sys/fileUpload/upload_image.do", method = RequestMethod.POST)
	@ResponseBody
	public String uploadImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		R result = null;
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
			if (CollectionUtils.isEmpty(fileMap)) {
				throw new IllegalStateException("No upload file found!");
			}
			MultipartFile partFile = fileMap.entrySet().iterator().next().getValue();
			String relativePath = "";
			String msg = "";
			String fileName = partFile.getOriginalFilename();// 文件原名称
			String ext = FilenameUtils.getExtension(fileName).toLowerCase();
			String separator = System.getProperty("file.separator");
			if (partFile != null) {// 判断上传的文件是否为空
				System.out.println("上传的文件原名称:" + fileName);
				// 项目在容器中实际发布运行的根路径
				String year = DateUtils.getYear(); // 年份
				String month = DateUtils.getMonth(); // 月份
				// 相对路径
				relativePath = separator + "files" + separator + year + separator + month;
				// 绝对路径
				String realPath = bootdoConfig.getUploadPath() + relativePath;
				// 创建目录
				FileUtils.createDirectory(FileUtils.path(realPath));
				// 自定义的文件名称
				String trueFileName = String.valueOf(System.currentTimeMillis()) + fileName;
				// 设置存放文件的路径
				String path = realPath + separator + trueFileName;
				System.out.println("存放文件的路径:" + path);
				// 转存文件到指定的路径
				try {
					partFile.transferTo(new File(path));
				} catch (IllegalStateException e) {
					e.printStackTrace();
					msg = "文件上传失败" + e.getMessage();
					jsonMap.put("msg", msg);
					return renderString(response, jsonMap);
				} catch (IOException e) {
					e.printStackTrace();
					msg = "文件上传失败" + e.getMessage();
					jsonMap.put("msg", msg);
					return renderString(response, jsonMap);
				}

				relativePath = relativePath + separator + trueFileName;
			} else {
				msg = "没有找到文件！";
				jsonMap.put("msg", msg);
				return renderString(response, jsonMap);
			}
			String title = request.getParameter("pictitle");
			String imagePath = relativePath.replaceAll("\\\\", "/");
			String url = "/quick" + imagePath;
			jsonMap.put("title", title);
			jsonMap.put("state", "SUCCESS");
			jsonMap.put("original", fileName);
			jsonMap.put("url", url);
			jsonMap.put("fileType", "." + ext);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return renderString(response, jsonMap);
	}

	/**
	 *
	 * @param file 原始文件流
	 * @param newFile 保存文件
	 * @param jsonMap 返回值
	 * @throws IOException
	 */
	public File isReName(JSONObject param, MultipartFile file, File newFile, Map<String, Object> jsonMap) throws IOException {
		String prefix = param.getString("fileName").substring(0, param.getString("fileName").lastIndexOf("."));
		int i = checkFileIsExists(param.getString("realPath"), param.getString("separator"), prefix, param.getString("suffix"), 0);
		if (i > 0) {
			String fileName = prefix + "(" + i + ")." + param.getString("suffix");
			param.put("fileName", fileName);
		}
		String path = param.getString("realPath") + param.getString("separator") + param.getString("fileName");
		newFile = new File(path);
		file.transferTo(newFile);
		int type = FileType.fileType(param.getString("fileName"));
		/**根据视频文件获取时长**/
		if(type==2){
			Time videoTime =ReadVideoTime(newFile);
			jsonMap.put("videoTime", videoTime);
		}
		return newFile;
	}
}
