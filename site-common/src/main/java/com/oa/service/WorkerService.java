package com.oa.service;

import java.util.Map;

import com.oa.common.pojo.Result;
import com.oa.common.pojo.Worker;
import com.oa.common.pojo.WorkerType;

public interface WorkerService {
	
	public Result doSaveWorkerType(WorkerType workerType);
	public Result doUpdateWorkerType(WorkerType workerType);
	public Result getNoDeployWorkerList();
	public Result doWorkerTypeTree(String workerTypeCode);
	public Result doDeleteWorkerType(WorkerType workerType);
	public Result doWorker(int page, int rows, Map<String, Object> para);
	public Result reduceWorkerFallnum(Worker worker);
	public Result doShowWorkerContent(Worker worker);
	public Result getCustSql(String sql);
}
