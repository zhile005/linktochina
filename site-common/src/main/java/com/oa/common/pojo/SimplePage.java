package com.oa.common.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.apache.struts2.json.annotations.JSON;

public class SimplePage<T> implements Paginable<T> {
	
	private static final long serialVersionUID = 1L;
	public static final int DEF_COUNT = 20;
	protected int totalCount = 0;
	protected int pageSize = DEF_COUNT;
	protected int pageNo = 1;
	protected int totalPageNum = 1;
	protected List<T> listData = new ArrayList<T>();
	/**
     * 分页后的记录开始的地方
     * 第一条记录是1
     */
	protected int startRow;
    /**
     * 分页后的记录结束的地方
     */
	protected int endRow;
	
	public SimplePage(int pageNo, int pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		adjustPage();
	}
	/**
	 */
	public void adjustPage() {
		if (totalCount <= 0) {
			totalCount = 0;
		}
		if (pageSize <= 0) {
			pageSize = DEF_COUNT;
		}
		if ((pageNo - 1) * pageSize >= totalCount) {
			pageNo = totalCount / pageSize;
		}
		if (pageNo <= 0) {
			pageNo = 1;
		}
		totalPageNum = (totalCount+pageSize-1)/pageSize;
		endRow = pageNo * pageSize;
		startRow = endRow - pageSize + 1;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		adjustPage();
	}
	public int getStartRow() {
		return startRow;
	}
	public int getEndRow() {
		return endRow;
	}
	@JSON(name="page")
	public int getPageNo() {
		return pageNo;
	}
	@JSON(serialize=false)
	public int getPageSize() {
		return pageSize;
	}
	
	@JSON(name="records")
	public int getTotalCount() {
		return totalCount;
	}
	@JSON(name="total")
	public int getTotalPage() {
		return totalPageNum;
	}
	public boolean isFirstPage() {
		return pageNo <= 1;
	}
	public boolean isLastPage() {
		return pageNo >= getTotalPage();
	}
	public int getNextPage() {
		if (isLastPage()) {
			return pageNo;
		} else {
			return pageNo + 1;
		}
	}
	public int getPrePage() {
		if (isFirstPage()) {
			return pageNo;
		} else {
			return pageNo - 1;
		}
	}
	@JSON(name="rows")
	public List<T> getPageData() {
		return listData;
	}
	public void setPageData(List<T> pageDate) {
		this.listData = pageDate;
	}
	
	public String toJsonStr() throws JSONException {
		StringBuffer temp = new StringBuffer();
		temp.append("{totalProperty:").append(getTotalCount()).append(", root:")
				.append(JSONUtil.serialize(getPageData()).toString()).append("}");
		return temp.toString().replaceAll("\"", "\'").replaceAll("'true'", "true")
		.replaceAll("'false'", "false");
	}
}
