package com.lenovo.zy.info.crawler.manager.db;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lenovo.zy.info.crawler.dao.TestAnswerDaoIf;
import com.lenovo.zy.info.crawler.dao.TestDaoIf;
import com.lenovo.zy.info.crawler.domain.Test;
import com.lenovo.zy.info.crawler.domain.TestAnswer;

public class QuestionDao {
  public static Logger logger = LoggerFactory.getLogger(QuestionDao.class);

  private SqlSession session;

  public QuestionDao(SqlSession session) {
    this.session = session;
  }

  public long saveTest(Test test) {
    TestDaoIf testDao = session.getMapper(TestDaoIf.class);
    int count = testDao.insertTest(test);
    session.commit();
    logger.debug("Test Insert Count:" + count);
    if(count==1){
      return test.getTestId();
    }
    return 0;
  }

  public long saveTestAnswer(TestAnswer testAnswer) {
    TestAnswerDaoIf testAnswerDao = session.getMapper(TestAnswerDaoIf.class);
    int count = testAnswerDao.insertTestAnswer(testAnswer);
    session.commit();
    logger.debug("TestAnswer Insert Count:" + count);
    if(count==1){
      return testAnswer.getAnswerId();
    }
    return 0;
  }

}
