package com.newroad.fileext.dao.cos;

import java.util.List;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lenovo.pilot.PilotException;
import com.lenovo.pilot.PilotOssCloud;
import com.lenovo.pilot.PilotOssEx;
import com.lenovo.pilot.PilotOssObjectBaseEx;
import com.newroad.fileext.utilities.SystemProperties;
import com.newroad.util.exception.COSException;

/**
 * @Info COS Bucket data access api
 * @author tangzj1
 */
public class COSDao {

	private final Logger logger = LoggerFactory.getLogger(COSDao.class);

	private PilotOssEx connectByConnector(JSONObject sessionUser) {
		String sessionConnector = sessionUser.getString("cosConnector");
		logger.info("COSDao connect sessionConnector:" + sessionConnector);
		PilotOssEx cos = PilotOssCloud.CreateOssCloudEx();
		try {
			cos.loginByConnector(SystemProperties.devID,
					SystemProperties.devKey, sessionConnector);
			return cos;
		} catch (PilotException e) {
			logger.error("COS initial connection failure!", e);
			throw new COSException("COS initial connection failure!" + e);
		}
	}

	public String getCOSToken(String account, long expire) {
		PilotOssEx cos = PilotOssCloud.CreateOssCloudEx();
		try {
			return cos.generateToken(SystemProperties.appID,
					SystemProperties.devID, SystemProperties.devKey, account,
					expire);
		} catch (PilotException e) {
			logger.error("COS initial connection failure!", e);
		}
		return null;
	}
	
	public String getConnectorByUser(String user, String password) {
		PilotOssEx cos = PilotOssCloud.CreateOssCloudEx();
		try {
			if (user == null || password == null) {
				throw new COSException("No user or password connect COS!");
			}
			cos.login(SystemProperties.appID, SystemProperties.devID,
					SystemProperties.devKey, user,
					password, SystemProperties.spec,
					SystemProperties.workspace);
			return cos.getConnector();
		} catch (PilotException e) {
			logger.error("COS initial connection failure!", e);
		}
		return null;
	}

	public String getConnectorByLenovoID(String realm, String lenovoST) {
		PilotOssEx cos = PilotOssCloud.CreateOssCloudEx();
		try {
			if (realm == null || lenovoST == null) {
				throw new COSException("No lenovoST or realm connect COS!");
			}
			cos.loginByLenovoID(SystemProperties.appID, SystemProperties.devID,
					SystemProperties.devKey, realm, lenovoST,
					SystemProperties.spec, SystemProperties.workspace);
			return cos.getConnector();
		} catch (PilotException e) {
			logger.error("COS initial connection failure!", e);
		}
		return null;
	}

	public int listObjectNumber(JSONObject sessionUser) {
		int number = 0;
		PilotOssEx cos = connectByConnector(sessionUser);
		try {
			List<PilotOssObjectBaseEx> objectList = cos.listOssObject(
					SystemProperties.bucketName, 0, 10);
			if (objectList != null) {
				number = objectList.size();
			}
			return number;
		} catch (PilotException e) {
			logger.error("COSDao.listObjects Exception!", e);
			throw new COSException("COSDao.listObjects Exception!" + e);
		}
	}

	public List<PilotOssObjectBaseEx> listOssObject(JSONObject sessionUser,
			long offset, long length) throws PilotException {
		List<PilotOssObjectBaseEx> list = null;
		PilotOssEx cos = connectByConnector(sessionUser);
		list = cos.listOssObject(SystemProperties.bucketName, offset, length);
		return list;
	}

}
