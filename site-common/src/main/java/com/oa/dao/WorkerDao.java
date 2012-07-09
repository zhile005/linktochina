package com.oa.dao;

import java.util.List;
import java.util.Map;

import com.oa.common.pojo.Paginable;
import com.oa.common.pojo.Worker;
import com.oa.common.pojo.WorkerContent;


public interface WorkerDao {

	public abstract int saveWorker(Worker worker);
	public abstract int lockWorkerList(Map<String,Object> paraMap);
	public abstract List<Worker> getWorkerListLocked(Map<String,Object> paraMap);
	public abstract int updateWorkerStatus2Msg(Worker work);
	public abstract boolean checkExistsTask(String workerTypeCode);
	public abstract int reduceWorkerFallnum(Worker worker);
	public abstract WorkerContent getWorkerContent(int refId);
	public abstract Paginable<Worker> doWorker(int page, int rows, Map<String, Object> para);
	public abstract List<Map<String,Object>> getCustSql(String sql);
}
