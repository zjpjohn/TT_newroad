package com.newroad.fileext.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @info Process block utilities
 * @author tangzj1
 * @date Sep 30, 2013
 * @version
 */
public class ProcessUtil {

	private static Logger logger = LoggerFactory.getLogger(ProcessUtil.class);

	public static void execProcess(String... cmdStr) {
		Process process = null;
		try {
			logger.info("Process Command:" + Arrays.toString(cmdStr));
			process = Runtime.getRuntime().exec(cmdStr);
			//new ProcessClearStream(process.getInputStream(), "INFO").start();
			new ProcessClearStream(process.getErrorStream(), "ERROR").start();
			//int status = process.waitFor();
			//logger.trace("Process exitValue: " + status);
		} catch (Exception e) {
			logger.error("Execute Process Exception!", e);
		} finally {
			if (process != null) {
				if (process.getInputStream() != null) {
					try {
						process.getInputStream().close();
					} catch (Exception e2) {
						logger.error("Process getInputStream() bring an exception : ", e2);
					}
				}
				if (process.getErrorStream() != null) {
					try {
						process.getErrorStream().close();
					} catch (Exception e2) {
						logger.error("process.getErrorStream() bring an exception : ", e2);
					}
				}
				if (process.getOutputStream() != null) {
					try {
						process.getOutputStream().close();
					} catch (Exception e2) {
						logger.error("process.getOutputStream() bring an exception : ", e2);
					}
				}
				process.destroy();
			}
			process = null;
		}
	}

	public static String execCallableProcess(String... cmdStr) {
		Process process = null;
		try {
			//logger.debug("CallableProcess Command:" + Arrays.toString(cmdStr));
			process = Runtime.getRuntime().exec(cmdStr);
			FutureTask<String> task = new FutureTask<String>(
					new ProcessCallableClearStream(process.getInputStream(),
							"INFO"));
			new Thread(task).start();
			//int status = process.waitFor();
			//logger.debug("Process exitValue: " + status);
			return task.get();
		} catch (Exception e) {
			logger.error("Execute Process Exception!", e);
		} finally {
			if (process != null) {
				if (process.getInputStream() != null) {
					try {
						process.getInputStream().close();
					} catch (Exception e2) {
						logger.error("Process getInputStream() bring an exception : ", e2);
					}
				}
				if (process.getErrorStream() != null) {
					try {
						process.getErrorStream().close();
					} catch (Exception e2) {
						logger.error("process.getErrorStream() bring an exception : ", e2);
					}
				}
				if (process.getOutputStream() != null) {
					try {
						process.getOutputStream().close();
					} catch (Exception e2) {
						logger.error("process.getOutputStream() bring an exception : ", e2);
					}
				}
				process.destroy();
			}
			process = null;
		}
		return null;
	}
}

class ProcessClearStream extends Thread {

	private static Logger logger = LoggerFactory
			.getLogger(ProcessClearStream.class);

	private InputStream inputStream;
	private String type;

	ProcessClearStream(InputStream inputStream, String type) {
		this.inputStream = inputStream;
		this.type = type;
	}

	public void run() {
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream);
			BufferedReader br = new BufferedReader(inputStreamReader);
			String line = null;
			while ((line = br.readLine()) != null) {
				logger.trace(type + ">" + line);
			}
		} catch (IOException ioe) {
			logger.error("Clear Stream IOException:", ioe);
		}
	}
}

class ProcessCallableClearStream implements Callable<String> {

	private static Logger logger = LoggerFactory
			.getLogger(ProcessClearStream.class);

	private InputStream inputStream;
	private String type;

	ProcessCallableClearStream(InputStream inputStream, String type) {
		this.inputStream = inputStream;
		this.type = type;
	}

	public String call() {
		StringBuffer sb = new StringBuffer();
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream);
			BufferedReader br = new BufferedReader(inputStreamReader);
			String line = null;
			while ((line = br.readLine()) != null) {
				logger.debug(type + ">" + line);
				sb.append(line);
			}
		} catch (IOException ioe) {
			logger.error("Clear Stream IOException!", ioe);
		}
		return sb.toString();
	}
}
