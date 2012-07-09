package com.oa.common;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * manager基类 User: Administrator Date: 2010-4-26 Time: 15:52:43
 */
public class BaseManager {

	private PlatformTransactionManager transactionManager;

	public TransactionTemplate getDataSourceTransactionManager() {
		return new TransactionTemplate(transactionManager);
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
}
