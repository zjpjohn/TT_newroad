package com.newroad.fileext.transaction;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.newroad.fileext.utilities.FileResourceException;
import com.newroad.mongodb.orm.transaction.TransactionManager;

/**
 * @info : 事务管理
 * @author: tangzj1
 * @data : 2013-10-15
 * @since : 2.0
 */
public class TransactionInterceptor implements MethodInterceptor {

	private TransactionManager manager;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object result = null;
	
		// 开启事务
		manager.openTransaction();
		try {
			// 执行目标对象
			result = invocation.proceed();
			// 提交事务
			manager.commitTransaction();
			return result;
		} catch (Exception e) {
			// 事务回滚
			manager.rollbackTransaction();
			throw new FileResourceException("TransactionInterceptor rollback Exception:",e);
		}finally {
			manager.closeTransaction();
		}
	}

	public void setManager(TransactionManager manager) {
		this.manager = manager;
	}
}
