package com.txdata.system.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 图标数据所需实体类
 * 
 * @author huangmk
 *
 */
public class DataList implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<String> legend;
	private List<String> xAxis;
	private List<Series> series;

	public List<String> getLegend() {
		return legend;
	}

	public void setLegend(List<String> legend) {
		this.legend = legend;
	}

	public List<String> getxAxis() {
		return xAxis;
	}

	public void setxAxis(List<String> xAxis) {
		this.xAxis = xAxis;
	}

	public List<Series> getSeries() {
		return series;
	}

	public void setSeries(List<Series> series) {
		this.series = series;
	}
}
