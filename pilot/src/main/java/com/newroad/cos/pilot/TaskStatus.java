package com.newroad.cos.pilot;

/**
 * 
 * 任务的状态
 *
 */
public enum TaskStatus{
	/**
	 * 任务启动
	 */
	TASK_START,
	/**
	 * 任务中断
	 */
	TASK_STOP,
	/**
	 * 任务正在执行
	 */
	TASK_RUNNING,
	/**
	 * 任务已经完成
	 */
	TASK_COMPLETED,
	/**
	 * 任务出现错误
	 */
	TASK_Error
}