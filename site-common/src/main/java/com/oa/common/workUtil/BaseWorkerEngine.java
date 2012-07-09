package com.oa.common.workUtil;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;

import com.oa.common.pojo.Worker;
import com.oa.common.pojo.WorkerContent;
import com.oa.common.pojo.WorkerException;
import com.oa.common.pojo.WorkerResult;
import com.oa.common.pojo.WorkerType;
import com.oa.common.tpyeEnum.WorkerStatusEnum;
import com.oa.common.tpyeEnum.YnEnum;
import com.oa.dao.WorkerDao;
import com.oa.dao.WorkerTypeDao;
import com.oa.manager.WorkerManager;
import com.thoughtworks.xstream.XStream;

public abstract class BaseWorkerEngine<T> {

	@Resource
	protected WorkerDao workerDao;
	@Resource
	protected WorkerTypeDao workerTypeDao;
	@Resource
	protected WorkerManager workerManager;

	protected Logger logger = Logger.getLogger(this.getClass());

	/**
	 * 注册该worker引擎的类型编号（必须唯一） 程序启动的时候会校验
	 * 
	 * @return
	 */
	public abstract String getWorkerTypeCode();

	/**
	 * 执行work 若有业务更新异常，必须抛出WorkerException 若执行，返回WorkerResult
	 * 
	 * @return
	 */
	protected abstract WorkerResult executeWorker(Worker worker, T refObject) throws WorkerException;

	/**
	 * 初始化worker
	 * 
	 * @return
	 */
	protected abstract Worker createInitWorker(T contentObject);

	/**
	 * quartzjob执行接口方法
	 * 
	 * @param context
	 */
	public final void execute(JobExecutionContext context) {

		try {
			String workerTypeCode = context.getJobDetail().getName();
			lockReadyWork(workerTypeCode);
			// 锁定work后查询锁定的work
			List<Worker> queryReadyWorklist = queryLockedWorklist(workerTypeCode);
			// 注意还有以前锁定同步worker，现在才执行的情况
			if (queryReadyWorklist != null) {
				for (Worker worker : queryReadyWorklist) {
					try {
						// 校验是否可以本worker可执行
						WorkerQuartzUtil.checkCanRun(worker.getWorkerTypeCode());
						T refObject = unmarshallWorker(worker);
						WorkerResult nowait = executeWorker(worker, refObject);
						worker.setMsg(nowait.getMsg());
						// 根据执行结果决定是否延迟执行
						if (nowait.isWait() == false) {
							if (nowait.isSuccess()) {
								successWorker(worker, refObject);
							} else {
								failWorker(worker);
							}
						}
					} catch (WorkerException e) {
						// 业务异常
						worker.setMsg(e.getMessage());
						failWorker(worker);
					} catch (Exception e) {
						// 系统异常
						logger.error("work执行失败！" + worker.toString(), e);
						worker.setMsg(e.getMessage() + "/" + e.getStackTrace());
						failWorker(worker);
					}
				}
			}
			//业务监控
			WorkerType workerType = WorkerQuartzUtil.getWorkerTypeMap().get(workerTypeCode);
			if(workerType != null && workerType.getWatchYn() == YnEnum.Y.getValue()){//开启业务监控
				workerTypeDao.updateWorkerTypeWatchStatus(workerTypeCode);
			}
		} catch (Exception e) {
			// 捕获所有的数据库异常
			logger.error("work执行失败！" + e.getMessage(), e);
		}
	}

	/**
	 * 用终端Ip+应用进程号作为应用的唯一标示 锁定要操作的work
	 * 
	 * @param workerTypeCode
	 * @return
	 */
	protected boolean lockReadyWork(String workerTypeCode) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("workerTypeCode", workerTypeCode);
		paraMap.put("readyWorkerStatus", WorkerStatusEnum.READY.getValue());
		paraMap.put("appLogo", WorkerQuartzUtil.getApplicationLogo());
		paraMap.put("lockedWorkerStatus", WorkerStatusEnum.LOCKED.getValue());
		if (WorkerQuartzUtil.getApplicationLogo().indexOf("127.0.0.1") != -1) {
			logger.error("系统工具类取得IP为127.0.0.1，系统不成正常运行，请检查！！！！");
			return false;
		} else {
			int num = workerDao.lockWorkerList(paraMap);
			if (num > 0) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 取得预占的类型的work列表
	 * 
	 * @param workerTypeCode
	 * @return
	 */
	protected List<Worker> queryLockedWorklist(String workerTypeCode) {
		if (WorkerQuartzUtil.getApplicationLogo().indexOf("127.0.0.1") != -1) {
			logger.error("系统工具类取得IP为127.0.0.1，系统不成正常运行，请检查！！！！");
			return null;
		}
		Map<String, Object> paraMap = new HashMap<String, Object>();
		// 若是事件通用worker执行引擎，则取得所有事件
		paraMap.put("workerTypeCode", workerTypeCode);
		paraMap.put("appLogo", WorkerQuartzUtil.getApplicationLogo());
		paraMap.put("lockedWorkerStatus", WorkerStatusEnum.LOCKED.getValue());
		paraMap.put("failedWorkerStatus", WorkerStatusEnum.FAILED.getValue());
		return workerDao.getWorkerListLocked(paraMap);
	}

	/**
	 * 执行成功并自动执行同步worker的后续（将同步worker所属的子节点worker生成）
	 * 
	 * @return
	 */
	protected void successWorker(Worker worker, T refObject) throws WorkerException {
		if (workerManager.successWorker(worker, refObject) != 1) {
			throw new WorkerException("数据库更新记录数（更新返回记录数<>1）异常，已回滚！");
		}
	}

	/**
	 * 执行失败结束
	 * 
	 * @return
	 */
	protected void failWorker(Worker worker) throws WorkerException {
		worker.setWorkerStatus(WorkerStatusEnum.FAILED.getValue());
		if (workerDao.updateWorkerStatus2Msg(worker) != 1) {
			throw new WorkerException("数据库更新记录数（更新返回记录数<>1）异常，已回滚！");
		}
	}

	/**
	 * 解析worker共通辅助类
	 * 
	 * @return
	 */
	protected T unmarshallWorker(Worker worker) {
		WorkerContent workerContent = workerDao.getWorkerContent(worker.getWorkerId());
		if (workerContent != null) {
			XStream xs = new XStream();
			xs.autodetectAnnotations(true);
			return (T) xs.fromXML(workerContent.getWorkerContent());
		} else {
			return null;
		}
	};

	/**
	 * 生成worker共通辅助类
	 * 
	 * @return
	 * @throws Exception
	 */
	public Worker createWorker(T contentObject) throws WorkerException {

		Worker saveWorker = createInitWorker(contentObject);
		// 校验参数校验
		if (saveWorker.getRefOreders() == null) {
			throw new WorkerException("关联订单号[RefOreders]不能为null,必须在createInitWorker方法中初始化,若确没有值，可赋值0");
		}
		saveWorker.setWorkerTypeCode(getWorkerTypeCode());
		// 取出workerType相关参数
		WorkerType workerType = workerTypeDao.getInfo(saveWorker.getWorkerTypeCode());
		if (workerType == null) {
			throw new WorkerException("请确认workerTypeCode是否正确，导致取到的WorkerType为null");
		}
		saveWorker.setWtype(workerType.getWtype());
		// 失败次数
		saveWorker.setFailMaxNum(0);
		Calendar cld = Calendar.getInstance();
		cld.add(Calendar.HOUR, new Double(workerType.getPassTimeNum() * 24).intValue());
		// 设置超时时间
		saveWorker.setPassTime(cld.getTime());
		saveWorker.setWorkerStatus(WorkerStatusEnum.READY.getValue());
		saveWorker.setYn(YnEnum.Y.getValue());
		if (contentObject != null) {
			WorkerContent workerContent = new WorkerContent();
			XStream xs = new XStream();
			xs.autodetectAnnotations(true);
			workerContent.setWorkerContent(xs.toXML(contentObject));
			saveWorker.setWorkerContent(workerContent);
		}
		workerManager.saveWorker(saveWorker);
		return saveWorker;
	};
}
