package com.oa.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.oa.common.pojo.JsTreeNode;
import com.oa.common.pojo.JsTreeNode.NodeDate;
import com.oa.common.pojo.Paginable;
import com.oa.common.pojo.Result;
import com.oa.common.pojo.Worker;
import com.oa.common.pojo.WorkerType;
import com.oa.common.tpyeEnum.WorkerEnum;
import com.oa.common.tpyeEnum.WorkerTypeEnum;
import com.oa.common.tpyeEnum.YnEnum;
import com.oa.common.workUtil.WorkerQuartzUtil;
import com.oa.dao.WorkerDao;
import com.oa.dao.WorkerTypeDao;
import com.oa.manager.WorkerManager;

@Component("workerService")
public class WorkerServiceImpl implements WorkerService {

	@Resource
	protected WorkerDao workerDao;
	@Resource
	private WorkerTypeDao workerTypeDao;
	@Resource
	protected WorkerManager workerManager;

	private Logger logger = Logger.getLogger(getClass());

	public Result getNoDeployWorkerList() {
		Result result = new Result(false);
		try {
			result.setSuccess(true);
			result.addDefaultModel(WorkerEnum.getNoDeployWorkerList(workerTypeDao.getAllWorkTypeList()));
		} catch (Exception e) {
			logger.error("save出现错误", e);
		}
		return result;
	}

	public Result doSaveWorkerType(WorkerType workerType) {
		Result result = new Result(false);
		try {
			int num = workerTypeDao.saveWorkerType(workerType);
			if (num == 1) {
				result.setSuccess(true);
			}
		} catch (Exception e) {
			logger.error("save出现错误", e);
		}
		return result;
	}

	public Result doUpdateWorkerType(WorkerType workerType) {
		Result result = new Result(false);
		try {
			int num = workerTypeDao.updateWorkerType(workerType);
			if (num == 1) {
				WorkerType info = workerTypeDao.getInfo(workerType.getWorkerTypeCode());
				result.addDefaultModel(info);
				result.setSuccess(true);
			}
		} catch (Exception e) {
			logger.error("update出现错误", e);
		}
		return result;
	}

	public Result doWorkerTypeTree(String workerTypeCode) {
		Result result = new Result(false);
		try {
			List<WorkerType> workerListLocked = workerTypeDao.getNextLevel(workerTypeCode);
			JsTreeNode jtnparent = new JsTreeNode<WorkerType>();
			for (WorkerType workerType : workerListLocked) {
				JsTreeNode<WorkerType> child = new JsTreeNode<WorkerType>();
				NodeDate createNodeDate = child.createNodeDate();
				child.setAttr(workerType);
				if (WorkerTypeEnum.SYN_EVENT.getValue() == workerType.getWtype()) {
					createNodeDate.setIcon("jd-tree-syn-event");
					child.setState("closed");
				} else if (WorkerTypeEnum.ASY_WORKER.getValue() == workerType.getWtype()) {
					createNodeDate.setIcon("jd-tree-asy-worker");
				}
				String title = "[" + workerType.getWtypeText() + "]" + workerType.getWorkerTypeName();
				if (YnEnum.Y.getValue() == workerType.getYn()) {
					title += "√";
				} else {
					title += "×";
				}
				createNodeDate.setTitle(title);
				createNodeDate.setExtTitle("版本：" + workerType.getVersion() + "\\克隆表达式："
						+ workerType.getCronexpression());
				child.setData(createNodeDate);
				jtnparent.addChild(child);
			}
			result.setSuccess(true);
			result.addDefaultModel(jtnparent);
		} catch (Exception e) {
			logger.error("查询出现错误", e);
		}
		return result;
	}

	public Result doDeleteWorkerType(WorkerType workerType) {
		Result result = new Result(false);
		try {
			boolean existsTask = workerDao.checkExistsTask(workerType.getWorkerTypeCode());
			boolean existsNextLevel = workerTypeDao.checkExistsNextLevel(workerType.getWorkerTypeCode());
			if (WorkerQuartzUtil.rootEventWorkerCode.equals(workerType.getWorkerTypeCode())) {
				result.setResultCode("根节点,不能删除");
			} else if (existsTask) {
				result.setResultCode("已经生成任务的不能删除");
			} else if (existsNextLevel) {
				result.setResultCode("有下级节点的不能删除");
			} else {
				int num = workerTypeDao.doDeleteWorkerType(workerType);
				if (num == 1) {
					result.setSuccess(true);
				}
			}
		} catch (Exception e) {
			logger.error("update出现错误", e);
		}
		return result;
	}

	public Result doWorker(int page, int rows, Map<String, Object> para) {
		Result result = new Result(false);
		try {
			Paginable<Worker> workerList = workerDao.doWorker(page, rows, para);
			if (workerList != null) {
				result.addDefaultModel(workerList);
				result.setSuccess(true);
			}
		} catch (Exception e) {
			logger.error("出现错误", e);
		}
		return result;
	}

	public Result reduceWorkerFallnum(Worker worker) {
		Result result = new Result(false);
		try {
			if (worker != null) {
				int updateNum = workerDao.reduceWorkerFallnum(worker);
				if (updateNum == 1) {
					result.setSuccess(true);
				}
			}
		} catch (Exception e) {
			logger.error("出现错误", e);
		}
		return result;
	}

	public Result doShowWorkerContent(Worker worker) {

		Result result = new Result(false);
		try {
			result.addDefaultModel(workerDao.getWorkerContent(worker.getWorkerId()));
			result.setSuccess(true);
		} catch (Exception e) {
			logger.error("出现错误", e);
		}
		return result;
	}

	public Result getCustSql(String sql) {
		Result result = new Result(false);
		try {
			result.addDefaultModel(workerDao.getCustSql(sql));
			result.setSuccess(true);
		} catch (Exception e) {
			logger.error("出现错误", e);
			result.setResultCode(e.getMessage());
		}
		return result;
	}
}
