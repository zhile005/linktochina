package com.oa.common.pojo;

import java.util.ArrayList;

/**
 * Date: 2010-4-15 Time: 18:34:44
 */
public class MyPaginatedArrayList<T> extends ArrayList<T> implements PaginableVm<T> {
	/**
	 * 每页大小
	 */
	private int pageSize;
	/**
	 * 当前页。第一页是1
	 */
	private int index;

	/**
	 * 总记录数
	 */
	private int totalItem;
	/**
	 * 总页数
	 */
	private int totalPage;

	/**
	 * 分页后的记录开始的地方 第一条记录是1
	 */
	private int startRow;
	/**
	 * 分页后的记录结束的地方
	 */
	private int endRow;

	/**
	 * 带当前页和页大小的构造方法
	 * 
	 * @param index
	 *            当前页
	 * @param pageSize
	 *            页大小
	 */
	public MyPaginatedArrayList(int index, int pageSize) {
		this.index = index;
		this.pageSize = pageSize;
		repaginate();
	}

	public boolean isFirstPage() {
		return index <= 1;
	}

	public boolean isMiddlePage() {
		return !(isFirstPage() || isLastPage());
	}

	public boolean isLastPage() {
		return index >= totalPage;
	}

	public boolean isNextPageAvailable() {
		return !isLastPage();
	}

	public boolean isPreviousPageAvailable() {
		return !isFirstPage();
	}

	public int getNextPage() {
		if (isLastPage()) {
			return totalItem;
		} else {
			return index + 1;
		}
	}

	public int getPreviousPage() {
		if (isFirstPage()) {
			return 1;
		} else {
			return index - 1;
		}
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		repaginate();
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
		repaginate();
	}

	public int getTotalItem() {
		return totalItem;
	}

	public void setTotalItem(int totalItem) {
		this.totalItem = totalItem;
		repaginate();
		if (this.endRow > this.totalItem) {
			this.endRow = this.totalItem;
		}
	}

	public int getStartRow() {
		return startRow;
	}

	public int getEndRow() {
		return endRow;
	}

	private void repaginate() {
		if (pageSize < 1) { // 防止程序偷懒，list和分页的混合使用
			pageSize = PAGESIZE_DEFAULT;
		}
		if (index < 1) {
			index = 1;// 恢复到第一页
		}
		if (totalItem > 0) {
			totalPage = totalItem / pageSize + (totalItem % pageSize > 0 ? 1 : 0);
			if (index > totalPage) {
				index = totalPage; // 最大页
			}
		}
		endRow = index * pageSize;
		startRow = endRow - pageSize + 1;
	}

	public int getTotalPage() {
		return totalPage;
	}

}
