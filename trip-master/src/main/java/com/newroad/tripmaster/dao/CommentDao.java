package com.newroad.tripmaster.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.tripmaster.constant.DataConstant;
import com.newroad.tripmaster.dao.pojo.Scenic;
import com.newroad.tripmaster.dao.pojo.info.Comment;


public class CommentDao extends BasicDAO<Scenic, ObjectId>{

  private static Logger logger = LoggerFactory.getLogger(CommentDao.class);
  
  protected CommentDao(Datastore ds) {
    super(ds);
    // TODO Auto-generated constructor stub
  }

  public Object submitComment(Comment comment) {
    Key<Comment> key = getDs().save(comment);
    return key.getId();
  }

  public List<Comment> listComments(String hashsiteid) {
    Query<Comment> commentQueryArray =
        getDs().find(Comment.class).field(DataConstant.HASH_SITE_ID).equal(hashsiteid).order("-lastupdatedtime").limit(TipsDao.QUERY_LIMIT);
    List<Comment> commentList = commentQueryArray.asList();
    logger.info("Scenic Comments:" + commentList);
    return commentList;
  }
}