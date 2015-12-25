package com.newroad.cos.pilot;

/**
 * 
 * 用户实现PilotOssListener，传递给PilotOssObjectBase，用于进度通知
 *
 */
public interface PilotOssObjectListCallback {
	/**
	 * @param object: the object filled to callback
	 */
	boolean onObject(PilotOssObjectBaseEx object);
}
