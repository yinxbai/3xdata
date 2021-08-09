package com.txdata.common.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class R<D> implements Serializable {
	private static final long serialVersionUID = 1L;

	private String code;

	private String message;

	private D data;

	public R() {
		this.code = "0";
		this.message = "操作成功";
	}

	public static R error() {
		return error("1", "操作失败");
	}

	public static R error(String msg) {
		return error("500", msg);
	}

	public static R error(String code, String msg) {
		R r = new R();
		r.setCode(code);
		r.setMessage(msg);
		return r;
	}

	public static R error(String code, String msg, Map<String, Object> map) {
		R r = new R();
		r.setCode(code);
		r.setMessage(msg);
		r.setData(map);
		return r;
	}

	public static R ok(String msg) {
		R r = new R();
		r.setMessage(msg);
		return r;
	}

	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.setData(map);
		return r;
	}

	public static R ok() {
		return new R();
	}

	public static R success() {
		R r = new R();
		r.setCode("200");
		r.setMessage("success");
		return r;
	}

	public static R success(Map<String, Object> map) {
		R r = new R();
		r.setCode("200");
		r.setMessage("success");
		r.setData(map);
		return r;
	}

	public static R success(String message) {
		R r = new R();
		r.setCode("200");
		r.setMessage(message);
		return r;
	}

//	@Override
	public R put(String key, Object value) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(key, value);
		R r = R.ok();
		r.setData(map);
		return r;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String msg) {
		this.message = msg;
	}

	public D getData() {
		return data;
	}

	public void setData(D data) {
		this.data = data;
	}
}
