package com.newroad.cos.pilot.util;

/**
 * @info
 * @author tangzj1
 * @date Aug 30, 2013
 * @version
 */
public interface PilotOssConstants {

  public static final String BUCKET = "bucket";

  public static final String OBJECT = "object";

  public static final String OBJECT_KEY = "object_key";

  public static final String OBJECT_KEYS = "keys";

  public static final String OBJECT_NUMBER = "object_number";

  public static final String METEDATA = "metadata";

  public static final String DISPLAY_NAME = "display_name";

  public static final String CALLBACK_TOKEN = "callback_authtoken";

  public static final String THUMB = "thumb";

  public static final String LINK = "link";

  public static final String PERVIEW = "perview";

  public static final String SCALE = "scale";
  
  public static final String TYPE = "type";

  public static final String PAGE_NUMBER = "page_number";

  public static final String VALID_TIME = "timeout";

  public static final String ACCESS_LIMIT = "access_limit";

  public static final String FAIL_REDIRECT = "invalid_redirect_url";

  public static final String OFFSET = "offset";

  public static final String LIMIT = "limit";

  public static long TOKEN_EXPIRATION = 5 * 60 * 60l;

  public static final String NET_ERROR = "-1";

  public static final String TEMP_STORAGE = "temp_storage";

  public static final String FILE_LIST = "file_list";

  public static final String FILE_NAME = "file_name";

  public static final String TEMP_FILE_PATH = "temp_file_path";

  public static final String CONTENT_TYPE = "content_type";

  public enum ThumbnailGenerateType {
    normal, area, resize;
  }
}
