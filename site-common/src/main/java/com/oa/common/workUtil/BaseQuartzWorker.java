package com.oa.common.workUtil;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

import com.oa.common.pojo.WorkerException;


public class BaseQuartzWorker implements Job{
	
	private Logger logger = Logger.getLogger(getClass());
	public final void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		try {
			String workerTypeCode = jobDetail.getName();
			if(WorkerQuartzUtil.rootEventWorkerCode.equals(workerTypeCode)){
				//根节点执行事件(负责轮询重新加载worker)
				Job reloadWorkerEventEngine = WorkerQuartzUtil.getReloadWorkerEventEngine();
				reloadWorkerEventEngine.execute(context);
			}else{
				BaseWorkerEngine workEngine = WorkerQuartzUtil.getWorkerEngine(workerTypeCode);
				workEngine.execute(context);
			}
			if(logger.isDebugEnabled()){
				Trigger trigger = context.getTrigger();
				String des = trigger.getNextFireTime().toString();
				if(trigger instanceof CronTrigger){
					des = ((CronTrigger)trigger).getCronExpression();
				}
				logger.debug("进程执行jobdetail["+jobDetail.getName()+"]/CronExpression?nextTime["+des+"]");
			}
		} catch (WorkerException e) {
			logger.error("work["+jobDetail.getName()+"]没有找到对应的执行器！:"+e.getMessage());
		}
	}
}
