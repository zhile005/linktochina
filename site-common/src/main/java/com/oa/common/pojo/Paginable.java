package com.oa.common.pojo;

import java.util.List;

import org.apache.struts2.json.JSONException;

/**
 * 
 */
public interface Paginable<T> {
	/**
	 * 总数据条数
	 * @return
	 */
	public int getTotalCount();

	/**
	 * 总页数
	 * @return
	 */
	public int getTotalPage();

	/**
	 * 当前页面内记录数
	 * @return
	 */
	public int getPageSize();

	/**
	 * 当前页数
	 * @return
	 */
	public int getPageNo();

	/**
	 * 是否是第一页
	 * @return
	 */
	public boolean isFirstPage();

	/**
	 * 是否是最后一页
	 * @return
	 */
	public boolean isLastPage();
	
	/**
	 * 取得当前页数据
	 */
	public List<T> getPageData();
	
	/**
	 * 取得json字符串
	 * @throws JSONException 
	 */
	public String toJsonStr() throws JSONException;
	/**
	 * 取得查询开始行
	 * @return
	 */
	public int getStartRow();
	/**
	 * 取得查询结束行
	 * @return
	 */
	public int getEndRow();
	/**
	 * 设置分页数据
	 * @return
	 */
	public void setPageData(List<T> queryForList);
	/**
	 * 设置总页数
	 * @return
	 */
	public void setTotalCount(int queryForObject);
}

