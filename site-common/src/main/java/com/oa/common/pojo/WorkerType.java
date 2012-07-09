package com.oa.common.pojo;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.oa.common.tpyeEnum.WorkerTypeEnum;
import com.oa.common.tpyeEnum.YnEnum;

public class WorkerType {
	/**
	 * 对应数据库字段：WORKER_TYPE_CODE 注释：类型编码
	 */
	private String workerTypeCode;
	/**
	 * 对应数据库字段：WORKER_TYPE_NAME 注释：类型名称
	 */
	private String workerTypeName;
	/**
	 * 对应数据库字段：WTYPE 注释：种类
	 */
	private Integer wtype;
	/**
	 * 对应数据库字段：VERSION 注释：版本
	 */
	private Integer version;
	/**
	 * 对应数据库字段：FAIL_MAX_NUM 注释：执行失败容忍次数
	 */
	private Integer failMaxNum;
	/**
	 * 对应数据库字段：PASS_TIME_NUM 注释：超时时限（天）
	 */
	private Double passTimeNum;
	/**
	 * 对应数据库字段：PARENT_TYPE_CODE 注释：所属类型编码
	 */
	private String parentTypeCode;
	/**
	 * 对应数据库字段：YN 注释：是否有效
	 */
	private Integer yn;
	/**
	 * 对应数据库字段：CRONEXPRESSION 注释：执行克隆表达式
	 */
	private String cronexpression;
	/**
	 * 对应数据库字段：CREATE_TIME 注释：建立时间
	 */
	private Date createTime;
	/**
	 * 对应数据库字段：UPDATE_TIME 注释：更新时间
	 */
	private Date updateTime;
	/**
	 * 对应数据库字段：WATCH_YN 注释：是否开启监控
	 */
	private int watchYn;
	/**
	 * 对应数据库字段：ACTIVE_NUM 注释：心跳次数
	 */
	private Integer activeNum;
	/**
	 * 对应数据库字段：WAIT_ORDERS 注释：挤压单量
	 */
	private Integer waitOrders;

	/**
	 * 监控是否有效文本
	 * 
	 * @return
	 */
	public String getWatchYnText() {
		return YnEnum.toText(watchYn);
	}

	/**
	 * 是否有效文本
	 * 
	 * @return
	 */
	public String getYnText() {
		return YnEnum.toText(yn);
	}

	/**
	 * 种类文本
	 * 
	 * @return
	 */
	public String getWtypeText() {
		return WorkerTypeEnum.toText(wtype);
	}

	/**
	 * 类型编码
	 * 
	 * @return
	 */
	public String getWorkerTypeCode() {
		return workerTypeCode;
	}

	/**
	 * 类型编码
	 */
	public void setWorkerTypeCode(String workerTypeCode) {
		this.workerTypeCode = workerTypeCode;
	}

	/**
	 * 类型名称
	 * 
	 * @return
	 */
	public String getWorkerTypeName() {
		return workerTypeName;
	}

	/**
	 * 类型名称
	 */
	public void setWorkerTypeName(String workerTypeName) {
		this.workerTypeName = workerTypeName;
	}

	/**
	 * 种类
	 * 
	 * @return
	 */
	public Integer getWtype() {
		return wtype;
	}

	/**
	 * 种类
	 */
	public void setWtype(Integer wtype) {
		this.wtype = wtype;
	}

	/**
	 * 版本
	 * 
	 * @return
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * 版本
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * 执行失败容忍次数
	 * 
	 * @return
	 */
	public Integer getFailMaxNum() {
		return failMaxNum;
	}

	/**
	 * 执行失败容忍次数
	 */
	public void setFailMaxNum(Integer failMaxNum) {
		this.failMaxNum = failMaxNum;
	}

	/**
	 * 超时时限（天）
	 * 
	 * @return
	 */
	public Double getPassTimeNum() {
		return passTimeNum;
	}

	/**
	 * 超时时限（天）
	 */
	public void setPassTimeNum(Double passTimeNum) {
		this.passTimeNum = passTimeNum;
	}

	/**
	 * 所属类型编码
	 * 
	 * @return
	 */
	public String getParentTypeCode() {
		return parentTypeCode;
	}

	/**
	 * 所属类型编码
	 */
	public void setParentTypeCode(String parentTypeCode) {
		this.parentTypeCode = parentTypeCode;
	}

	/**
	 * 是否有效
	 * 
	 * @return
	 */
	public Integer getYn() {
		return yn;
	}

	/**
	 * 是否有效
	 */
	public void setYn(Integer yn) {
		this.yn = yn;
	}

	/**
	 * 执行克隆表达式
	 * 
	 * @return
	 */
	public String getCronexpression() {
		return cronexpression;
	}

	/**
	 * 执行克隆表达式
	 */
	public void setCronexpression(String cronexpression) {
		this.cronexpression = cronexpression;
	}

	/**
	 * 建立时间
	 * 
	 * @return
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 建立时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 更新时间
	 * 
	 * @return
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * 更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getWatchYn() {
		return watchYn;
	}

	public void setWatchYn(int watchYn) {
		this.watchYn = watchYn;
	}

	public Integer getActiveNum() {
		return activeNum;
	}

	public void setActiveNum(Integer activeNum) {
		this.activeNum = activeNum;
	}

	public Integer getWaitOrders() {
		return waitOrders;
	}

	public void setWaitOrders(Integer waitOrders) {
		this.waitOrders = waitOrders;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public WorkerType clone() {
		WorkerType coloneObject = null;
		try {
			coloneObject = (WorkerType) super.clone();
			coloneObject.setWorkerTypeCode(this.getWorkerTypeCode());
			coloneObject.setWorkerTypeName(this.getWorkerTypeName());
			coloneObject.setWtype(this.getWtype());
			coloneObject.setVersion(this.getVersion());
			coloneObject.setFailMaxNum(this.getFailMaxNum());
			coloneObject.setPassTimeNum(this.getPassTimeNum());
			coloneObject.setParentTypeCode(this.getParentTypeCode());
			coloneObject.setYn(this.getYn());
			coloneObject.setCronexpression(this.getCronexpression());
			coloneObject.setCreateTime(this.getCreateTime());
			coloneObject.setUpdateTime(this.getUpdateTime());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return coloneObject;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WorkerType && this.getWorkerTypeCode() != null && this.getVersion() != null) {
			WorkerType ref = (WorkerType) obj;
			return this.getWorkerTypeCode().equals(ref.getWorkerTypeCode()) && this.getVersion().equals(ref.getVersion());
		} else {
			return false;
		}
	}

}
