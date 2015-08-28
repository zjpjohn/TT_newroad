package com.lenovo.zy.info.crawler.manager.db;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlSessionManager {

  public static Logger logger = LoggerFactory.getLogger(SqlSessionManager.class);

  public static final String resourceFilePath = "resources/zy/zyBatis-config.xml";
  public static final String resource2FilePath = "resources/zy/wpBatis-config.xml";

  private static SqlSession session;
  private static SqlSession session2;

  private SqlSessionManager() {

  }

  private static SqlSession buildDbFactory(String resourceFile) {
    Reader reader;
    try {
      reader = Resources.getResourceAsReader(resourceFile);
      SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
      SqlSessionFactory factory = builder.build(reader);
      return factory.openSession();
    } catch (IOException e) {
      logger.error("Fail to build db instance:", e);
    }
    return null;
  }

  public static SqlSession getSqlSession() {
    if (session == null) {
      session = buildDbFactory(resourceFilePath);
    }
    return session;
  }

  public static SqlSession getWPSqlSession() {
    if (session2 == null) {
      session2 = buildDbFactory(resource2FilePath);
    }
    return session2;
  }

}
