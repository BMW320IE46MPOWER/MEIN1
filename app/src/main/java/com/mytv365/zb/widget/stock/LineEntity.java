package com.mytv365.zb.widget.stock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LineEntity implements Serializable {

	/** 线表示数据 */
	private List<Float> lineData;

	/** 分时数据 */
	private List<OHLCEntity> timeLineData;

	/** 线�?�? */
	private String title;

	/** 线表示颜色 */
	private int lineColor;

	/** 是否显示线 */
	private boolean display = true;

	public List<Float> getLineData() {
		return lineData;
	}

	public List<OHLCEntity> getTimeLineData() {
		return timeLineData;
	}

	public void setTimeLineData(List<OHLCEntity> timeLineData) {
		this.timeLineData = timeLineData;
	}

	public void setLineData(List<Float> lineData) {
		this.lineData = lineData;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getLineColor() {
		return lineColor;
	}

	public void setLineColor(int lineColor) {
		this.lineColor = lineColor;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public LineEntity() {
		super();
	}

	public LineEntity(List<Float> lineData, String title, int lineColor) {
		super();
		this.lineData = lineData;
		this.title = title;
		this.lineColor = lineColor;
	}

	public void put(float value) {
		if (null == lineData) {
			lineData = new ArrayList<Float>();
		}
		lineData.add(value);
	}
}
