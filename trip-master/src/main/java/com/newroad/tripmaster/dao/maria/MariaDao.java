package com.newroad.tripmaster.dao.maria;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.newroad.util.Page;


/**
 * @info  : 数据访问层 
 * @author: xiangping_yu
 * @data  : 2013-11-14
 * @since : 1.5
 */
public class MariaDao {
	
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
	
	@SuppressWarnings("unchecked")
	public <T> Page<T> selectPage(String sqlId, Page<T> page){
		Map<String, Object> para = page.getPara();
		
		// 总记录数
		Integer count = (Integer)this.selectOne(sqlId+"_cnt", para);
		if(count==null || count.intValue()==0)
			return page;
		
		// 设置查询的起始位置
		page.setCount(count);
		para.put("beginNum", page.getBeginNum());
		para.put("offsetSize", page.getSize());
		
		page.setResult((List<T>) this.selectList(sqlId, para));
		return page;
	}
	
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
}
