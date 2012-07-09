package com.oa.common.workUtil;

import java.net.SocketException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.oa.common.pojo.WorkerException;
import com.oa.common.pojo.WorkerType;
import com.oa.common.tpyeEnum.WorkerTypeEnum;
import com.oa.common.tpyeEnum.YnEnum;
import com.oa.common.util.IpUtil;
import com.oa.dao.WorkerTypeDao;


public class WorkerQuartzUtil implements ApplicationContextAware, InitializingBean {

	@Resource
	private WorkerTypeDao workerTypeDao;

	/**
	 * 应用的唯一标示
	 */
	private static String applicationLogo = null;
	/**
	 * worker执行引擎缓存Map
	 */
	private static Map<String, BaseWorkerEngine> workerEngineMap = new ConcurrentHashMap<String, BaseWorkerEngine>();
	/**
	 * 自动重新加载状态改变的worker引擎
	 */
	private static Job reloadWorkerEventEngine;
	/**
	 * 默认的worker根事件编号
	 */
	public static String rootEventWorkerCode = "root";
	/**
	 * workerType缓存Map（用途：根据缓存状态和数据库状态不一致，更新应用的worker相关参数）
	 * 可能和数据库中不一致，不能通过本缓存确定数据库是否存在
	 */
	private static Map<String, WorkerType> workerTypeMap = new ConcurrentHashMap<String, WorkerType>();
	/**
	 * 定时调度器
	 */
	public static Scheduler scheduler = null;

	private static Logger logger = Logger.getLogger(WorkerQuartzUtil.class);
	/**
	 * spring加载器引用
	 */
	private ApplicationContext cpzac = null;
	/**
	 * 应用的标示
	 */
	private String applicationId;
	/**
	 * 自定义是否启动worker引擎
	 * 如：web应用就不需要启动worker引擎
	 */
	private boolean startWorker = true;
	/**
	 * work执行引擎spring注入列表
	 */
	private List<BaseWorkerEngine> workEngines;

	/**
	 * 初始化
	 */
	private synchronized void init() {
		try {
			WorkerQuartzUtil.applicationLogo = IpUtil.getLocalIp() + "/" + applicationId;
		} catch (SocketException e) {
			logger.error("请在服务器hosts配置localhost", e);
			System.exit(-1);
		}
	}
	/**
	 * 取得缓存worker类型（只读）
	 * @return
	 */
	public static Map<String, WorkerType> getWorkerTypeMap() {
		return new HashMap<String, WorkerType>(WorkerQuartzUtil.workerTypeMap);
	}
	/**
	 * 校验本worker是否具备执行条件
	 * @return
	 * @throws WorkerException 
	 */
	public static boolean checkCanRun(String workerTypeCode) throws WorkerException{
		WorkerType synEvent= WorkerQuartzUtil.workerTypeMap.get(workerTypeCode);
		if(WorkerTypeEnum.SYN_EVENT.getValue() == synEvent.getWtype()){
			//同步事件必须有子节点
			for (Map.Entry<String, WorkerType> me : WorkerQuartzUtil.workerTypeMap.entrySet()) {
				if(workerTypeCode.equals(me.getValue().getParentTypeCode())){
					return true;
				}
			}
			throw new WorkerException("同步事件["+workerTypeCode+"]必须具有子节点才能执行,请检查是否配置子节点");
		}else{
			return true;
		}
	}
	/**
	 * 取得同步事件下级所属的事件列表
	 * @return
	 */
	public static List<WorkerType> getNextLevelWorkerTypes(String workerTypeCode){
		List<WorkerType> list = new ArrayList<WorkerType>();
		WorkerType synEvent= WorkerQuartzUtil.workerTypeMap.get(workerTypeCode);
		if(WorkerTypeEnum.SYN_EVENT.getValue() == synEvent.getWtype()){
			//同步事件必须有子节点
			for (Map.Entry<String, WorkerType> me : WorkerQuartzUtil.workerTypeMap.entrySet()) {
				if(workerTypeCode.equals(me.getValue().getParentTypeCode())){
					list.add(me.getValue());
				}
			}
		}
		return list;
	}
	/**
	 * 根据获取引擎map
	 * @return
	 */
	public static Map<String, BaseWorkerEngine> getWorkerEngineMap() {
		return WorkerQuartzUtil.workerEngineMap;
	}
	/**
	 * 根据work引擎编码获取引擎
	 * @return
	 * @throws WorkerException 
	 */
	public static BaseWorkerEngine getWorkerEngine(String workerTypeCode) throws WorkerException {
		BaseWorkerEngine workEngine = null;
		if(workerTypeCode != null){
			workEngine = WorkerQuartzUtil.workerEngineMap.get(workerTypeCode);
		}
		if(workEngine == null){
			throw new WorkerException("找不到对应的worker执行引擎，请检查该worker["+workerTypeCode+"]是否部署或者引擎类是否已经添加到spring配置文件中");
		}
		return workEngine;
	}
	/**
	 * 获取spring的容器句柄
	 * 
	 * @return
	 */
	public ApplicationContext getContext() {
		return cpzac;
	}
	
	/**
	 * 初始化quartz
	 * 初始化workType缓存Map 从数据库提取相关数据
	 * @return
	 */
	private synchronized void initQuartz() {
		//检查work执行引擎是否有冲突，且缓存work执行引擎Map
		for (BaseWorkerEngine workEngine : workEngines) {
			BaseWorkerEngine preWorkEngine = WorkerQuartzUtil.workerEngineMap.get(workEngine.getWorkerTypeCode());
			if (preWorkEngine != null) {
				throw new RuntimeException("worker执行引擎workTypeCode冲突：" + preWorkEngine.getClass().getName() + "/"
						+ workEngine.getClass().getName());
			} else {
				WorkerQuartzUtil.workerEngineMap.put(workEngine.getWorkerTypeCode(), workEngine);
			}
		}
		try {
			// 从数据库查询workType类型，加载到workerTypeMap中
			List<WorkerType> allWorkTypeList = workerTypeDao.getAllWorkTypeList();
			if(allWorkTypeList.size() == 0){
				//系统第一次初始化根节点
				WorkerType rootWorkerType = new WorkerType();
				rootWorkerType.setWorkerTypeCode(WorkerQuartzUtil.rootEventWorkerCode);
				rootWorkerType.setWorkerTypeName("根节点(版本自动加载worker)");
				rootWorkerType.setWtype(WorkerTypeEnum.SYN_EVENT.getValue());
				rootWorkerType.setFailMaxNum(1);
				rootWorkerType.setPassTimeNum(1.0);
				rootWorkerType.setParentTypeCode("0");
				rootWorkerType.setYn(YnEnum.Y.getValue());
				rootWorkerType.setCronexpression("0 0/10 * * * ?");
				rootWorkerType.setVersion(1);
				rootWorkerType.setWatchYn(YnEnum.Y.getValue());
				workerTypeDao.saveWorkerType(rootWorkerType);
				//重新取得列表
				allWorkTypeList = workerTypeDao.getAllWorkTypeList();
			}
			for (WorkerType WorkerType : allWorkTypeList) {
				WorkerQuartzUtil.workerTypeMap.put(WorkerType.getWorkerTypeCode(), WorkerType);
			}
			
			// 部署任务
			SchedulerFactory schedulerFactory = new StdSchedulerFactory();
			scheduler = schedulerFactory.getScheduler();
			if(this.startWorker){
				for (WorkerType WorkerType : allWorkTypeList) {
					//注册work执行引擎
					if(YnEnum.Y.getValue() == WorkerType.getYn() && workEngines != null){//只执行在数据库已经配置生效的worker
						if(WorkerQuartzUtil.rootEventWorkerCode.equals(WorkerType.getWorkerTypeCode())){
							// 根节点负责重新加载版本改变的worker，不同于一般的流程，使用spring注入属性[WorkerQuartzUtil.reloadWorkerEventEngine]
							if(WorkerQuartzUtil.reloadWorkerEventEngine == null){
								throw new RuntimeException("work["+WorkerType.getWorkerTypeCode()+"]没有使用spring注入属性[WorkerQuartzUtil.reloadWorkerEventEngine]");
							}
						} else {
							BaseWorkerEngine workEngine = WorkerQuartzUtil.getWorkerEngine(WorkerType.getWorkerTypeCode());
							if (workEngine == null) {
								throw new RuntimeException("work["+WorkerType.getWorkerTypeCode()+"]没有找到对应的执行引擎！！！！！！！！！");
							}
						}
						JobDetail jobDetail = new JobDetail(WorkerType.getWorkerTypeCode(), BaseQuartzWorker.class);
						CronTrigger cronTrigger = new CronTrigger("cronTrigger" + WorkerType.getWorkerTypeCode(),
								"group", WorkerType.getCronexpression());
						scheduler.scheduleJob(jobDetail, cronTrigger);
						logger.info("成功部署worker类型[" + WorkerType.getWorkerTypeCode() + "]引擎");
					}
				}
			}
			scheduler.start();
			logger.info("成功启动worker驱动服务");
		} catch (ParseException e) {
			logger.error("JobDetail克隆表达式解析异常", e);
		} catch (SchedulerException se) {
			logger.error("quartz引擎异常", se);
		} catch (WorkerException we) {
			logger.error("quartz引擎异常", we);
		}
	}

	/**
	 * 停止work
	 * @param workType
	 * @return
	 */
	public static boolean stopWorker(WorkerType workType) {
		try {
			scheduler.deleteJob(workType.getWorkerTypeCode(), null);
			//更新缓存
			WorkerQuartzUtil.workerTypeMap.put(workType.getWorkerTypeCode(), workType);
			logger.info("成功停止执行job：" + workType.getWorkerTypeCode());
			return true;
		} catch (SchedulerException se) {
			logger.error("quartz引擎异常", se);
		}
		return false;
	}

	/**
	 * 重新加载work（work的克隆表达式改变）
	 * 
	 * @param workerType
	 * @return
	 */
	public static boolean reloadWorker(WorkerType workerType) {
		try {
			CronTrigger newCronTrigger = new CronTrigger("cronTrigger_" + workerType.getWorkerTypeCode(), "group",
					workerType.getCronexpression());
			JobDetail newJobDetail = scheduler.getJobDetail(workerType.getWorkerTypeCode(), null);
			if(newJobDetail != null){
				scheduler.deleteJob(workerType.getWorkerTypeCode(), null);
				logger.info("成功停止执行job：" + workerType.getWorkerTypeCode());
			}else{
				newJobDetail = new JobDetail(workerType.getWorkerTypeCode(), BaseQuartzWorker.class);
			}
			scheduler.scheduleJob(newJobDetail, newCronTrigger);
			//更新缓存
			WorkerQuartzUtil.workerTypeMap.put(workerType.getWorkerTypeCode(), workerType);
			logger.info("成功重新部署job：" + workerType.getWorkerTypeCode());
			return true;
		} catch (ParseException e) {
			logger.error("JobDetail克隆表达式解析异常", e);
		} catch (SchedulerException se) {
			logger.error("quartz引擎异常", se);
		}
		return false;
	}

	@Override
	protected void finalize() throws Throwable {
		if (scheduler != null) {
			scheduler.shutdown();
		}
		super.finalize();
	}

	public List<BaseWorkerEngine> getWorkEngines() {
		return workEngines;
	}

	public void setWorkEngines(List<BaseWorkerEngine> workEngines) {
		this.workEngines = workEngines;
	}

	public static String getApplicationLogo() {
		return WorkerQuartzUtil.applicationLogo;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public void setStartWorker(boolean startWorker) {
		this.startWorker = startWorker;
	}

	public void afterPropertiesSet() throws Exception {
		init();
		initQuartz();
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.cpzac = applicationContext;
	}
	public static Job getReloadWorkerEventEngine() {
		return reloadWorkerEventEngine;
	}
	public void setReloadWorkerEventEngine(Job reloadWorkerEventEngine) {
		WorkerQuartzUtil.reloadWorkerEventEngine = reloadWorkerEventEngine;
	}
}
