package com.newroad.tripmaster.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class BeanDBObjectUtils {

  private static Logger logger = LoggerFactory.getLogger(BeanDBObjectUtils.class);

  /**
   * 
   * @param bean
   * @return
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   */
  public static <T> DBObject bean2DBObject(T bean) throws IllegalArgumentException, IllegalAccessException {
    if (bean == null) {
      return null;
    }
    DBObject dbObject = new BasicDBObject();
    Field[] fields = bean.getClass().getDeclaredFields();
    for (Field field : fields) {
      String varName = field.getName();
      boolean accessFlag = field.isAccessible();
      if (!accessFlag) {
        field.setAccessible(true);
      }
      Object param = field.get(bean);
      if (param == null) {
        continue;
      } else if (param instanceof Integer) {// �жϱ���������
        int value = ((Integer) param).intValue();
        dbObject.put(varName, value);
      } else if (param instanceof String) {
        String value = (String) param;
        dbObject.put(varName, value);
      } else if (param instanceof Double) {
        double value = ((Double) param).doubleValue();
        dbObject.put(varName, value);
      } else if (param instanceof Float) {
        float value = ((Float) param).floatValue();
        dbObject.put(varName, value);
      } else if (param instanceof Long) {
        long value = ((Long) param).longValue();
        dbObject.put(varName, value);
      } else if (param instanceof Boolean) {
        boolean value = ((Boolean) param).booleanValue();
        dbObject.put(varName, value);
      } else if (param instanceof Date) {
        Date value = (Date) param;
        dbObject.put(varName, value);
      }
      field.setAccessible(accessFlag);
    }
    return dbObject;
  }

  public static <T> Map<String, Object> bean2Map(T bean) {
    if (bean == null) {
      return null;
    }
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      Field[] fields = bean.getClass().getDeclaredFields();
      for (Field field : fields) {
        String varName = field.getName();
        if ("serialVersionUID".equalsIgnoreCase(varName)) {
          continue;
        }
        boolean accessFlag = field.isAccessible();
        if (!accessFlag) {
          field.setAccessible(true);
        }
        Object param = field.get(bean);
        if (param == null || "".equals(param)) {
          continue;
        } else{
          map.put(varName, param);
        } 
        field.setAccessible(accessFlag);
      }
      return map;
    } catch (IllegalArgumentException e) {
      logger.error("bean2Map IllegalArgumentException:" + e);
    } catch (IllegalAccessException e) {
      logger.error("bean2Map IllegalAccessException:" + e);
    }
    return null;
  }

  public static <T> Map<String, Object> formatBeanMap(Map<String, Object> map, T bean) {
    if (bean == null) {
      return null;
    }
    try {
      for (Map.Entry<String, Object> entry : map.entrySet()) {
        String key = entry.getKey();
        Field field = bean.getClass().getDeclaredField(key);
        if (field == null) {
          map.remove(key);
        }
      }
      return map;
    } catch (IllegalArgumentException e) {
      logger.error("bean2Map IllegalArgumentException:" + e);
    } catch (NoSuchFieldException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SecurityException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  public static Map<String, Object> filterBeanMap(Map<String, Object> map) {
    if (map == null) {
      return null;
    }

    try {
      Iterator<Entry<String, Object>> iter = map.entrySet().iterator();
      while (iter.hasNext()) {
        Entry<String, Object> entry = iter.next();
        //String key = entry.getKey();
        Object value = entry.getValue();
        if (value == null) {
          iter.remove();
          //Couldn't remove key/value data using map.remove(key) because iter.hasNext() will check the map size every time in while block.
          //map.remove(key);
        }
      }
      return map;
    } catch (IllegalArgumentException e) {
      logger.error("filterBeanMap IllegalArgumentException:" + e);
    } catch (SecurityException e) {
      logger.error("filterBeanMap SecurityException:" + e);
    }
    return null;
  }

  /**
   * 
   * @param dbObject
   * @param bean
   * @return
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   * @throws NoSuchMethodException
   */
  public static <T> T dbObject2Bean(DBObject dbObject, T bean) {
    if (bean == null) {
      return null;
    }
    Field[] fields = bean.getClass().getDeclaredFields();
    try {
      for (Field field : fields) {
        String varName = field.getName();
        Object object = dbObject.get(varName);
        if (object != null) {
          if (object instanceof DBObject) {
            BeanUtils.setProperty(bean, varName, dbObject2Bean((DBObject) object, field.getType().newInstance()));
          } else {
            BeanUtils.setProperty(bean, varName, object);
          }
        }
      }
    } catch (IllegalAccessException e) {
      logger.error("dbObject2Bean IllegalAccessException:" + e);
    } catch (InvocationTargetException e) {
      logger.error("dbObject2Bean InvocationTargetException:" + e);
    } catch (InstantiationException e) {
      logger.error("dbObject2Bean InstantiationException:" + e);
    }
    return bean;
  }

}
