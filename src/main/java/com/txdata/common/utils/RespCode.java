package com.txdata.common.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 返回响应码
 * 
 * @author Administrator
 *
 */
public class RespCode {

	private String code;

	private String msg;

	private static final Map<String, RespCode> MAP = new LinkedHashMap<String, RespCode>();

	public static RespCode RAMP_SUCCESS = new RespCode("000000", "success");

	public static RespCode RAMP_FAIL = new RespCode("000001", "fail");

	public static RespCode TOKEN_FAIL = new RespCode("000002", "token invalid");

	public static RespCode NO_DATA = new RespCode("000003", "no data");

	public static RespCode SUCCESS = new RespCode("200", "操作成功");

	public static RespCode PARAM_FAIL = new RespCode("400", "参数错误");

	public static RespCode NO_LOGIN = new RespCode("401", "没有登录");

	public static RespCode UN_AUTHORIZED = new RespCode("403", "没有权限");

	public static RespCode BUSI_ERROR = new RespCode("406", "业务异常");

	public static RespCode NET_ERROR = new RespCode("502", "网络异常");

	public static RespCode NET_TIMEOUT = new RespCode("503", "网络连接超时");

	public static RespCode FAIL_NOKNOW = new RespCode("500", "未知异常");

	public static RespCode UN_LOGIN_TOKEN = new RespCode("999", "登录信息已过期");

	public RespCode(String code, String msg) {
		this.code = code;
		this.msg = msg;
		MAP.put(code, this);
	}

	public static RespCode valueOf(String code) {
		return MAP.get(code);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
