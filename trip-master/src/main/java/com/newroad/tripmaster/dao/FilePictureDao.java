package com.newroad.tripmaster.dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import com.newroad.tripmaster.dao.pojo.Scenic;


public class FilePictureDao extends BasicDAO<Scenic, ObjectId>{

  protected FilePictureDao(Datastore ds) {
    super(ds);
    // TODO Auto-generated constructor stub
  }

}
