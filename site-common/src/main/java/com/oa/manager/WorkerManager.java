package com.oa.manager;

import com.oa.common.pojo.Worker;

public interface WorkerManager{
	
	public int saveWorker(Worker work);
	public int successWorker(Worker worker,Object refObject);
}
