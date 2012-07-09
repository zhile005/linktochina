package com.oa.dao;

import java.util.List;

import com.oa.common.pojo.WorkerType;



public interface WorkerTypeDao {
	
	public int saveWorkerType(WorkerType workerType);
	public int updateWorkerType(WorkerType workerType);
	public List<WorkerType> getAllWorkTypeList();
	public List<WorkerType> getNextLevel(String workerTypeCode);
	public WorkerType getInfo(String workerTypeCode);
	public int doDeleteWorkerType(WorkerType workerType);
	public boolean checkExistsNextLevel(String workerTypeCode);
	public List<WorkerType> getUpdatedWorkerType(List<WorkerType> paraList);
	public int updateWorkerTypeWatchStatus(String workerTypeCode);
}
