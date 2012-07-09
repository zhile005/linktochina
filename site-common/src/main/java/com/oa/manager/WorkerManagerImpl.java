package com.oa.manager;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.oa.common.BaseManager;
import com.oa.common.pojo.Worker;
import com.oa.common.pojo.WorkerType;
import com.oa.common.tpyeEnum.WorkerStatusEnum;
import com.oa.common.tpyeEnum.WorkerTypeEnum;
import com.oa.common.workUtil.WorkerQuartzUtil;
import com.oa.dao.WorkerDao;

@Component("workerManager")
public class WorkerManagerImpl extends BaseManager implements WorkerManager {

	@Resource
	private WorkerDao workerDao;
	private Logger logger = Logger.getLogger(getClass());

	public int saveWorker(final Worker worker) {
		TransactionTemplate dataSourceTransactionManager = super.getDataSourceTransactionManager();
		return dataSourceTransactionManager.execute(new TransactionCallback<Integer>() {
			public Integer doInTransaction(TransactionStatus status) {
				try {
					int workerId = workerDao.saveWorker(worker);
					if (workerId == 0) {
						throw new Exception("save返回ID是0");
					}
					worker.setWorkerId(workerId);
					return workerId;
				} catch (Exception e) {
					status.setRollbackOnly();
					logger.error("事务异常" + worker.toString(), e);
					throw new RuntimeException(e.getMessage() + ":导致事务异常");
				}
			}
		});
	}

	public int successWorker(final Worker worker, final Object refObject) {
		TransactionTemplate dataSourceTransactionManager = super.getDataSourceTransactionManager();
		return dataSourceTransactionManager.execute(new TransactionCallback<Integer>() {
			public Integer doInTransaction(TransactionStatus status) {
				try {
					worker.setWorkerStatus(WorkerStatusEnum.SUCCESS.getValue());
					int num = workerDao.updateWorkerStatus2Msg(worker);
					if (num != 1) {
						throw new Exception("主键更新返回记录数不是1");
					}
					if (WorkerTypeEnum.SYN_EVENT.getValue() == worker.getWtype()) {
						// 同步事件,自动生成所属的事件
						List<WorkerType> nextLevelWorkerTypes = WorkerQuartzUtil.getNextLevelWorkerTypes(worker.getWorkerTypeCode());
						if (nextLevelWorkerTypes.size() == 0) {
							throw new Exception("该同步事件没有所属子事件,不能执行!");
						}
						for (WorkerType workerType : nextLevelWorkerTypes) {
							// 生成子节点
							WorkerQuartzUtil.getWorkerEngine(workerType.getWorkerTypeCode()).createWorker(refObject);
						}
					}
					return num;
				} catch (Exception e) {
					status.setRollbackOnly();
					logger.error("事务异常" + worker.toString(), e);
					throw new RuntimeException(e.getMessage() + ":导致事务异常");
				}
			}
		});
	}
}
