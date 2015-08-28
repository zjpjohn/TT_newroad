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
import com.newroad.tripmaster.dao.pojo.info.Comment;


public class CommentDao extends BasicDAO<Comment, ObjectId> {

  private static Logger logger = LoggerFactory.getLogger(CommentDao.class);

  protected CommentDao(Datastore ds) {
    super(ds);
    // TODO Auto-generated constructor stub
  }

  public Object submitComment(Comment comment) {
    try {
      Key<Comment> key = getDs().save(comment);
      return key.getId();
    } catch (Exception ex) {
      logger.error("Fail to save travel POI:", ex);
    }
    return null;
  }

  public List<Comment> listComments(Integer type, Long userId, String targetId) {
    Query<Comment> commentQueryArray = getDs().find(Comment.class).field(DataConstant.ACTION_TYPE).equal(type);
    if (userId != null) {
      commentQueryArray.field(DataConstant.USER_ID).equal(userId);
    }
    if (targetId != null) {
      commentQueryArray.field(DataConstant.TARGET_ID).equal(targetId);
    }
    commentQueryArray.order("-updateTime").limit(TipsDao.QUERY_LIMIT);
    List<Comment> commentList = commentQueryArray.asList();
    logger.info("Target Comments:" + commentList);
    return commentList;
  }
}
