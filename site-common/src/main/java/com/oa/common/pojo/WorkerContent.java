package com.oa.common.pojo;
import java.lang.Integer;
import java.lang.String;
import java.util.Date;
public class WorkerContent {
 	/**
	 * 对应数据库字段：WORKER_ID
	 * 注释：workId
	 */
	private Integer workerId;
 	/**
	 * 对应数据库字段：WORKER_CONTENT
	 * 注释：worker内容
	 */
	private String workerContent;
 	/**
	 * 对应数据库字段：CREATE_TIME
	 * 注释：建立时间
	 */
	private Date createTime;
 	/**
	 * 对应数据库字段：UPDATE_TIME
	 * 注释：更新时间
	 */
	private Date updateTime;
 	/**
	 * 	workId
	 * @return
	 */
	public Integer getWorkerId() {
		return workerId;
	}
	/**
	 * workId
	 */
	public void setWorkerId(Integer workerId) {
		this.workerId = workerId;
	}
 	/**
	 * 	worker内容
	 * @return
	 */
	public String getWorkerContent() {
		return workerContent;
	}
	/**
	 * worker内容
	 */
	public void setWorkerContent(String workerContent) {
		this.workerContent = workerContent;
	}
 	/**
	 * 	建立时间
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
	 * 	更新时间
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
	@Override
	public WorkerContent clone() {
		WorkerContent coloneObject = null;
		try {
           coloneObject = (WorkerContent)super.clone();
           coloneObject.setWorkerId(this.getWorkerId());
           coloneObject.setWorkerContent(this.getWorkerContent());
           coloneObject.setCreateTime(this.getCreateTime());
           coloneObject.setUpdateTime(this.getUpdateTime());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();   
        }
		return coloneObject;
	}
}
