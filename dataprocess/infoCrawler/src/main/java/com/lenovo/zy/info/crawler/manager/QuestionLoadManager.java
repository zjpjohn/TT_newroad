package com.lenovo.zy.info.crawler.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lenovo.zy.info.crawler.constants.Constants;
import com.lenovo.zy.info.crawler.domain.FileData;
import com.lenovo.zy.info.crawler.domain.Test;
import com.lenovo.zy.info.crawler.domain.TestAnswer;
import com.lenovo.zy.info.crawler.manager.db.QuestionDao;
import com.lenovo.zy.info.crawler.manager.db.SqlSessionManager;
import com.lenovo.zy.info.crawler.manager.db.ZYProductDao;

public class QuestionLoadManager {


  public static Logger logger = LoggerFactory.getLogger(QuestionLoadManager.class);

  private QuestionDao questionDao;

  private ZYProductDao productDao;

  public QuestionLoadManager() {
    super();
    this.questionDao = new QuestionDao(SqlSessionManager.getSqlSession());
    this.productDao = new ZYProductDao(SqlSessionManager.getSqlSession());
  }

  public Test loadTestProperties(String mainFilePath, String pictureUrl) {
    Test test = new Test();
    String title = null;
    String content = null;
    String picUrl = pictureUrl;

    Properties props = Utils.getProperties(mainFilePath);
    title = searchKeyProperty(props, "title");
    content = props.getProperty("content");
    test.setTitle(title);
    test.setContent(content);
    test.setImage(picUrl);
    test.setStatus(1);

    return test;
  }

  public static String searchKeyProperty(Properties props, String name) {
    String value = null;
    Set<Entry<Object, Object>> set = props.entrySet();
    Iterator<Entry<Object, Object>> iter = set.iterator();
    while (iter.hasNext()) {
      Entry<Object, Object> entry = iter.next();
      logger.debug("Property Info:" + entry);
      // name key contain blank char
      String key = (String) entry.getKey();
      if (key.indexOf(name) >= 0) {
        value = (String) entry.getValue();
        break;
      }
    }
    return value;
  }

  public List<TestAnswer> loadAnswerProperties(Long testId, Map<String, String> answerMap) {
    List<TestAnswer> answerList = new ArrayList<TestAnswer>();
    Set<Entry<String, String>> set = answerMap.entrySet();
    Iterator<Entry<String, String>> iter = set.iterator();
    while (iter.hasNext()) {
      Entry<String, String> entry = iter.next();
      String choice = entry.getKey();
      String answerInfoPath = entry.getValue();
      Properties props2 = Utils.getProperties(answerInfoPath);
      TestAnswer testAnswer = new TestAnswer();
      testAnswer.setTestId(testId);
      testAnswer.setAnswer(choice);
      testAnswer.setDescription(searchKeyProperty(props2, "choice"));
      testAnswer.setMeaning(props2.getProperty("answer"));
      answerList.add(testAnswer);
    }
    return answerList;
  }

  public Long saveTestInfo(Test test) {
    Long testId = questionDao.saveTest(test);
    return testId;
  }

  public void saveTestAnswer(TestAnswer testAnswer) {
    questionDao.saveTestAnswer(testAnswer);
  }

  public String savePicture(String filePath, Integer fileType) {
    String pictureUrl = null;
    String localDestFolderPath = Constants.ZY_QUESTION_STORE_HEADER;
    String linkUrlPath = Constants.QUESTION_LINK_HEADER;
    File destFolder = new File(localDestFolderPath);
    if (!destFolder.exists())
      destFolder.mkdir();
    logger.info("Create local folder path:" + destFolder.getAbsolutePath());
    File file = new File(filePath);
    if(file.length()<=0){
      logger.error("Fail to get picture data because the length is zero!");
      return null;
    }
    String newFileName = UUID.randomUUID().toString() + "_" + file.getName();
    // File newFile = new File(localDestFolderPath + File.separator + newFileName);
    String srcPath = filePath;
    String destPath = localDestFolderPath + newFileName;
    Utils.copyFileChannel(srcPath, destPath);
    String fileLink = linkUrlPath + newFileName;
    logger.info("FileLink:" + fileLink);
    FileData fileData = new FileData(newFileName, fileType, "", fileLink, destPath, file.length());
    productDao.saveResource(fileData);
    pictureUrl = fileLink;
    return pictureUrl;
  }

}
