package com.newroad.fileext.dao.qiniu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.Mac;

import org.json.JSONException;

//import com.qiniu.api.auth.AuthException;
//import com.qiniu.api.auth.digest.Mac;
//import com.qiniu.api.io.IoApi;
//import com.qiniu.api.io.PutExtra;
//import com.qiniu.api.io.PutRet;
//import com.qiniu.api.rs.GetPolicy;
//import com.qiniu.api.rs.PutPolicy;
//import com.qiniu.api.rs.URLUtils;

public class QiNiuAccess {

	private static FileWriter log;
	public static String ACCESS_KEY = "sP6dgW3Slwmd5pWGtzjWIHeKihEKR4otFs_mHi4m";
	public static String SECRET_KEY = "0Ayw2SzAflVDyAdGE3dMsM1iKfrKM4khMw85Wgwk";
	private static int threadTask = 10;
	private static int fileCount;

	public void testCloudStorage(String sourceFile, String downloadFolder,
			String logFile) throws Exception {
		log = new FileWriter(logFile);
		QiNiuAccess.log("File,Type,Size(Byte),ExecutionTime(ms)");

		File file = new File(sourceFile);
		if (file.isDirectory()) {// 判读文件对象是否是文件夹
			File[] files = file.listFiles();
			long len = files.length;
			for (int i = 0; i < len; i++) {
				File unitFile = files[i];
				String fileName = CloudStorageUtils.generateKeyId() + "_"
						+ unitFile.getName();
				long fileSize = unitFile.length();
				try {
					String filePath = unitFile.getAbsolutePath();
					uploadResource(fileName, filePath, fileSize);
					downloadResource(fileName, downloadFolder, fileSize);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			long fileSize = file.length();
			String fileName = CloudStorageUtils.generateKeyId() + "_" + file.getName();
			try {
				uploadResource(fileName, sourceFile, fileSize);
				downloadResource(fileName, downloadFolder, fileSize);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void testConcurrentCloudStorage(String localFile,
			String outputFolder, String logFile) throws Exception {
		log = new FileWriter(logFile);
		QiNiuAccess.log("File,Type,Thread,Size(Byte),ExecutionTime(ms)");

		Map<String, Long> keyMap = new HashMap<String, Long>();
		File file = new File(localFile);
		if (file.isDirectory()) {// 判读文件对象是否是文件夹
			File[] files = file.listFiles();
			long len = files.length;
			for (int i = 0; i < len; i++) {
				File unitFile = files[i];
				long fileSize = unitFile.length();
				try {
					for (int j = 0; j < threadTask; j++) {
						String fileName = CloudStorageUtils.generateKeyId() + "_" + j
								+ "_" + unitFile.getName();
						keyMap.put(fileName, fileSize);
						new Thread(new UpLoad(j, fileName,
								unitFile.getAbsolutePath(), fileSize)).start();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			long fileNum = len * threadTask;
			while (fileCount < fileNum) {
				continue;
			}
			Iterator<Entry<String, Long>> iterator = keyMap.entrySet()
					.iterator();
			int k = 0;
			while (iterator.hasNext()) {
				Entry<String, Long> entry = iterator.next();
				new Thread(new DownLoad(k, entry.getKey(), outputFolder,
						entry.getValue())).start();
				k++;
			}
			while (fileCount > 0) {
				continue;
			}
			System.out.println("Download file count:" + fileCount);
			fileCount = 0;
		}
	}

	public void uploadResource(String key, String localFile, long fileSize) {
//		long start = System.currentTimeMillis();
//		Mac mac = new Mac(ACCESS_KEY, SECRET_KEY);
//		PutPolicy putPolicy = new PutPolicy("yuxiangping");
//		String token;
//		try {
//			token = putPolicy.token(mac);
//			PutExtra extra = new PutExtra();
//			PutRet ret = IoApi.putFile(token, key, localFile, extra);
//			if (ret.getStatusCode() == 200) {
//				QiNiuAccess.log(key + ",Upload Success," + fileSize + ","
//						+ (System.currentTimeMillis() - start));
//			} else {
//				QiNiuAccess.log(key + ",Upload Fail," + fileSize + ","
//						+ (System.currentTimeMillis() - start));
//			}
//		} catch (AuthException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public void downloadResource(String key, String localFile, long fileLength) {
//		long start = System.currentTimeMillis();
//		Mac mac = new Mac(ACCESS_KEY, SECRET_KEY);
//		String baseUrl;
//		try {
//			baseUrl = URLUtils.makeBaseUrl("yuxiangping.qiniudn.com", key);
//			GetPolicy getPolicy = new GetPolicy();
//			String downloadUrl = getPolicy.makeRequest(baseUrl, mac);
//
//			long bytesum = 0;
//			int byteread = 0;
//			URL url = new URL(downloadUrl);
//
//			URLConnection conn = url.openConnection();
//			InputStream inStream = conn.getInputStream();
//			FileOutputStream fs = new FileOutputStream(localFile
//					+ File.separator + key);
//
//			byte[] buffer = new byte[1024];
//			while ((byteread = inStream.read(buffer)) != -1) {
//				bytesum += byteread;
//				fs.write(buffer, 0, byteread);
//			}
//			inStream.close();
//			fs.close();
//
//			if (bytesum != fileLength) {
//				QiNiuAccess.log(key + ",Download Fail," + fileLength + ","
//						+ (System.currentTimeMillis() - start)+","+bytesum);
//			} else {
//				QiNiuAccess.log(key + ",Download Success," + fileLength + ","
//						+ (System.currentTimeMillis() - start));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public static synchronized void countThread(int _count) {
		fileCount += _count;
	}

	public static synchronized void log(String logData) {
		try {
			log.write(logData + " \r\n");
			log.flush();
			System.out.println(logData);
		} catch (Exception e) {
		}
	}
}

class UpLoad implements Runnable {

	private int thread;
	private String key;
	private String localFile;
	private long fileSize;

	public UpLoad(int thread, String key, String localFile, long fileSize) {
		this.thread = thread;
		this.key = key;
		this.localFile = localFile;
		this.fileSize = fileSize;
	}

	public void run() {
		long start = System.currentTimeMillis();

//		Mac mac = new Mac(QiNiuAccess.ACCESS_KEY, QiNiuAccess.SECRET_KEY);
//		PutPolicy putPolicy = new PutPolicy("yuxiangping");
//		String token;
//		try {
//			token = putPolicy.token(mac);
//			PutExtra extra = new PutExtra();
//			PutRet ret = IoApi.putFile(token, key, localFile, extra);
//			if (ret.getStatusCode() == 200) {
//				QiNiuAccess.log(key + ", Upload Success, Thread-[" + thread
//						+ "]," + fileSize + ","
//						+ (System.currentTimeMillis() - start));
//			} else {
//				QiNiuAccess
//						.log(key + ", Upload Fail, Thread-[" + thread + "],"
//								+ fileSize + ","
//								+ (System.currentTimeMillis() - start));
//			}
//		} catch (AuthException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		QiNiuAccess.countThread(1);
	}
}

class DownLoad implements Runnable {

	private int thread;
	private String key;
	private String localFile;
	private long fileSize;

	public DownLoad(int thread, String key, String localFile, long fileSize) {
		this.thread = thread;
		this.key = key;
		this.localFile = localFile;
		this.fileSize = fileSize;
	}

	public void run() {
		long start = System.currentTimeMillis();

//		Mac mac = new Mac(QiNiuAccess.ACCESS_KEY, QiNiuAccess.SECRET_KEY);
//		String baseUrl;
//		try {
//			baseUrl = URLUtils.makeBaseUrl("yuxiangping.qiniudn.com", key);
//			GetPolicy getPolicy = new GetPolicy();
//
//			String downloadUrl = getPolicy.makeRequest(baseUrl, mac);
//
//			int bytesum = 0;
//			int byteread = 0;
//			URL url = new URL(downloadUrl);
//
//			URLConnection conn = url.openConnection();
//			InputStream inStream = conn.getInputStream();
//			FileOutputStream fs = new FileOutputStream(localFile
//					+ File.separator + key);
//
//			byte[] buffer = new byte[1024];
//			while ((byteread = inStream.read(buffer)) != -1) {
//				bytesum += byteread;
//				fs.write(buffer, 0, byteread);
//			}
//
//			inStream.close();
//			fs.close();
//
//			if (bytesum != fileSize) {
//				QiNiuAccess.log(key + ", DOWNLoad Fail, Thread-[" + thread + "],"
//						+ fileSize + "," + (System.currentTimeMillis() - start)
//						+ "," + bytesum);
//			} else {
//				QiNiuAccess
//						.log(key + ", DOWNLoad Success, Thread-[" + thread + "],"
//								+ fileSize + ","
//								+ (System.currentTimeMillis() - start));
//			}
//			QiNiuAccess.countThread(-1);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
