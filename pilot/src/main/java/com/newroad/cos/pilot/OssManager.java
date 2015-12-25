package com.newroad.cos.pilot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.cos.pilot.HttpHelper.RequestAndResponse;
import com.newroad.cos.pilot.util.CallbackData;
import com.newroad.cos.pilot.util.PilotOssConstants;

public class OssManager extends OssManagerBase {
	private final Logger logger = LoggerFactory.getLogger(OssManager.class);

	public Map<String, String> createObject(String bucketName, String keyName,
			CallbackData callbackData) throws IOException {
		String token = getAuthorization();
		String url = null;
		String encodedata = null;

		if (keyName == null) {
			url = PilotOssHelper.buildBucketUrl(bucketName);
		} else {
			encodedata = URLEncoder.encode(keyName, "utf-8");
			url = PilotOssHelper.buildBucketUrl(bucketName) + "?object_key="
					+ encodedata;
		}
		return createObj(url, token, callbackData);
	}

	public JSONArray ListObjects(String bucketName,
			Map<String, String> queryParam) throws IOException {
		String token = getAuthorization();
		String url = PilotOssHelper.buildBucketUrl(bucketName);
		String objectUrl = assembleUrl(url, queryParam);
		logger.debug("COS ListObjects URL:" + objectUrl);
		return ListObjects(objectUrl, token);
	}

	public Map<String, String> deleteObject(String objectUrl,
			CallbackData callbackData) throws IOException {
		String token = getAuthorization();
		Map<String, String> queryParam = null;
		if (callbackData != null) {
			queryParam = callbackData.getData();
		}
		String url = assembleUrl(
				PilotOssHelper.insertContents(objectUrl, "object", "/metadata"),
				queryParam);
		logger.debug("COS deleteObject URL:" + url);
		return delObject(url, token, callbackData);
	}

	public Map<String, String> deleteObject(String bucketName, String keyName,
			CallbackData callbackData) throws IOException {
		String token = getAuthorization();
		Map<String, String> queryParam = null;
		if (callbackData != null) {
			queryParam = callbackData.getData();
		}
		String url = assembleUrl(
				PilotOssHelper.buildObjUrl(bucketName, null, keyName),
				queryParam);
		logger.debug("COS deleteObject URL:" + url);
		return delObject(url, token, callbackData);
	}

	public List<String> batchCreateObjects(String bucketName,
			Map<String, String> queryParam, CallbackData callbackData)
			throws IOException {
		String token = getAuthorization();
		String url = assembleUrl(
				PilotOssHelper.buildBucketUrl(bucketName, "ops/batch-create"),
				queryParam);
		logger.debug("COS batchCreateObjects URL:" + url);
		return batchCreateObjects(url, token, callbackData);
	}

	public String createObjectPublicLink(String objectUrl,
			Map<String, String> queryParam, CallbackData callbackData)
			throws IOException {
		String token = getAuthorization();
		String url = assembleUrl(
				PilotOssHelper.insertContents(objectUrl, "object", "/link"),
				queryParam);
		logger.debug("COS createObjectPublicLink URL:" + url);
		return makeObjectPublicLink(url, token, callbackData);
	}

	public String createObjectPublicLink(String bucketName, String keyName,
			Map<String, String> queryParam, CallbackData callbackData)
			throws IOException {
		String token = getAuthorization();
		String url = assembleUrl(
				PilotOssHelper.buildObjUrl(bucketName, "link", keyName),
				queryParam);
		logger.debug("COS createObjectPublicLink URL:" + url);
		return makeObjectPublicLink(url, token, callbackData);
	}

	public String batchCreateObjectPublicLink(String bucketName,
			InputStream is, long size, Map<String, String> queryParam,
			CallbackData callbackData) throws IOException {
		String token = getAuthorization();
		String url = assembleUrl(
				PilotOssHelper.buildObjUrl(bucketName, "link/batch-create"),
				queryParam);
		logger.debug("COS batchCreateObjectPublicLink URL:" + url);
		return batchMakeObjectPublicLink(url, token, is, size, callbackData);
	}

	public String deleteObjectPublicLink(String objectUrl,
			CallbackData callbackData) throws IOException {
		String token = getAuthorization();
		Map<String, String> queryParam = null;
		if (callbackData != null) {
			queryParam = callbackData.getData();
		}
		String url = assembleUrl(
				PilotOssHelper.insertContents(objectUrl, "object", "/link"),
				queryParam);
		logger.debug("COS deleteObjectPublicLink URL:" + url);
		return delObjectPublicLink(url, token, callbackData);
	}

	public String deleteObjectPublicLink(String bucketName, String keyName,
			CallbackData callbackData) throws IOException {
		String token = getAuthorization();
		Map<String, String> queryParam = null;
		if (callbackData != null) {
			queryParam = callbackData.getData();
		}
		String url = assembleUrl(
				PilotOssHelper.buildObjUrl(bucketName, "link", keyName),
				queryParam);
		logger.debug("COS deleteObjectPublicLink URL:" + url);
		return delObjectPublicLink(url, token, callbackData);
	}

	public Map<String, String> getObjectInfo(String objectUrl)
			throws IOException {
		String token = getAuthorization();
		return getObjInfo(
				PilotOssHelper.insertContents(objectUrl, "object", "/metadata"),
				token);
	}

	public Map<String, String> getObjectInfo(String bucketName, String keyName)
			throws IOException {
		String token = getAuthorization();
		String url = PilotOssHelper
				.buildObjUrl(bucketName, "metadata", keyName);
		logger.debug("COS getObjectInfo URL:" + url);
		return getObjInfo(url, token);
	}

	private Map<String, String> createObj(String bucketUrl, String token,
			CallbackData callbackData) throws IOException {
		String url = bucketUrl;
		String authtoken = token;
		Map<String, String> sendHeaders = new HashMap<String, String>();

		logger.debug("COS createObject URL:" + url);

		//String urlcallback = "http://createObject";
		sendHeaders.put("X-Lenovows-Authorization", authtoken);
		//sendHeaders.put("X-Lenovows-OSS-Callback", urlcallback);

		HttpHelper hhp = new HttpHelper();
		RequestAndResponse rr = null;
		rr = hhp.performPost(url, null, null, sendHeaders, null, null, 0, null,
				null);

		byte[] body = handlerResponseBody(hhp, rr, HttpHelper.POST_METHOD, url,
				sendHeaders, null, 0, null, null);
		if (body == null) {
			throw new IOException("http body is null");
		}

		String json = new String(body);
		Map<String, String> object = new HashMap<String, String>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			object.put("key", jsonObject.getString("key"));
			object.put("location", jsonObject.getString("location"));
			return object;
		} catch (JSONException jse) {
			logger.error("JSON Exception:", jse);
		}
		return null;
	}

	private List<String> batchCreateObjects(String url, String token,
			CallbackData callbackData) throws IOException {
		//String urlcallback = "http://batchCreateObjects";
		String authtoken = token;

		Map<String, String> sendHeaders = new HashMap<String, String>();
		sendHeaders.put("X-Lenovows-Authorization", authtoken);
		//sendHeaders.put("X-Lenovows-OSS-Callback", urlcallback);

		// perform http request
		HttpHelper hhp = new HttpHelper();
		RequestAndResponse rr = null;
		rr = hhp.performPost(url, null, null, sendHeaders, null, null, 0, null,
				null);

		byte[] body = handlerResponseBody(hhp, rr, HttpHelper.POST_METHOD, url,
				sendHeaders, null, 0, null, null);
		if (body == null) {
			throw new IOException("http body is null");
		}

		String json = new String(body);
		List<String> objectMaps = new ArrayList<String>();
		try {
			JSONArray jsonArray = new JSONArray(json);
			int number = jsonArray.length();
			JSONObject jsonObject = null;
			for (int i = 0; i < number; i++) {
				jsonObject = jsonArray.getJSONObject(i);
				String keyID = jsonObject.getString("key");
				objectMaps.add(keyID);
			}
			return objectMaps;
		} catch (JSONException jse) {
			logger.error("JSON Exception:", jse);
		}
		return null;
	}

	private JSONArray ListObjects(String url, String token) throws IOException {
		String authtoken = token;
		Map<String, String> sendHeaders = new HashMap<String, String>();
		sendHeaders.put("X-Lenovows-Authorization", authtoken);

		HttpHelper hhp = new HttpHelper();
		RequestAndResponse rr = null;
		rr = hhp.performGet(url, null, null, sendHeaders);

		byte[] body = handlerResponseBody(hhp, rr, HttpHelper.GET_METHOD, url,
				sendHeaders, null, 0, null, null);
		if (body == null) {
			throw new IOException("http body is null");
		}

		String json = new String(body);
		try {
			return new JSONArray(json);
		} catch (JSONException e) {
			logger.error("JSONException:" + e);
		}
		return null;
	}

	private Map<String, String> delObject(String url, String token,
			CallbackData callbackData) throws IOException {
		String authtoken = token;
		Map<String, String> sendHeaders = new HashMap<String, String>();

		//String urlcallback = "http://deleteObject";

		sendHeaders.put("X-Lenovows-Authorization", authtoken);
		//sendHeaders.put("X-Lenovows-OSS-Callback", urlcallback);
		HttpHelper hhp = new HttpHelper();
		RequestAndResponse rr = null;
		rr = hhp.performDelete(url, null, null, sendHeaders, null);

		byte[] body = handlerResponseBody(hhp, rr, HttpHelper.DELETE_METHOD,
				url, sendHeaders, null, 0, null, null);
		if (body == null) {
			throw new IOException("http body is null");
		}

		String json = new String(body);
		Map<String, String> deleteInfo = new HashMap<String, String>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			Iterator<?> it = jsonObject.keys();
			String key = null;
			String value = null;
			while (it.hasNext()) {
				key = (String) it.next().toString();
				value = jsonObject.getString(key);
				deleteInfo.put(key, value);
			}
			return deleteInfo;
		} catch (JSONException jse) {
			logger.error("JSONException:" + jse);
		}
		return null;
	}

	private Map<String, String> getObjInfo(String url, String token)
			throws IOException {
		Map<String, String> objectInfo = null;

		// houwei modify for difference of iocos and cos
		// int index = url.indexOf("/object/metadata/");
		// String newurl = URL_BASE + url.substring(index);
		// url = newurl;

		String authtoken = token;
		Map<String, String> sendHeaders = new HashMap<String, String>();
		sendHeaders.put("X-Lenovows-Authorization", authtoken);

		HttpHelper hhp = new HttpHelper();
		RequestAndResponse rr = null;
		rr = hhp.performGet(url, null, null, sendHeaders);
		logger.info("getObjInfo using URL:" + url + ",token:" + authtoken);

		byte[] body = handlerResponseBody(hhp, rr, HttpHelper.GET_METHOD, url,
				sendHeaders, null, 0, null, null);
		if (body == null) {
			throw new IOException("http body is null");
		}

		String json = new String(body);
		objectInfo = new HashMap<String, String>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			Iterator<?> it = jsonObject.keys();
			String key = null;
			// String value = null;
			while (it.hasNext()) {
				key = (String) it.next().toString();
				Object valueObj = jsonObject.get(key);
				objectInfo.put(key, valueObj.toString());
			}
			return objectInfo;
		} catch (JSONException jse) {
			logger.error("JSONException:" + jse);
		}
		return null;
	}

	private String makeObjectPublicLink(String url, String token,
			CallbackData callbackData) throws IOException {
		String link_url = null;
		//String urlcallback = "http://makeObjectPublicLink";
		String authtoken = token;

		Map<String, String> sendHeaders = new HashMap<String, String>();
		sendHeaders.put("X-Lenovows-Authorization", authtoken);
		//sendHeaders.put("X-Lenovows-OSS-Callback", urlcallback);

		// perform http request
		HttpHelper hhp = new HttpHelper();
		RequestAndResponse rr = null;
		rr = hhp.performPost(url, null, null, sendHeaders, null, null, 0, null,
				null);

		byte[] body = handlerResponseBody(hhp, rr, HttpHelper.POST_METHOD, url,
				sendHeaders, null, 0, null, null);
		if (body == null) {
			throw new IOException("http body is null");
		}

		String json = new String(body);
		try {
			JSONObject jsonObject = new JSONObject(json);
			link_url = jsonObject.getString("link_url");
			return link_url;
		} catch (JSONException jse) {
			logger.error("JSONException:" + jse);
		}
		return null;
	}

	private String delObjectPublicLink(String url, String token,
			CallbackData callbackData) throws IOException {
		String id = null;
		//String urlcallback = "http://deleteObjectPublicLink";

		String authtoken = token;
		Map<String, String> sendHeaders = new HashMap<String, String>();
		sendHeaders.put("X-Lenovows-Authorization", authtoken);
		//sendHeaders.put("X-Lenovows-OSS-Callback", urlcallback);

		HttpHelper hhp = new HttpHelper();
		RequestAndResponse rr = null;
		rr = hhp.performDelete(url, null, null, sendHeaders, null);

		byte[] body = handlerResponseBody(hhp, rr, HttpHelper.DELETE_METHOD,
				url, sendHeaders, null, 0, null, null);
		if (body == null) {
			throw new IOException("http body is null");
		}

		String json = new String(body);
		try {
			JSONObject jsonObject = new JSONObject(json);
			id = jsonObject.getString("link_url");
			return id;
		} catch (JSONException jse) {
			logger.error("JSONException:", jse);
		}
		return null;
	}

	private String batchMakeObjectPublicLink(String url, String token,
			InputStream input, long size, CallbackData callbackData)
			throws IOException {
		//String urlcallback = "http://batchMakeObjectPublicLink";
		String authtoken = token;

		Map<String, String> sendHeaders = new HashMap<String, String>();
		sendHeaders.put("X-Lenovows-Authorization", authtoken);
		//sendHeaders.put("X-Lenovows-OSS-Callback", urlcallback);

		// perform http request
		HttpHelper hhp = new HttpHelper();
		RequestAndResponse rr = null;
		rr = hhp.performPost(url, null, null, sendHeaders, null, input, size,
				null, null);

		byte[] body = handlerResponseBody(hhp, rr, HttpHelper.POST_METHOD, url,
				sendHeaders, null, 0, null, null);
		if (body == null) {
			throw new IOException("http body is null");
		}

		String publicLinks = new String(body);
		return publicLinks;
	}

	public void readObject(String bucketName, String keyName,
			OutputStream output, long size, Map<String, String> queryParam,
			OssManagerListener listener, Object progressData)
			throws IOException {
		String token = getAuthorization();
		String url = assembleUrl(
				PilotOssHelper.buildObjIOUrl(bucketName, null, keyName),
				queryParam);
		logger.debug("COS readObject token:" + token + ", URL:" + url);
		getObject(url, token, output, size, listener, progressData);
	}

	public void readObject(String objectUrl, OutputStream output, long size,
			Map<String, String> queryParam, OssManagerListener listener,
			Object progressData) throws IOException {
		String token = getAuthorization();
		String url = assembleUrl(objectUrl, queryParam);
		logger.debug("COS readObject token:" + token + ", URL:" + url);
		getObject(url, token, output, size, listener, progressData);
	}

	public void writeObject(String bucketName, String keyName,
			String contentType, InputStream input, long size,
			OssManagerListener listener, CallbackData callbackData,
			Object progressData) throws IOException {
		String token = getAuthorization();
		Map<String, String> queryParam = null;
		if (callbackData != null) {
			queryParam = callbackData.getData();
		}
		String url = assembleUrl(
				PilotOssHelper.buildObjIOUrl(bucketName, null, keyName),
				queryParam);
		logger.debug("COS writeObject token:" + token + ", URL:" + url);
		putObject(url, contentType, token, input, size, listener, callbackData,
				progressData);
	}

	public void writeObject(String objectUrl, String contentType,
			InputStream input, long size, OssManagerListener listener,
			CallbackData callbackData, Object progressData) throws IOException {
		String token = getAuthorization();
		Map<String, String> queryParam = null;
		if (callbackData != null) {
			queryParam = callbackData.getData();
		}
		String url = assembleUrl(objectUrl, queryParam);
		logger.debug("COS writeObject token:" + token + ", URL:" + url);
		putObject(url, contentType, token, input, size, listener, callbackData,
				progressData);
	}

	public void writeObjectByChunk(String bucketName, String keyName,
			String contentType, InputStream input, long size, long offset,
			OssManagerListener listener, CallbackData callbackData,
			Object progressData) throws IOException {
		String token = getAuthorization();
		Map<String, String> queryParam = null;

		if (callbackData != null) {
			queryParam = callbackData.getData();
		}
		String url = assembleUrl(
				PilotOssHelper.buildObjIOUrl(bucketName, null, keyName),
				queryParam);
		// if (size > 0){
		// buf = new byte[size];
		// System.arraycopy(data, 0, buf, 0, size);
		// }else {
		// buf = null;
		// }
		logger.debug("COS writeObjectByChunk token:" + token + ", URL:" + url);
		putObjectByChunk(url, contentType, token, input, size, offset,
				listener, callbackData, progressData);
	}

	public void writeObjectByChunk(String objectUrl, String contentType,
			InputStream input, long size, long offset,
			OssManagerListener listener, CallbackData callbackData,
			Object progressData) throws IOException {
		String token = getAuthorization();
		Map<String, String> queryParam = null;

		if (callbackData != null) {
			queryParam = callbackData.getData();
		}
		String url = assembleUrl(objectUrl, queryParam);
		// if (size > 0){
		// buf = new byte[size];
		// System.arraycopy(data, 0, buf, 0, size);
		// }else {
		// buf = null;
		// }
		logger.debug("COS writeObjectByChunk token:" + token + ", URL:" + url);
		putObjectByChunk(url, contentType, token, input, size, offset,
				listener, callbackData, progressData);
	}

	public void commitObjectByChunk(String bucketName, String keyName,
			String contentType, CallbackData callbackData) throws IOException {
		String token = getAuthorization();
		Map<String, String> queryParam = null;
		if (callbackData != null) {
			queryParam = callbackData.getData();
		}
		String url = assembleUrl(
				PilotOssHelper.buildObjIOUrl(bucketName, null, keyName),
				queryParam);
		logger.debug("COS commitObjectByChunk token:" + token + ", URL:" + url);
		commitObject(url, contentType, token, callbackData);
	}

	public void commitObjectByChunk(String objectUrl, String contentType,
			CallbackData callbackData) throws IOException {
		String token = getAuthorization();
		Map<String, String> queryParam = null;
		if (callbackData != null) {
			queryParam = callbackData.getData();
		}
		String url = assembleUrl(objectUrl, queryParam);
		logger.debug("COS commitObjectByChunk token:" + token + ", URL:" + url);
		commitObject(url, contentType, token, callbackData);
	}

	public void readObjectByChunk(String objectUrl, OutputStream output,
			long offset, long size, Map<String, String> queryParam,
			OssManagerListener listener, Object progressData)
			throws IOException {
		String token = getAuthorization();
		String url = assembleUrl(objectUrl, queryParam);
		logger.debug("COS readObjectByChunk token:" + token + ", URL:" + url);
		getObjectByChunk(url, token, output, offset, size, listener,
				progressData);
	}

	public void readObjectByChunk(String bucketName, String keyName,
			OutputStream output, long offset, long size,
			Map<String, String> queryParam, OssManagerListener listener,
			Object progressData) throws IOException {
		String token = getAuthorization();
		String url = assembleUrl(
				PilotOssHelper.buildObjIOUrl(bucketName, null, keyName),
				queryParam);
		logger.debug("COS readObjectByChunk token:" + token + ", URL:" + url);
		getObjectByChunk(url, token, output, offset, size, listener,
				progressData);
	}

	public String batchPutObjectList(String bucketName, InputStream is,
			long fileSize, OssManagerListener listener,
			Map<String, String> queryParam, CallbackData callbackData,
			Object progressData) throws IOException {
		String token = getAuthorization();
		String url = assembleUrl(
				PilotOssHelper.buildObjIOUrl(bucketName, "ops/batch-upload"),
				queryParam);
		logger.debug("COS batchPutObjectList URL:" + url);
		return batchPutObjectList(url, token, is, fileSize, listener,
				callbackData, progressData);
	}

	public List<Map<String, Object>> batchGetObjectList(String bucketName,
			InputStream is, long length, long fileSize,
			OssManagerListener listener, Map<String, String> queryParam,
			CallbackData callbackData, Object progressData) throws IOException {
		String token = getAuthorization();
		String url = assembleUrl(
				PilotOssHelper.buildObjIOUrl(bucketName, "ops/batch-download"),
				queryParam);
		logger.debug("COS batchGetObjectList URL:" + url);
		return batchGetObjectList(url, token, is, length, fileSize, listener,
				callbackData, progressData);
	}

	public byte[] getObjectThumbnail(String objectUrl,
			Map<String, String> queryParam) throws IOException {
		String token = getAuthorization();
		String url = assembleUrl(
				PilotOssHelper.insertContents(objectUrl, "object", "/thumb"),
				queryParam);
		logger.debug("COS getObjectThumbnail URL:" + url);
		return getObjThumbnail(url, token);
	}

	public byte[] getObjectThumbnail(String bucketName, String keyName,
			Map<String, String> queryParam) throws IOException {
		String token = getAuthorization();
		String url = assembleUrl(
				PilotOssHelper.buildObjIOUrl(bucketName, "thumb", keyName),
				queryParam);
		logger.debug("COS getObjectThumbnail URL:" + url);
		return getObjThumbnail(url, token);

	}

	public byte[] getObjectDocThumbnail(String objectUrl,
			Map<String, String> queryParam) throws IOException {
		String token = getAuthorization();
		String url = assembleUrl(
				PilotOssHelper.insertContents(objectUrl, "object", "/preview"),
				queryParam);
		logger.debug("COS getObjectDocThumbnail URL:" + url);
		return getObjDocThumbnail(url, token);
	}

	public void getObjectProgress(String bucketName, String keyName,
			String partNumber, CallbackData callbackData) throws IOException {

		String token = getAuthorization();
		Map<String, String> queryParam = null;
		if (callbackData != null) {
			CallbackData cbdata = (CallbackData) callbackData;
			queryParam = cbdata.getData();
		}
		String url = assembleUrl(
				PilotOssHelper.buildObjIOUrl(bucketName, null, keyName),
				queryParam);
		logger.debug("COS getObjectProgress URL:" + url);
		getProgress(url, partNumber, token, callbackData);
	}

	public void getObjectProgress(String objectUrl, String partNumber,
			CallbackData callbackData) throws IOException {
		String token = getAuthorization();
		Map<String, String> queryParam = null;
		if (callbackData != null) {
			CallbackData cbdata = (CallbackData) callbackData;
			queryParam = cbdata.getData();
		}
		String url = assembleUrl(objectUrl, queryParam);
		logger.debug("COS getObjectProgress URL:" + url);
		getProgress(url, partNumber, token, callbackData);
	}

	public void setThumbnailOrientation(String objectUrl,
			CallbackData callbackData) throws IOException {
		String token = getAuthorization();
		Map<String, String> queryParam = null;
		if (callbackData != null) {
			CallbackData cbdata = (CallbackData) callbackData;
			queryParam = cbdata.getData();
		}
		String objUrl = PilotOssHelper.insertContents(objectUrl, "object",
				"/thumb/ops/change-orientation");
		String url = assembleUrl(objUrl, queryParam);
		logger.debug("COS setThumbnailOrientation URL:" + url);
		setOrientation(url, token, callbackData);
	}

	public void setThumbnailOrientation(String bucketName, String keyName,
			CallbackData callbackData) throws IOException {
		String token = getAuthorization();
		Map<String, String> queryParam = null;
		if (callbackData != null) {
			CallbackData cbdata = (CallbackData) callbackData;
			queryParam = cbdata.getData();
		}
		String url = assembleUrl(PilotOssHelper.buildObjIOUrl(bucketName,
				"thumb/ops/change-orientation", keyName), queryParam);
		logger.debug("COS setThumbnailOrientation URL:" + url);
		setOrientation(url, token, callbackData);
	}

	protected Map<String, String> getProgress(String objectUrl,
			String partNumber, String token, CallbackData callbackData)
			throws IOException {
		String url = objectUrl;
		String authtoken = token;
		Map<String, String> sendHeaders = new HashMap<String, String>();
		sendHeaders.put("X-Lenovows-Authorization", authtoken);
		if (partNumber != null) {
			sendHeaders.put("X-Lenovows-Part-Number", partNumber);
		}

		HttpHelper hhp = new HttpHelper();
		RequestAndResponse rr = null;
		rr = hhp.performGet(url, null, null, sendHeaders);

		byte[] body = handlerResponseBody(hhp, rr);
		if (body == null) {
			throw new IOException("http body is null");
		}

		String json = new String(body);
		Map<String, String> object = new HashMap<String, String>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			Iterator<?> it = jsonObject.keys();
			String key = null;
			String value = null;
			while (it.hasNext()) {
				key = (String) it.next().toString();
				value = jsonObject.getString(key);
				object.put(key, value);
			}
			return object;
		} catch (JSONException jse) {
			logger.error("JSONException:" + jse);
		}
		return null;
	}

	private byte[] getObjDocThumbnail(String url, String token)
			throws IOException {
		// add query string here
		// url += "?scale=" + scale + "&page_number=" + PageNumber;
		Map<String, String> sendHeaders = new HashMap<String, String>();
		sendHeaders.put("X-Lenovows-Authorization", token);

		HttpHelper hhp = new HttpHelper();
		RequestAndResponse rr = null;
		rr = hhp.performGet(url, null, null, sendHeaders);
		byte[] body = handlerResponseBody(hhp, rr, HttpHelper.GET_METHOD, url,
				sendHeaders, null, 0, null, null);
		return body;
	}

	private byte[] getObjThumbnail(String url, String token) throws IOException {
		String authtoken = token;
		Map<String, String> sendHeaders = new HashMap<String, String>();
		sendHeaders.put("X-Lenovows-Authorization", authtoken);

		HttpHelper hhp = new HttpHelper();
		RequestAndResponse rr = null;
		rr = hhp.performGet(url, null, null, sendHeaders);
		byte[] body = handlerResponseBody(hhp, rr, HttpHelper.GET_METHOD, url,
				sendHeaders, null, 0, null, null);
		return body;
	}

	private void setOrientation(String url, String token,
			CallbackData callbackData) throws IOException {
		String authtoken = token;
		Map<String, String> sendHeaders = new HashMap<String, String>();
		sendHeaders.put("X-Lenovows-Authorization", authtoken);

		HttpHelper hhp = new HttpHelper();
		RequestAndResponse rr = null;
		rr = hhp.performPost(url, null, null, sendHeaders, null, null, 0, null,
				null);

		byte[] body = handlerResponseBody(hhp, rr, HttpHelper.POST_METHOD, url,
				sendHeaders, null, 0, null, null);
		if (body == null) {
			throw new IOException("http body is null");
		}

		String json = new String(body);
		Map<String, String> object = new HashMap<String, String>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			Iterator<?> it = jsonObject.keys();
			String key = null;
			String value = null;
			while (it.hasNext()) {
				key = (String) it.next().toString();
				value = jsonObject.getString(key);
				object.put(key, value);
			}
		} catch (JSONException jse) {
			logger.error("JSONException:" + jse);
		}
	}

	private void getObject(String url, String token, OutputStream output,
			long size, OssManagerListener listener, Object progressData)
			throws IOException {
		String authtoken = token;
		Map<String, String> sendHeaders = new HashMap<String, String>();
		sendHeaders.put("X-Lenovows-Authorization", authtoken);

		HttpHelper hhp = new HttpHelper();
		RequestAndResponse rr = null;
		rr = hhp.performGet(url, null, null, sendHeaders);

		try {
			rr = retryCheckResponseStatus(HttpHelper.GET_METHOD, hhp, rr, url,
					sendHeaders, null, 0, null, null);
			HttpEntity entity = rr.response.getEntity();
			if (entity != null) {
				InputStream is = entity.getContent();
				try {
					writeStreamToOutput(is, output, size, listener,
							progressData);
				} finally {
					EntityUtils.consume(entity);
					is.close();
				}
			}
			rr.request.releaseConnection();
		} catch (IOException ioe) {
			if (rr != null)
				rr.request.abort();
			throw ioe;
		} finally {
			hhp.closeConnections();
		}
	}

	private void getObjectByChunk(String url, String token,
			OutputStream output, long offset, long size,
			OssManagerListener listener, Object progressData)
			throws IOException {
		String authtoken = token;

		Map<String, String> sendHeaders = new HashMap<String, String>();
		sendHeaders.put("X-Lenovows-Authorization", authtoken);
		String start = Long.toString(offset);
		String end = Long.toString(offset + size - 1);
		String range = start + "-" + end;
		sendHeaders.put("X-Lenovows-Range", range);
		// sendHeaders.put("Content-Type", contentType);

		HttpHelper hhp = new HttpHelper();
		RequestAndResponse rr = null;
		rr = hhp.performGet(url, null, null, sendHeaders);

		try {
			rr = retryCheckResponseStatus(HttpHelper.GET_METHOD, hhp, rr, url,
					sendHeaders, null, 0, null, null);
			HttpEntity entity = rr.response.getEntity();
			if (entity != null) {
				InputStream is = entity.getContent();
				try {
					writeStreamToOutput(is, output, size, listener,
							progressData);
				} finally {
					EntityUtils.consume(entity);
					is.close();
				}
			}
			rr.request.releaseConnection();
		} catch (IOException ioe) {
			if (rr != null)
				rr.request.abort();
			throw ioe;
		} finally {
			hhp.closeConnections();
		}
	}

	private void writeStreamToOutput(InputStream is, OutputStream os,
			long length, OssManagerListener listener, Object progressData)
			throws IOException {
		long totalRead = 0;
		long lastListened = 0;
		boolean status = true;
		byte[] buffer = new byte[4096];
		int read;
		while (true) {
			read = is.read(buffer);
			if (read < 0) {
				if (length >= 0 && totalRead < length) {
					// We've reached the end of the file, but it's
					// unexpected.
					throw new IOException("Reach the end of file, readByte:"
							+ totalRead + "/fileSize:" + length);
				}
				if (length >= 0 && totalRead == length) {
					status = listener.onProgress(totalRead, length,
							progressData);
					// progress status should be false because the data write
					// stream is finished.
					if (status) {
						throw new HttpAbortException(
								"Writing File should be completed!");
					}
				}
				// TODO check for partial success, if possible
				break;
			}
			if ((totalRead + read) > length) {
				read = (int) (length - totalRead);
			}
			os.write(buffer, 0, read);
			totalRead += read;

			if (listener != null) {
				long now = System.currentTimeMillis();
				if (now - lastListened > listener.getProgressInterval()) {
					lastListened = now;
					status = listener.onProgress(totalRead, length,
							progressData);
					// progress status should be true because the data write
					// stream is in progress.
					if (!status && totalRead < length) {
						throw new HttpAbortException();
					}

				}
			}
		}
		// Make sure it's flushed out to disk
	}

	private void putObject(String url, String contentType, String token,
			InputStream input, long size, OssManagerListener listener,
			CallbackData callbackData, Object progressData) throws IOException {
		String authtoken = token;

		Map<String, String> sendHeaders = new HashMap<String, String>();
		sendHeaders.put("X-Lenovows-Authorization", authtoken);
		sendHeaders.put("Content-Type", contentType);
		if (callbackData != null) {
			Map<String, String> entry = callbackData.getHeaderData();
			if (entry != null) {
				String range = entry.get("range");
				String partNumber = entry.get("partNumber");

				if (range != null) {
					sendHeaders.put("X-Lenovows-Range", range);
					if (partNumber == null) {
						return;
					}
					sendHeaders.put("X-Lenovows-Part-Number", partNumber);
				}
			}
			String callbackUrl = callbackData.getUrl();
			if (callbackUrl != null) {
				sendHeaders.put("X-Lenovows-OSS-Callback-Enabled", "true");
				sendHeaders.put("X-Lenovows-OSS-Callback", callbackUrl);
			}
		}

		HttpHelper hhp = new HttpHelper();
		RequestAndResponse rr = null;
		if (input.markSupported()) {
			input.mark((int) size);
		}
		rr = hhp.performPut(url, null, null, sendHeaders, null, input, size,
				listener, progressData);

		try {
			retryCancelResponseStatus(HttpHelper.PUT_METHOD, hhp, rr, url,
					sendHeaders, input, size, listener, progressData);
			rr.request.releaseConnection();
		} catch (IOException e) {
			if (rr != null)
				rr.request.abort();
			throw e;
		} finally {
			hhp.closeConnections();
		}
	}

	private void putObjectByChunk(String url, String contentType, String token,
			InputStream input, long size, long offset,
			OssManagerListener listener, CallbackData callbackData,
			Object progressData) throws IOException {
		String authtoken = token;

		Map<String, String> sendHeaders = new HashMap<String, String>();
		sendHeaders.put("X-Lenovows-Authorization", authtoken);
		sendHeaders.put("Content-Type", contentType);
		if (callbackData != null) {
			Map<String, String> entry = callbackData.getHeaderData();
			String start = Long.toString(offset);
			String end = Long.toString(offset + size - 1);
			String range = start + "-" + end;
			sendHeaders.put("X-Lenovows-Range", range);
			if (entry != null) {
				String partNumber = entry.get("partnumber");
				if (partNumber == null) {
					return;
				}
				sendHeaders.put("X-Lenovows-Part-Number", partNumber);
			}
			String callbackUrl = callbackData.getUrl();
			if (callbackUrl != null) {
				sendHeaders.put("X-Lenovows-OSS-Callback-Enabled", "true");
//		        sendHeaders.put("X-Lenovows-OSS-Callback", callbackUrl);
			}
		}

		HttpHelper hhp = new HttpHelper();
		RequestAndResponse rr = null;
		if (input.markSupported()) {
			input.mark((int) size);
		}
		rr = hhp.performPut(url, null, null, sendHeaders, null, input, size,
				listener, progressData);

		try {
			retryCancelResponseStatus(HttpHelper.PUT_METHOD, hhp, rr, url,
					sendHeaders, input, size, listener, progressData);
			rr.request.releaseConnection();
		} catch (IOException e) {
			if (rr != null)
				rr.request.abort();
			throw e;
		} finally {
			hhp.closeConnections();
		}
	}

	private void commitObject(String url, String contentType, String token,
			CallbackData callbackData) throws IOException {
		String authtoken = token;

		Map<String, String> sendHeaders = new HashMap<String, String>();
		sendHeaders.put("X-Lenovows-Authorization", authtoken);
		sendHeaders.put("Content-Type", contentType);
		if (callbackData != null) {
			Map<String, String> entry = null;
			entry = callbackData.getHeaderData();
			String num = entry.get("partnumber");
			if (num == null) {
				return;
			}
			sendHeaders.put("X-Lenovows-Part-Number", num);
			String callbackUrl = callbackData.getUrl();
			if (callbackUrl != null) {
				sendHeaders.put("X-Lenovows-OSS-Callback-Enabled", "true");
				sendHeaders.put("X-Lenovows-OSS-Callback", callbackUrl);
			}
		}
		sendHeaders.put("X-Lenovows-Commit", "true");
		logger.info("commitObject sendHeaders Part Number:"
				+ sendHeaders.get("X-Lenovows-Part-Number"));

		HttpHelper hhp = new HttpHelper();
		RequestAndResponse rr = null;
		rr = hhp.performPut(url, null, null, sendHeaders, null, null, 0, null,
				null);
		try {
			retryCancelResponseStatus(HttpHelper.PUT_METHOD, hhp, rr, url,
					sendHeaders, null, 0, null, null);
			rr.request.releaseConnection();
		} catch (IOException e) {
			if (rr != null)
				rr.request.abort();
			throw e;
		} finally {
			hhp.closeConnections();
		}
	}

	private String batchPutObjectList(String url, String token,
			InputStream input, long size, OssManagerListener listener,
			CallbackData callbackData, Object progressData) throws IOException {
		String authtoken = token;

		Map<String, String> sendHeaders = new HashMap<String, String>();
		sendHeaders.put("X-Lenovows-Authorization", authtoken);
		if (callbackData != null) {
			String callbackUrl = callbackData.getUrl();
			if (callbackUrl != null) {
				sendHeaders.put("X-Lenovows-OSS-Callback-Enabled", "true");
				sendHeaders.put("X-Lenovows-OSS-Callback", callbackUrl);
			}
		}

		// perform http request
		HttpHelper hhp = new HttpHelper();
		hhp.setMultipart(true);
		RequestAndResponse rr = null;
		rr = hhp.performPost(url, null, null, sendHeaders, null, input, size,
				listener, progressData);

		byte[] body = handlerResponseBody(hhp, rr, HttpHelper.POST_METHOD, url,
				sendHeaders, input, size, listener, progressData);
		if (body == null) {
			throw new IOException("http body is null");
		}

		String json = new String(body);
		return json;
	}

	private List<Map<String, Object>> batchGetObjectList(String url,
			String token, InputStream input, long length, long size,
			OssManagerListener listener, CallbackData callbackData,
			Object progressData) throws IOException {
		String authtoken = token;

		Map<String, String> sendHeaders = new HashMap<String, String>();
		sendHeaders.put("X-Lenovows-Authorization", authtoken);
		if (callbackData != null) {
			String callbackUrl = callbackData.getUrl();
			if (callbackUrl != null) {
				sendHeaders.put("X-Lenovows-OSS-Callback-Enabled", "true");
				sendHeaders.put("X-Lenovows-OSS-Callback", callbackUrl);
			}
		}

		// perform http request
		HttpHelper hhp = new HttpHelper();
		RequestAndResponse rr = null;
		rr = hhp.performPost(url, null, null, sendHeaders, null, input, length,
				null, null);

		List<Map<String, Object>> resultVal = null;
		try {
			rr = retryCheckResponseStatus(HttpHelper.POST_METHOD, hhp, rr, url,
					sendHeaders, input, length, null, null);
			HttpResponse response = rr.response;
			String metaLengthName = "X-Lenovows-OSS-Meta-Length";
			int metaLength = 0;
			if (response.containsHeader(metaLengthName)) {
				String metaLengthStr = response.getFirstHeader(metaLengthName)
						.getValue();
				metaLength = Integer.valueOf(metaLengthStr);
			} else {
				throw new IOException("http 404:Object metadata not found!");
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream is = entity.getContent();
				byte[] metadata = PilotOssHelper.inputStream2Byte(is,
						metaLength);
				if (metadata == null) {
					throw new IOException("http body is null");
				}
				try {
					String tempStorage = callbackData.getData().get(
							PilotOssConstants.TEMP_STORAGE);
					resultVal = writeStreamToOutput(is, metadata, metaLength,
							tempStorage, size, listener, progressData);
					return resultVal;
				} catch (JSONException e) {
					logger.error("JSONException:", e);
				} finally {
					EntityUtils.consume(entity);
					is.close();
				}

			}
			rr.request.releaseConnection();
			return resultVal;
		} catch (IOException ioe) {
			if (rr != null)
				rr.request.abort();
			throw ioe;
		} finally {
			hhp.closeConnections();
		}
	}

	private List<Map<String, Object>> writeStreamToOutput(InputStream input,
			byte[] metaByte, int metaLength, String outputPath, long size,
			OssManagerListener listener, Object progressData)
			throws JSONException, IOException {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		ProgressData progress = (ProgressData) progressData;

		String json = new String(metaByte);
		OutputStream output = null;
		try {
			JSONArray array = new JSONArray(json);
			int objectNum = array.length();
			for (int i = 0; i < objectNum; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject object = (JSONObject) array.get(i);
				String keyID = (String) object.get("key");
				Integer length = (Integer) object.get("length");
				// Integer offset=(Integer)object.get("offset");
				String tempFile = outputPath + File.separator + keyID;
				output = new FileOutputStream(tempFile);
				writeStreamToOutput(input, output, length, listener,
						progressData);
				progress.setCompleted(length);
				map.put("key", keyID);
				map.put("length", length);
				map.put("tempFilePath", tempFile);
				result.add(map);
			}
			return result;
		} finally {
			try {
				if (input != null)
					input.close();
				if (output != null)
					output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("Closing stream IOException:", e);
			}
		}
	}

}
