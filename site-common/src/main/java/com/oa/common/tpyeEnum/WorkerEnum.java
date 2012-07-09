package com.oa.common.tpyeEnum;

import java.util.ArrayList;
import java.util.List;

import com.oa.common.pojo.WorkerType;


/**
 * 
 */
public enum WorkerEnum {
	gatherPayable("gatherPayable","根据结算单汇总应付帐"),
	makePaySheet("makePaySheet","生成结算明细"),
	noticePaySheet("noticePaySheet","通知POP结算单状态变化"),
	autoHx("autoHx","结算单自动核销发票"),
	autoHx2("autoHx2","结算单二次核销发票"),
	autoAppr("autoAppr","结算单自动审批"),
	autoReleaseHx("autoReleaseHx","结算单解除核销");
	
	private final String workerTypeCode;
	private final String workerTypeName;
	
	private WorkerEnum(String workerTypeCode,String workerTypeName){
		this.workerTypeCode = workerTypeCode;
		this.workerTypeName = workerTypeName;
	}
	public String getWorkerTypeCode() {
        return workerTypeCode;
    }
	public String getWorkerTypeName() {
        return workerTypeName;
    }
	public static String toText(String workerTypeCode) {
		String returnText = "未知";
		if(workerTypeCode != null){
			for(WorkerEnum ot:values()){
				if(ot.getWorkerTypeCode().equals(workerTypeCode)){
					returnText = ot.getWorkerTypeName();
					break;
				}
			}
		}
        return returnText;
    }
	/**
	 * 筛选未部署的worker
	 * @param deployedWorkerList
	 * @return
	 */
	public static List<WorkerType> getNoDeployWorkerList(List<WorkerType> deployedWorkerList) {
		List<WorkerType> rl = new ArrayList<WorkerType>();
		boolean exist = false;
		for(WorkerEnum ot:values()){
			if(deployedWorkerList != null){
				exist = false;
				for (WorkerType workerType : deployedWorkerList) {
					if(ot.getWorkerTypeCode().equals(workerType.getWorkerTypeCode())){
						exist = true;
						break;
					}
				}
			}
			if(exist == false){
				WorkerType wt = new WorkerType();
				wt.setWorkerTypeCode(ot.getWorkerTypeCode());
				wt.setWorkerTypeName(ot.getWorkerTypeName());
				rl.add(wt);
			}
		}
        return rl;
    }
}
