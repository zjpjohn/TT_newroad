package com.newroad.fileext.utilities;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.fileext.data.model.FileMimeType;

import net.sf.json.JSONObject;


/**
 * @info
 * @author tangzj1
 * @date Dec 6, 2013
 * @version
 */
public class AudioConvertor {

  private static Logger logger = LoggerFactory.getLogger(AudioConvertor.class);

  public static JSONObject audioHandler(String fileName, String contentType, String tempStorePath) {
    String convertFilePath = null;
    JSONObject json = new JSONObject();
    String wavFilePath = tempStorePath + "_decode.wav";
    Transform.decodeAudioFile(SystemProperties.speexdecPath, tempStorePath, wavFilePath, null);
    String decode = "kk";
    File wavFile = new File(wavFilePath);
    if (wavFile.exists()) {
      int extIndex = fileName.indexOf(".kk");
      if (extIndex >= 0) {
        fileName = fileName.substring(0, extIndex) + ".wav";
      }
      convertFilePath = wavFilePath;
      contentType = FileMimeType.wav.getContentType();
      decode = "wav";

      String mp3FilePath = tempStorePath + "_decode.mp3";
      Transform.decodeAudioFile(SystemProperties.lamePath, wavFilePath, mp3FilePath, null);
      File mp3File = new File(mp3FilePath);
      if (mp3File.exists()) {
        fileName = fileName.substring(0, fileName.indexOf(".wav")) + ".mp3";
        convertFilePath = mp3FilePath;
        contentType = FileMimeType.mp3.getContentType();
        decode = "mp3";
      }
      json.put(FileDataConstant.FILE_NAME, fileName);
      json.put(FileDataConstant.FILE_CONTENT_TYPE, contentType);
      json.put(FileDataConstant.TEMP_FILE_PATH, convertFilePath);
      logger.info("Decode file status:" + decode + "; FileName:" + fileName + "; ContentType:" + contentType + "; convertFilePath:"
          + convertFilePath);
      return json;
    } else {
      logger.error("Fail to convert kk file to wav file in the path "+tempStorePath+"!");
      return null;
    }
  }

}
