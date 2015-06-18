package com.newroad.fileext.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

public class FileResourceSQLDao {

  private SqlSession sqlSession;

  public <T> T selectOne(String sqlID, Object param) {
    return sqlSession.selectOne(sqlID, param);
  }

  public <T> List<T> selectList(String sqlID, Object param) {
    return sqlSession.selectList(sqlID, param);
  }

  public int insert(String sqlID, Object param) {
    return sqlSession.insert(sqlID, param);
  }

  public int update(String sqlID, Object param) {
    return sqlSession.update(sqlID, param);
  }

  public int delete(String sqlID, Object param) {
    return sqlSession.delete(sqlID, param);
  }


  public void setSqlSession(SqlSession sqlSession) {
    this.sqlSession = sqlSession;
  }


}
