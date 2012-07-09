package com.oa.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Component;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.oa.common.pojo.WorkerType;
import com.oa.common.tpyeEnum.WorkerStatusEnum;
import com.oa.common.tpyeEnum.YnEnum;

@Component("workerTypeDao")
public class WorkerTypeDaoImpl extends SqlMapClientTemplate implements WorkerTypeDao {

	public int saveWorkerType(final WorkerType workerType) {
		return update("WORKER_TYPE.saveWorkerType", workerType);
	}

	public int btSaveWorkerType(final List<WorkerType> workerTypes) {
		return super.execute(new SqlMapClientCallback<Integer>() {
			public Integer doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
				executor.startBatch();
				for (WorkerType workerType : workerTypes) {
					executor.update("WORKER_TYPE.saveWorkerType", workerType);
				}
				return executor.executeBatch();
			}
		});
	}

	public int updateWorkerType(final WorkerType workerType) {
		return update("WORKER_TYPE.updateWorkerType", workerType);
	}

	public List<WorkerType> getAllWorkTypeList() {
		return queryForList("WORKER_TYPE.getAll");
	}

	public List<WorkerType> getNextLevel(String workerTypeCode) {
		return queryForList("WORKER_TYPE.getNextLevel", workerTypeCode);
	}

	public WorkerType getInfo(String workerTypeCode) {
		return (WorkerType) queryForObject("WORKER_TYPE.getInfo", workerTypeCode);
	}

	public int doDeleteWorkerType(WorkerType workerType) {
		return update("WORKER_TYPE.doDeleteWorkerType", workerType.getWorkerTypeCode());
	}

	public boolean checkExistsNextLevel(String workerTypeCode) {
		return queryForList("WORKER_TYPE.getNextLevel", workerTypeCode).size() > 0;
	}

	public List<WorkerType> getUpdatedWorkerType(List<WorkerType> paraList) {
		List<WorkerType> reList = new ArrayList<WorkerType>();
		// if(paraList != null && paraList.size() != 0){
		// //取得最新的版本改变的worker和新部署的worker
		// reList = queryForList("WORKER_TYPE.getUpdatedWorkerType", paraList);
		// }
		List<WorkerType> reDbList = super.queryForList("WORKER_TYPE.getAll");
		boolean pass = false;
		for (WorkerType dbType : reDbList) {
			pass = false;
			for (WorkerType cacheType : paraList) {
				if (dbType.equals(cacheType)) {
					pass = true;
					break;
				}
			}
			if (pass == false) {
				// 新建的或者版本修改的worker类型
				reList.add(dbType);
			}
		}
		for (WorkerType cacheType : paraList) {
			pass = false;
			for (WorkerType dbType : reDbList) {
				if (cacheType.getWorkerTypeCode().equals(dbType.getWorkerTypeCode())) {
					pass = true;
					break;
				}
			}
			if (pass == false) {
				// 已经删除的worker
				cacheType.setYn(YnEnum.N.getValue());
				cacheType.setVersion(-1);
				reList.add(cacheType);
			}
		}
		return reList;
	}

	public int updateWorkerTypeWatchStatus(String workerTypeCode) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("workerTypeCode", workerTypeCode);
		paraMap.put("waitStatus", WorkerStatusEnum.READY.getValue());
		return update("WORKER_TYPE.updateWorkerTypeWatchStatus", paraMap);
	}
}
