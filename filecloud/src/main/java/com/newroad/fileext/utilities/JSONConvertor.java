package com.newroad.fileext.utilities;

import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONConvertor {

  private static Logger logger = LoggerFactory.getLogger(JSONConvertor.class);

  private static ObjectMapper objectMapper = null;

  public static ObjectMapper getJSONInstance() {
    if (objectMapper == null) {
      objectMapper = JsonFactory.create();
    }
    return objectMapper;
  }

  private JSONConvertor() {

  }

}
