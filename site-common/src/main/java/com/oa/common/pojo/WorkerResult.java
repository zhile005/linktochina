package com.oa.common.pojo;
public class WorkerResult {
	
	//执行结果 true：成功、false：失败
 	private boolean success = false;
	//等待 true：执行条件不满足等待、false：立即执行
 	private boolean wait = false;
 	//异常信息
 	private String msg;
 	public WorkerResult(){
 	}
 	public WorkerResult(boolean wait,String msg){
 		this.wait = wait;
 		this.msg = msg;
 	}
 	/**
 	 * 是否等待
 	 * @return
 	 */
	public boolean isWait() {
		return wait;
	}
	/**
 	 * 是否等待
 	 */
	public void setWait(boolean wait) {
		this.wait = wait;
	}
	/**
 	 * 信息
 	 */
	public String getMsg() {
		return msg;
	}
	/**
 	 * 信息
 	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
}
