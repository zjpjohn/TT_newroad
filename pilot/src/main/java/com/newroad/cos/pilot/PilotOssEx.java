package com.newroad.cos.pilot;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author tangzj1
 * 
 */
public interface PilotOssEx {

	/**
	 * 使用用户名、密码登录到OSS服务器。
	 * 
	 * @param appID
	 *            在开发时获取的应用程序的唯一标识
	 * @param devID
	 *            在开发时获取的开发者的唯一标识
	 * @param devKey
	 *            在开发时获取的开发者的口令
	 * @param userName
	 *            在OSS服务器上注册的用户名
	 * @param password
	 *            在OSS服务器上注册的用户口令
	 * @param spec
	 *            使用OSS服务器的规格描述
	 * @param workspace
	 *            暂时未用到，传递null
	 * @return 操作成功返回true，否则返回false
	 */
	boolean login(String appID, String devID, String devKey, String userName,
			String password, String spec, String workspace)
			throws PilotException;

	/**
	 * 使用Lenovo ID登录到OSS服务器。
	 * 
	 * @param appID
	 *            在开发时获取的应用程序的唯一标识
	 * @param devID
	 *            在开发时获取的开发者的唯一标识
	 * @param devKey
	 *            在开发时获取的开发者的口令
	 * @param realm
	 *            登陆域
	 * @param lenovoST
	 *            用户已有的Lenovo ID
	 * @param spec
	 *            使用OSS服务器的规格描述
	 * @param workspace
	 *            暂时未用，传递null
	 * @return 操作成功返回true，否则返回false
	 */
	boolean loginByLenovoID(String appID, String devID, String devKey,
			String realm, String lenovoST, String spec, String workspace)
			throws PilotException;

	/**
	 * 在已经登陆的情况下获得登录认证标识。
	 * 
	 * @return 成功返回认证标识，失败则返null
	 */
	String getConnector();

	/**
	 * 使用已验证的Connector登录到OSS服务器。
	 * 
	 * @param devID
	 *            在开发时获取的开发者的唯一标识
	 * @param devKey
	 *            在开发时获取的开发者的口令
	 * @param connector
	 *            已获得的认证串
	 * @return 成功返回true，失败则返回false
	 */
	boolean loginByConnector(String devID, String devKey, String connector)
			throws PilotException;

	
	String generateToken(String appID,String devID, String devKey, String userSlug,long expiration)
			throws PilotException;
	
	void useToken(String token) throws IOException;
	
	/**
	 * 列出一个bucket中指定个数的object
	 * 
	 * @param bucketName
	 *            想要操作的bucket的name
	 * @param callback
	 *            填充object对象的回调函数
	 * @param offset
	 *            起始object的偏移量
	 * @param length
	 *            取回的Object信息的个数
	 * @return 成功返回Object的list，如果指定offset下一个object都没有则返回null
	 */
	List<PilotOssObjectBaseEx> listOssObject(String bucketName, long offset,
			long length) throws PilotException;

	/**
	 * 获取bucket的信息
	 * 
	 * @return 返回对象信息，JSON格式
	 */
	Map<String, Object> getBucketInfo(String url) throws PilotException;

	/**
	 * 在OSS服务器上创建一个对象
	 * 
	 * @param bucketName
	 *            OSS服务器上已有的bucket name
	 * @param keyID
	 *            对象的唯一标识，可以为null
	 * @param param
	 *            参数，对于为ParameterData类
	 * @return 返回PilotOssObjectBase接口
	 * @see PilotOssObjectBase
	 */
	PilotOssObjectBaseEx createObject(String bucketName, String keyID,
			Object param) throws PilotException;

	/**
	 * @param bucketName
	 * @param objNumber
	 * @param parameterData
	 * @return
	 * @throws PilotException
	 */
	PilotOssObjectListEx batchCreateObjects(String bucketName, int objNumber,
			Object parameterData) throws PilotException;

	/**
	 * 在OSS服务器上通过keyID获取一个已有对象
	 * 
	 * @param bucketName
	 *            OSS服务器上已有的bucket name
	 * @param keyID
	 *            OSS服务器上已有对象唯一标识
	 * @return 返回PilotOssObjectBase接口
	 * @see PilotOssObjectBase
	 */
	PilotOssObjectBaseEx getOssObject(String bucketName, String keyID)
			throws PilotException;

	/**
	 * 在OSS服务器上通过url获取一个已有对象
	 * 
	 * @param url
	 *            OSS服务器上已有对象的url
	 * @return 返回PilotOssObjectBaseEx接口
	 * @see PilotOssObjectBaseEx
	 */
	PilotOssObjectBaseEx getOssObject(String url) throws PilotException;

	/**
	 * @param bucketName
	 * @param keys
	 * @return
	 * @throws PilotException
	 */
	PilotOssObjectListEx getOssObjectList(String bucketName, String... keys)
			throws PilotException;

	boolean isLoginSuccessed();

}
