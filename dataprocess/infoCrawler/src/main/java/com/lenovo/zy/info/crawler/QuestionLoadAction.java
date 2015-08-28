package com.lenovo.zy.info.crawler;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lenovo.zy.info.crawler.domain.Test;
import com.lenovo.zy.info.crawler.domain.TestAnswer;
import com.lenovo.zy.info.crawler.manager.QuestionLoadManager;
import com.lenovo.zy.info.crawler.manager.Utils;

public class QuestionLoadAction {

  public static Logger logger = LoggerFactory.getLogger(QuestionLoadAction.class);

  private QuestionLoadManager loadManager;

  private static final char[] nameRandom = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
      'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

  private void executeQuestionAction(String currentDateHeader, String mainFilePath, String mainPicPath, Map<String, String> answerMap) {
    loadManager = new QuestionLoadManager();
    String pictureUrl = loadManager.savePicture(mainPicPath, 1);
    if (pictureUrl == null) {
      logger.error("Fail to save picture data to DB!");
      return;
    }
    Test test = loadManager.loadTestProperties(mainFilePath, pictureUrl);
    Long testId = loadManager.saveTestInfo(test);
    List<TestAnswer> answerList = loadManager.loadAnswerProperties(testId, answerMap);
    for (TestAnswer answer : answerList) {
      loadManager.saveTestAnswer(answer);
    }
  }

  public static void main(String[] args) {
    String currentDateHeader = Utils.getCurrentDateInfo();
    logger.info("Current Info Crawller Date:" + currentDateHeader);

    if (args == null) {
      return;
    }

    QuestionLoadAction action = new QuestionLoadAction();
    if ("f".equals(args[0])) {
      String productFolder = args[1];
      File resouceFolder = new File(productFolder);
      if (resouceFolder.isDirectory()) {
        String[] filePaths = resouceFolder.list();
        for (int i = 0; i < filePaths.length; i++) {
          String testFolder = productFolder + File.separator + filePaths[i];
          Map<String, String> answerFileMap = listAnswerFiles(testFolder);
          String mainFilePath = testFolder + File.separator + "main.txt";

          String finalPath = null;
          String mainPicPath = testFolder + File.separator + "main.jpg";
          String mainPicPath2 = testFolder + File.separator + "main.JPG";
          File picfile = new File(mainPicPath);
          File picfile2 = new File(mainPicPath2);
          if (picfile.exists()) {
            finalPath = mainPicPath;
          } else if (picfile2.exists()) {
            finalPath = mainPicPath2;
          } else {
            logger.error("Fail to get source picture file in the path:" + mainPicPath);
          }
          action.executeQuestionAction(currentDateHeader, mainFilePath, finalPath, answerFileMap);
        }
      }
    }
  }

  private static Map<String, String> listAnswerFiles(String productFolder) {
    File productFile = new File(productFolder);
    String[] fileArray = productFile.list();
    int finalNum = fileArray.length - 2;
    Map<String, String> answerMap = new HashMap<String, String>(finalNum);

    for (int i = 0; i < finalNum; i++) {
      String choice = nameRandom[i] + "";
      answerMap.put(choice, productFolder + File.separator + nameRandom[i] + ".txt");
    }
    logger.info("answer file path string array:" + answerMap);
    return answerMap;

  }
}
