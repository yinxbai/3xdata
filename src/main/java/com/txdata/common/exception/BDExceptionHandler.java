package com.txdata.common.exception;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;
import com.txdata.common.config.Constant;
import com.txdata.common.domain.LogDO;
import com.txdata.common.service.LogService;
import com.txdata.common.utils.HttpServletUtils;
import com.txdata.common.utils.R;
import com.txdata.common.utils.ShiroUtils;
import com.txdata.system.domain.UserDO;

/**
 * 异常处理器
 */
@RestControllerAdvice
public class BDExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	LogService logService;

	@Value("${spring.servlet.multipart.max-file-size}")
	private String maxSize;
//
//    /**
//     * 自定义异常
//     */
//    @ExceptionHandler(BDException.class)
//    public R handleBDException(BDException e) {
//        logger.error(e.getMessage(), e);
//        R r = new R();
//        r.put("code", e.getCode());
//        r.put("msg", e.getMessage());
//        return r;
//    }
//
//    @ExceptionHandler(DuplicateKeyException.class)
//    public R handleDuplicateKeyException(DuplicateKeyException e) {
//        logger.error(e.getMessage(), e);
//        return R.error("数据库中已存在该记录");
//    }
//
//    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
//    public R noHandlerFoundException(org.springframework.web.servlet.NoHandlerFoundException e) {
//        logger.error(e.getMessage(), e);
//        return R.error(404, "没找找到页面");
//    }

	@ExceptionHandler(AuthorizationException.class)
	public Object handleAuthorizationException(AuthorizationException e, HttpServletRequest request) {
		logger.error(e.getMessage(), e);
//		if (HttpServletUtils.jsAjax(request)) {
			return R.error("403", "未授权");
//		}
//		return new ModelAndView("error/403");
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public Object handleUploadMaxSizeException(MaxUploadSizeExceededException e) {
		logger.error(e.getMessage(), e);
		String msg = "";
		if(e.getCause().getCause() instanceof FileUploadBase.FileSizeLimitExceededException) {
			msg = "上传失败，上传单个文件大小不得超过" + maxSize;
		}
		if(e.getCause().getCause() instanceof FileUploadBase.SizeLimitExceededException) {
			msg = "上传失败，上传总文件大小不得超过" + maxSize;
		}
		return R.error("302", msg);
	}

	@ExceptionHandler({ Exception.class })
	public Object handleException(Exception e, HttpServletRequest request) {
		LogDO logDO = new LogDO();
		logDO.setCreateDate(new Date());
		logDO.setOperation(Constant.LOG_ERROR);
		logDO.setMethod(request.getRequestURL().toString());
		logDO.setParams(e.toString());
		UserDO current = ShiroUtils.getUser();
		if (null != current) {
			logDO.setUserId(current.getId());
			logDO.setUsername(current.getUsername());
		}
		logService.saveLog(logDO);
		logger.error(e.getMessage(), e);
//		if (HttpServletUtils.jsAjax(request)) {
			return R.error("500", e.getMessage());
//		}
//		return new ModelAndView("error/500");
	}
}
