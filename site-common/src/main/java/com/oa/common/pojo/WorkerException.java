package com.oa.common.pojo;

public class WorkerException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3845621463987328706L;
	/**
	 * 用于work执行异常报警
	 * @param Msg
	 */
	public WorkerException(String msg) {
		super(msg);
	}
	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
