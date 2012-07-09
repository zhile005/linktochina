package com.oa.common.pojo;

public interface PaginableVm<T> {

	/**
	 * 默认每页的记录数量
	 */
	public static final int PAGESIZE_DEFAULT = 20;

	/**
	 * 表示是不是第一页
	 * @return true 是; false 不是
	 */
	public abstract boolean isFirstPage();

	public abstract boolean isMiddlePage();

	public abstract boolean isLastPage();

	public abstract boolean isNextPageAvailable();

	public abstract boolean isPreviousPageAvailable();

	/**
	 * 下一页号
	 * @return 取得下一页号
	 */
	public abstract int getNextPage();

	public abstract int getPreviousPage();

	/**
	 * Method getPageSize returns the pageSize of this PaginatedArrayList object.
	 *
	 *  每页大小
	 *
	 * @return the pageSize (type int) of this PaginatedArrayList object.
	 */

	public abstract int getPageSize();

	/**
	 * Method setPageSize sets the pageSize of this PaginatedArrayList object.
	 *  每页大小
	 * @param pageSize the pageSize of this PaginatedArrayList object.
	 */

	public abstract void setPageSize(int pageSize);

	/**
	 * Method getIndex returns the index of this PaginatedArrayList object.
	 *  当前页。第一页是1
	 * @return the index (type int) of this PaginatedArrayList object.
	 */

	public abstract int getIndex();

	/**
	 * Method setIndex sets the index of this PaginatedArrayList object.
	 *  当前页。第一页是1
	 * @param index the index of this PaginatedArrayList object.
	 *
	 */

	public abstract void setIndex(int index);

	/**
	 * Method getTotalItem returns the totalItem of this PaginatedArrayList object.
	 *  总记录数
	 * @return the totalItem (type int) of this PaginatedArrayList object.
	 */

	public abstract int getTotalItem();

	/**
	 * Method setTotalItem sets the totalItem of this PaginatedArrayList object.
	 *  总记录数
	 * @param totalItem the totalItem of this PaginatedArrayList object.
	 *
	 */

	public abstract void setTotalItem(int totalItem);

	/**
	 * Method getTotalPage returns the totalPage of this PaginatedArrayList object.
	 *  总页数
	 * @return the totalPage (type int) of this PaginatedArrayList object.
	 */

	public abstract int getTotalPage();

	/**
	 * Method getStartRow returns the startRow of this PaginatedArrayList object.
	 *
	 *  分页后的记录开始的地方
	 *
	 * @return the startRow (type int) of this PaginatedArrayList object.
	 */

	public abstract int getStartRow();

	/**
	 * Method getEndRow returns the endRow of this PaginatedArrayList object.
	 *
	 *  分页后的记录结束的地方
	 *
	 * @return the endRow (type int) of this PaginatedArrayList object.
	 */

	public abstract int getEndRow();

}