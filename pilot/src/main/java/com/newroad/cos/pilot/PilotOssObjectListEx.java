package com.newroad.cos.pilot;

import java.util.List;
import java.util.Map;

import com.newroad.cos.pilot.util.ResourceData;
/**
 * @info  用于传递Object List
 * @author tangzj1
 *
 */
public interface PilotOssObjectListEx extends PilotOssObjectBaseEx {

	/**
	 * 得到OSS服务器上对象的KeyIDs
	 * @return 返回对象的KeyIDs，有些对象可能不存在keyID，不存在keyID的对象返回null
	 */
	String[] getKeyIDs();
	
	/**
	 * @param resourceList
	 * @param batchFileSize
	 * @param parameterData
	 * @param userData
	 * @return
	 * @throws PilotException
	 */
	String batchPutObjectList(List<ResourceData> resourceList, long batchFileSize,
			Object parameterData, Object userData) throws PilotException;
	
	/**
	 * @param batchFileSize
	 * @param outputFolderPath
	 * @param parameterData
	 * @param userData
	 * @return
	 * @throws PilotException
	 */
	List<Map<String, Object>> batchGetObjectList(long batchFileSize,String outputFolderPath,
			Object parameterData, Object userData) throws PilotException;

	/**
	 * @param validTime
	 * @return
	 * @throws PilotException
	 */
	String batchCreateObjectPublicLink(int validTime) throws PilotException;
}
