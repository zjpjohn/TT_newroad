package com.newroad.cos.pilot;

/**
 * 
 * 用户实现PilotOssListener，传递给PilotOssObjectBase，用于进度通知
 *
 */
public interface PilotOssListenerEx {
	/**
	 * 传递任务进度信息及状态
	 * @param status 上传或下载的状态
	 * @param completed 已完成的字节数
	 * @param total 任务总共字节数
	 * @param userData 用户数据
	 * @param errCode 错误代码，当status为TASK_Error时有效
	 * @return 返回false上传或下载未完成，否则传递true
	 * @see TaskStatus
	 */
	boolean onProgress(TaskStatus status, long completed, long total, Object userData, int errCode);
	
	/** 
	 * 开始网络传输时调用
	 * @param status 上传或下载的状态
	 * @param userData 用户数据
	 */
	void onStart(Object userData);

	/**
	 * 任务出现异常停止时调用
	 * @param completed 已完成的字节数
	 * @param total 任务总共字节数
	 * @param userData 用户数据
	 * @param errCode 错误代码
	 */
	void onStop(long completed, long total, Object userData, int errCode);

	/**
	 * 任务正常完成退出时调用
	 * @param completed 已完成的字节数
	 * @param total 任务总共字节数
	 * @param userData 用户数据
	 */
	void onFinished(long completed, long total, Object userData);
}
