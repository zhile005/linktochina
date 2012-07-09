package com.oa.common.workUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import com.oa.common.pojo.WorkerType;
import com.oa.common.tpyeEnum.YnEnum;
import com.oa.dao.WorkerTypeDao;


public class ReloadWorkerEventEngine implements Job{

	@Resource
	private WorkerTypeDao workerTypeDao;
	
	private Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 自动加载version修改的worker
	 */
	public void execute(JobExecutionContext context){
		try {
			//查找已经被更新的workerType
			List<WorkerType> paraList = new ArrayList<WorkerType>();
			Map<String, WorkerType> workerTypeMap = WorkerQuartzUtil.getWorkerTypeMap();
			for (Map.Entry<String, WorkerType> me : workerTypeMap.entrySet()) {
				paraList.add(me.getValue());
			}
			List<WorkerType> toDos = workerTypeDao.getUpdatedWorkerType(paraList);
			for (WorkerType toDo : toDos) {
				if(YnEnum.Y.getValue() == toDo.getYn()){
					WorkerQuartzUtil.reloadWorker(toDo);
				}else{
					WorkerQuartzUtil.stopWorker(toDo);
				}
				logger.info("workerType更新执行成功+"+toDos.toString());
			}
			//业务监控
			WorkerType workerType = WorkerQuartzUtil.getWorkerTypeMap().get(WorkerQuartzUtil.rootEventWorkerCode);
			if(workerType != null && workerType.getWatchYn() == YnEnum.Y.getValue()){//开启业务监控
				workerTypeDao.updateWorkerTypeWatchStatus(workerType.getWorkerTypeCode());
			}
		} catch (Exception e) {
			logger.error("根节点执行异常"+e.getMessage(), e);
		}
	}
}
