package com.lenovo.zy.info.crawler.manager.db;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lenovo.zy.info.crawler.wp.dao.WPPostDaoIf;
import com.lenovo.zy.info.crawler.wp.dao.WPPostMetaDaoIf;
import com.lenovo.zy.info.crawler.wp.domain.WPPost;
import com.lenovo.zy.info.crawler.wp.domain.WPPostMeta;

public class WPPostDao {

  public static Logger logger = LoggerFactory.getLogger(WPPostDao.class);

  private SqlSession session;

  public WPPostDao(SqlSession session) {
    this.session = session;
  }
  
  public long savePost(WPPost post) {
    WPPostDaoIf wppostDao = session.getMapper(WPPostDaoIf.class);
    int id = wppostDao.insertWPPost(post);
    session.commit();
    logger.debug("WPPost Insert Count:" + id);
    return id;
  }
  
  public long savePostMeta(WPPostMeta postMeta) {
    WPPostMetaDaoIf postMetaDao = session.getMapper(WPPostMetaDaoIf.class);
    int id = postMetaDao.insertWPPostMeta(postMeta);
    session.commit();
    logger.debug("WPPostMeta Insert Count:" + id);
    return id;
  }
}
