package com.oa.common.pojo;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.oa.common.tpyeEnum.WorkerEnum;
import com.oa.common.tpyeEnum.WorkerStatusEnum;
import com.oa.common.tpyeEnum.WorkerTypeEnum;
import com.oa.common.tpyeEnum.YnEnum;

public class Worker {
 	/**
	 * 对应数据库字段：WORKER_ID
	 * 注释：主键
	 */
	private Integer workerId;
 	/**
	 * 对应数据库字段：WORKER_TYPE_CODE
	 * 注释：worker编码
	 */
	private String workerTypeCode;
 	/**
	 * 对应数据库字段：WTYPE
	 * 注释：work类型
	 */
	private Integer wtype;
 	/**
	 * 对应数据库字段：REF_OREDERS
	 * 注释：varchar(50)
	 */
	private String refOreders;
 	/**
	 * 对应数据库字段：FAIL_MAX_NUM
	 * 注释：失败次数
	 */
	private Integer failMaxNum;
 	/**
	 * 对应数据库字段：PASS_TIME
	 * 注释：超时时间
	 */
	private Date passTime;
 	/**
	 * 对应数据库字段：WORKER_STATUS
	 * 注释：状态
	 */
	private Integer workerStatus;
 	/**
	 * 对应数据库字段：APP_LOGO
	 * 注释：预占应用名
	 */
	private String appLogo;
 	/**
	 * 对应数据库字段：YN
	 * 注释：是否有效
	 */
	private Integer yn;
 	/**
	 * 对应数据库字段：MSG
	 * 注释：执行结果信息
	 */
	private String msg;
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
	 * worker内容
	 */
	private WorkerContent workerContent;
	/**
	 * 执行结果信息
	 */
	public void setMsg(String msg) {
		if(msg != null && msg.length() > 50){
			this.msg = msg.substring(0,50);
		}else{
			this.msg = msg;
		}
	}
	/**
	 * 	workerType文本
	 * @return
	 */
	public String getWorkerTypeName() {
		return WorkerEnum.toText(workerTypeCode);
	}
	/**
	 * 	WtypeText文本
	 * @return
	 */
	public String getWtypeText() {
		return WorkerTypeEnum.toText(wtype);
	}
	/**
	 * 	状态文本
	 * @return
	 */
	public String getWorkerStatusText() {
		return WorkerStatusEnum.toText(workerStatus);
	}
	/**
	 * 	是否有效文本
	 * @return
	 */
	public String getYnText() {
		return YnEnum.toText(yn);
	}
	
	public WorkerContent getWorkerContent() {
		return workerContent;
	}
	public void setWorkerContent(WorkerContent workerContent) {
		this.workerContent = workerContent;
	}
	/**
	 * 	主键
	 * @return
	 */
	public Integer getWorkerId() {
		return workerId;
	}
	/**
	 * 主键
	 */
	public void setWorkerId(Integer workerId) {
		this.workerId = workerId;
	}
 	/**
	 * 	worker编码
	 * @return
	 */
	public String getWorkerTypeCode() {
		return workerTypeCode;
	}
	/**
	 * worker编码
	 */
	public void setWorkerTypeCode(String workerTypeCode) {
		this.workerTypeCode = workerTypeCode;
	}
 	/**
	 * 	work类型
	 * @return
	 */
	public Integer getWtype() {
		return wtype;
	}
	/**
	 * work类型
	 */
	public void setWtype(Integer wtype) {
		this.wtype = wtype;
	}
 	/**
	 * 	varchar(50)
	 * @return
	 */
	public String getRefOreders() {
		return refOreders;
	}
	/**
	 * varchar(50)
	 */
	public void setRefOreders(String refOreders) {
		this.refOreders = refOreders;
	}
 	/**
	 * 	失败次数
	 * @return
	 */
	public Integer getFailMaxNum() {
		return failMaxNum;
	}
	/**
	 * 失败次数
	 */
	public void setFailMaxNum(Integer failMaxNum) {
		this.failMaxNum = failMaxNum;
	}
 	/**
	 * 	超时时间
	 * @return
	 */
	public Date getPassTime() {
		return passTime;
	}
	/**
	 * 超时时间
	 */
	public void setPassTime(Date passTime) {
		this.passTime = passTime;
	}
 	/**
	 * 	状态
	 * @return
	 */
	public Integer getWorkerStatus() {
		return workerStatus;
	}
	/**
	 * 状态
	 */
	public void setWorkerStatus(Integer workerStatus) {
		this.workerStatus = workerStatus;
	}
 	/**
	 * 	预占应用名
	 * @return
	 */
	public String getAppLogo() {
		return appLogo;
	}
	/**
	 * 预占应用名
	 */
	public void setAppLogo(String appLogo) {
		this.appLogo = appLogo;
	}
 	/**
	 * 	是否有效
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
	 * 	执行结果信息
	 * @return
	 */
	public String getMsg() {
		return msg;
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
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	@Override
	public Worker clone() {
		Worker coloneObject = null;
		try {
           coloneObject = (Worker)super.clone();
           coloneObject.setWorkerId(this.getWorkerId());
           coloneObject.setWorkerTypeCode(this.getWorkerTypeCode());
           coloneObject.setWtype(this.getWtype());
           coloneObject.setRefOreders(this.getRefOreders());
           coloneObject.setFailMaxNum(this.getFailMaxNum());
           coloneObject.setPassTime(this.getPassTime());
           coloneObject.setWorkerStatus(this.getWorkerStatus());
           coloneObject.setAppLogo(this.getAppLogo());
           coloneObject.setYn(this.getYn());
           coloneObject.setMsg(this.getMsg());
           coloneObject.setCreateTime(this.getCreateTime());
           coloneObject.setUpdateTime(this.getUpdateTime());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();   
        }
		return coloneObject;
	}
}
