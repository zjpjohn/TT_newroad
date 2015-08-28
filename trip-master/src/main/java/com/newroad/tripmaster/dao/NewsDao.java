package com.newroad.tripmaster.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import com.newroad.tripmaster.dao.pojo.News;
import com.newroad.tripmaster.dao.pojo.Scenic;


public class NewsDao extends BasicDAO<Scenic, ObjectId>{

  protected NewsDao(Datastore ds) {
    super(ds);
    // TODO Auto-generated constructor stub
  }

  public List<News> getNewsList(){
    Query<News> newsQuery=getDs().find(News.class, "status", 1);
    List<News> newsList=newsQuery.asList();
    return newsList;
  }
  
}
