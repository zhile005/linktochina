package com.oa.common.pojo;

import java.util.Date;

public class PaginationBase {
	
	private Integer recordCount;
	//开始时间
	private Date startDate;
	//结束时间
	private Date endDate;
	
	public Integer getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(Integer recordCount) {
		this.recordCount = recordCount;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
