package com.txdata.common.utils;

import java.io.Serializable;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author bootdo 1992lcg@163.com
 */
public class PageUtils implements Serializable {
	private static final long serialVersionUID = 1L;
	private int total;
	private int pageSize;
	private int pageNo;
	private List<?> rows;
	private int count;

	public PageUtils(List<?> list, int total) {
		this.rows = list;
		this.total = total;
		this.count = total;
	}

	public PageUtils(List<?> list, int total, int pageSize, int pageNo) {
		this.rows = list;
		this.total = total;
		this.count = total;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * 获取list分页后的PageUtils
	 * 
	 * @param list     需要分页处理的list
	 * @param pageSize 每页条数
	 * @param pageNo   页码
	 */
	public static PageUtils getPage(List<?> list, int pageSize, int pageNo) {
		int total = list.size();
		pageSize = pageSize > 0 ? pageSize : 15;
		pageNo = pageNo > 1 ? pageNo : 1;
		int beginIndex = (pageNo - 1) * pageSize < total ? (pageNo - 1) * pageSize : total;
		int endIndex = (pageNo - 1) * pageSize + pageSize < total ? (pageNo - 1) * pageSize + pageSize : total;
		List<?> newlist = list.subList(beginIndex, endIndex);
		return new PageUtils(newlist, total, pageSize, pageNo);
	}

	/**
	 * 根据request获取页码
	 * 
	 * @param request
	 * @param response
	 * @param defaultPageNo
	 * @return
	 */
	public static int getPageNoFromRequest(HttpServletRequest request, HttpServletResponse response,
			int defaultPageNo) {
		// 设置页码参数（传递repage参数，来记住页码）
		String no = request.getParameter("pageNo");
		if (StringUtils.isNumeric(no)) {
			CookieUtils.setCookie(response, "pageNo", no);
			return Integer.parseInt(no);
		} else if (request.getParameter("repage") != null) {
			no = CookieUtils.getCookie(request, "pageNo");
			if (StringUtils.isNumeric(no)) {
				return Integer.parseInt(no);
			}
		}
		return defaultPageNo;
	}

	/**
	 * 根据request获取分页大小
	 * 
	 * @param request
	 * @param response
	 * @param defaultPageSize
	 * @return
	 */
	public static int getPageSizeFromRequest(HttpServletRequest request, HttpServletResponse response,
			int defaultPageSize) {
		// 设置页面大小参数（传递repage参数，来记住页码大小）
		String size = request.getParameter("pageSize");
		if (StringUtils.isNumeric(size)) {
			CookieUtils.setCookie(response, "pageSize", size);
			return Integer.parseInt(size);
		} else if (request.getParameter("repage") != null) {
			size = CookieUtils.getCookie(request, "pageSize");
			if (StringUtils.isNumeric(size)) {
				return Integer.parseInt(size);
			}
		}
		return defaultPageSize;
	}

}
