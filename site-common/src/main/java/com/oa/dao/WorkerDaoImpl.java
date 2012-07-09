package com.oa.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Component;

import com.oa.common.pojo.Paginable;
import com.oa.common.pojo.SimplePage;
import com.oa.common.pojo.Worker;
import com.oa.common.pojo.WorkerContent;
import com.oa.common.tpyeEnum.WorkerStatusEnum;

@Component("workerDao")
public class WorkerDaoImpl extends SqlMapClientTemplate implements WorkerDao {

	public int saveWorker(final Worker worker) {
		int workerId = (Integer) insert("WORKER.saveWorker", worker);
		WorkerContent workerContent = worker.getWorkerContent();
		workerContent.setWorkerId(workerId);
		// 若不是异步worker则保存内容
		insert("WORKER.saveWorkerContent", workerContent);
		return workerId;
	}

	public int lockWorkerList(Map<String, Object> paraMap) {
		return update("WORKER.lockWorkerList", paraMap);
	}

	public List<Worker> getWorkerListLocked(Map<String, Object> paraMap) {
		return queryForList("WORKER.getWorkerListLocked", paraMap);
	}

	public int updateWorkerStatus2Msg(Worker work) {
		return update("WORKER.updateWorkerStatus2Msg", work);
	}

	public int reduceWorkerFallnum(Worker worker) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("workerId", worker.getWorkerId());
		paraMap.put("failStatus", WorkerStatusEnum.FAILED.getValue());
		return update("WORKER.reduceWorkerFallnum", paraMap);
	}

	public boolean checkExistsTask(String workerTypeCode) {
		return ((Integer) queryForObject("WORKER.checkExistsTask", workerTypeCode)) > 0;
	}

	public WorkerContent getWorkerContent(int refId) {
		return (WorkerContent) queryForObject("WORKER.getWorkerContent", refId);
	}

	public Paginable<Worker> doWorker(int page, int rows, Map<String, Object> paraMap) {
		Paginable<Worker> sp = new SimplePage<Worker>(page, rows);
		paraMap.put("startNum", sp.getStartRow());
		paraMap.put("endNum", sp.getEndRow());
		sp.setPageData(super.queryForList("WORKER.getPageDateByPara", paraMap));
		sp.setTotalCount((Integer) queryForObject("WORKER.getPageCountByPara", paraMap));
		return sp;
	}

	public List<Map<String, Object>> getCustSql(String sql) {
		return queryForList("WORKER.getByCustSql", sql);
	}
}
