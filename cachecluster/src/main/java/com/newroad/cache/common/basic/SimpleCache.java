package com.newroad.cache.common.basic;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.newroad.cache.common.Cache;

public class SimpleCache implements Cache<String, Serializable> {

  ConcurrentMap<String, Serializable> map = new ConcurrentHashMap<String, Serializable>();

  public SimpleCache() {
    super();
  }

  @Override
  public Serializable get(String key) {
    return map.get(key);
  }

  @Override
  public Map<String, Serializable> getAllPresent(Iterable<String> keys) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean set(String key, Serializable value) {
    map.put(key, value);
    return true;
  }

  @Override
  public boolean set(String key, Serializable value, long expire) {
    map.put(key, value);
    return true;
  }

  @Override
  public int setAll(Map<? extends String, ? extends Serializable> m) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean delete(String key) {
    map.remove(key);
    return true;
  }

  @Override
  public int deleteAll(Iterable<String> keys) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean deleteAll() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public long size() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public ConcurrentMap<String, Serializable> asMap() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<String> keys() {
    // TODO Auto-generated method stub
    return null;
  }
}
