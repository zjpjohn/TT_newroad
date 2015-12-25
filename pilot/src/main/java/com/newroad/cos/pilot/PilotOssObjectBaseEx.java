package com.newroad.cos.pilot;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.newroad.cos.pilot.util.PilotOssConstants.ThumbnailGenerateType;

/**
 * 
 * 
 */
public interface PilotOssObjectBaseEx {
	/**
	 * 得到OSS服务器上对象的bucket ID
	 * @return 返回对象的bucket ID
	 */
	String getBucketName();
	
	/**
	 * 得到OSS服务器上对象的KeyID
	 * @return 返回对象的KeyID，有些对象可能不存在keyID，不存在keyID的对象返回null
	 */
	String getKeyID();
	
	/**
	 * 得到OSS服务器上对象的url
	 * @return 返回对象的url
	 */
	String getUrl();
	
	
	Map<String,String> getObjectInfoMap();
	
	/**
	 * 注册用户的通知接口，用户上传下载数据时通知进度
	 * @param listener 用户实现的PilotOssListenerEx接口
	 * @see PilotOssListenerEx
	 */
	void registerListener(PilotOssListenerEx listener);
	
	/**
	 * 取消用户注册的通知接口
	 * @param listener 用户实现的PilotOssListenerEx接口
	 * @see PilotOssListenerEx
	 */
	void unregisterListener(PilotOssListenerEx listener);
	
	/**
	 * 上传数据
	 * @param file 数据对象，用户应设置数据流起始位置
	 * @param contentType 数据的Mime Type
	 * @param objectOffset 上传数据对于OSS服务器上对象的相对位置
	 * @param size 上传数据的长度
	 * @param param 参数，对于为ParameterData类
	 * @param userData 用户数据，在通知用户上传进度时传递给用户
	 * @return 操作成功返回true，否则返回false
	 */
	boolean putObject(File uploadFile, String contentType, long objectOffset, long size, Object param, Object userData) throws PilotException;
	
	/**
	 * 上传数据
	 * @param stream 数据流，用户应设置数据流起始位置
	 * @param contentType 数据的Mime Type
	 * @param objectOffset 上传数据对于OSS服务器上对象的相对位置
	 * @param size 上传数据的长度
	 * @param param 参数，对于为ParameterData类
	 * @param userData 用户数据，在通知用户上传进度时传递给用户
	 * @return 操作成功返回true，否则返回false
	 */
	boolean putObject(InputStream stream, String contentType, long objectOffset, long size, Object param, Object userData) throws PilotException;
	/**
	 * 下载数据
	 * @param stream 用户传入的输出流，得到的数据写入此流
	 * @param objectOffset 下载数据对于OSS服务器上对象的相对位置
	 * @param size 下载数据的长度
	 * @param userData 用户数据，在通知用户下载进度时传递给用户
	 * @return 操作成功返回true，否则返回false
	 */
	boolean getObject(OutputStream stream, long objectOffset, long size, Object userData) throws PilotException;
	
	/**
	 * 停止当前Object的上传下载动作 
	 * @return 操作成功返回true，否则返回false
	 * @throws PilotException
	 */
	boolean stopTransfer() throws PilotException;
	
	/**
	 * 删除对象
	 * @param param 参数，对于为ParameterData类
	 * @return 操作成功返回true，否则返回false
	 */
	Map<String, String> deleteObject(Object param) throws PilotException;
	
	/**
	 * 获取对象的信息
	 * @return 返回对象信息，JSON格式
	 */
	Map<String, String> getObjectInfo() throws PilotException;
	
	/**
	 * 获取对象的缩略图
	 * @param width 缩略图的宽度
	 * @param height 缩略图的高度
	 * @return 返回缩略图数据
	 */
	public byte[] getObjectThumbnail(int width, int height, ThumbnailGenerateType thumbnailType, String displayName) throws PilotException;
	
	/**
	 * 获取文档指定页的缩略图
	 * @param width 缩略图的宽度
	 * @param height 缩略图的高度
	 * @param PageNumber 页码
	 * @return 返回缩略图数据
	 * @throws PilotException 
	 */
	byte[] getObjectDocThumbnail(int width, int height, int PageNumber) throws PilotException;
	
	/**
	 * 设置Thumbnail的方向
	 * @param orientation 应该是 0 90 180 270中的一个
	 * @throws PilotException
	 */
	void setThumbnailOrientation(int orientation) throws PilotException;
	
	/**
	 * 创建对象的公共链接
	 * @param validTime 链接有效时间，以分钟为单位
	 * @param accessLimit 链接访问的个数限制
	 * @param failedRedirect 访问出错后的重定向页面地址
	 * @return 返回创建的公共链接
	 * @throws PilotException
	 */
	String createObjectPublicLink(int validTime)throws PilotException;
	
	/**
	 * 删除对象的公共链接
	 * @param param 参数，对于为ParameterData类
	 * @return 操作成功返回true，否则返回false
	 */
	String deleteObjectPublicLink(Object param) throws PilotException;

}
