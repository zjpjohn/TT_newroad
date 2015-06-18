package com.newroad.fileext.utilities;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class Transform.
 * 
 * @author tangzj1
 * 
 */
public class Transform {

	private static Logger logger = LoggerFactory.getLogger(Transform.class);

	/**
	 * Decode spx file.
	 * 
	 * @param spxFilePath
	 *            the spx file path
	 * @param wavFilePath
	 *            the wav file path
	 * @return the string
	 */
	public static void decodeAudioFile(String cmdPath, String sourceFilePath,
			String targetFilePath, String rate) {
		if (sourceFilePath == null) {
			logger.error("source file path is null.");
			return;
		}
		if (targetFilePath == null) {
			logger.error("target file path is null.");
			return;
		}
		File sourceFile = new File(sourceFilePath);
		if (!sourceFile.exists()) {
			logger.error("source file path is null or couldn't find this file.");
			return;
		}
		String[] cmdArray = null;
		if (rate != null) {
			cmdArray = new String[] { cmdPath, sourceFilePath, targetFilePath,
					"--rate", rate };
		} else {
			cmdArray = new String[] { cmdPath, sourceFilePath, targetFilePath };
		}
		ProcessUtil.execProcess(cmdArray);
		// Process p = Runtime.getRuntime().exec(new
		// String[]{SPEEDEC_PATH,spxFilePath,wavFilePath,"--rate",rate});
	}

	public static void main(String[] args) {
		if (args[0] != null && args[1] != null && args[2] != null) {
			decodeAudioFile(args[0], args[1], args[2], null);
		}
	}
}
