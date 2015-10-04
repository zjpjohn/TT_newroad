package com.newroad.cache.common.ehcache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import net.sf.ehcache.CacheManager;

import com.newroad.cache.common.Cache;

public class Ehcache implements Cache<String, String> {

  private static CacheManager cacheManager;  

  private static net.sf.ehcache.Cache cache;
  
  @Override
  public String get(String key) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<String, String> getAllPresent(Iterable<String> keys) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean set(String key, String value) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean set(String key, String value, long expire) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public int setAll(Map<? extends String, ? extends String> m) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean delete(String key) {
    // TODO Auto-generated method stub
    return false;
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
  public ConcurrentMap<String, String> asMap() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<String> keys() {
    // TODO Auto-generated method stub
    return null;
  }

  
}
