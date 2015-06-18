package com.newroad.fileext.utilities;

import org.springframework.http.MediaType;

public class FileDataConstant {

  public enum FileStatus {
    SUCCESS("File SUCCESS"), NOFILE("No File"), TYPE("Unsupport Type"), SIZE("File Size Overload"), ENTYPE("Request Type Entype Error"), REQUEST(
        "Rquest Exception"), IO("IO Exception"), DIR("Fail to Create Directory"), UNKNOWN("Unknow Exception");

    private String message;

    private FileStatus(String message) {
      this.message = message;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

  }

  public static final String LINK_HEADER = "http://zy.lenovomm.com/images/products/";
  
  public static final String ZY_FILE_STORE_HEADER = "/data/productsImg/";
  
  public static final String PUBLIC_LINK_TAG="http://iocos.lenovows.com/v2/tiny/";
  public static final String AUTH_TOKEN = "AuthToken";
  public static final String LENOVO_ST = "lenovoST";
  public static final String REALM = "realm";
  public static final String COS_CONNECTOR = "cosConnector";
  public static final String ATTACHMENT = "attachment";
  public static final String FORM_FIELD = "formField";
  public static final String FILE_FIELD = "fileField";
  public static final String FILE_NAME = "fileName";
  public static final String FILE_CONTENT_TYPE = "contentType";
  public static final String FILE_SIZE = "size";
  public static final String FILE_STREAM = "fileStream";
  public static final String LOCAL_FOLDER_PATH = "localFolderPath";
  public static final String TEMP_FILE_PATH = "tempFilePath";
  public static final String RESOURCES = "resources";
  public static final String CLIENT_RESOURCE_ID = "clientResourceID";
  public static final String FILE_DATE = "fileData";
  public static final String RESOURCE_KEY_ID = "keyID";
  public static final String RESOURCE_LINK = "link";
  public static final String RESOURCE_PUBLIC_LINK = "publicLink";
  public static final String THUMBNAIL = "thumbnail";
  public static final String THUMBNAIL_SCALE = "scale";
  public static final String THUMBNAIL_WIDTH = "thumbnailWidth";
  public static final String THUMBNAIL_HEIGHT = "thumbnailHeight";
  public static final String LINK_VALIDTIME = "linkValidTime";
  public static final String LINK_ACCESSLIMIT = "linkAccessLimit";
  public static final String LINK_FAILREDIRECT = "linkFailRedirect";
  public static final String FILE_STATE = "state";
  public static final String FILE_STATE_FAILURE = "FAILURE";
  public static final String FILE_STATE_SUCCESS = "SUCCESS";

  public static final String EXECUTION_STATUS = "executionStatus";
  public static final String EXECUTION_RESULT = "executionResult";
  /** The Constant CONTENT_TYPE_JSON. */
  public static final String CONTENT_TYPE_JSON = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8";

  public static final String CLOUD_SIZE = "cloudSize";
  public static final String CLOUD_STATUS = "cloudStatus";
  public static final String LAST_UPDATE_TIME = "lastUpdateTime";
  public static final String USER_CREATE_TIME = "userCreateTime";
  public static final String USER_ID = "userID";

  // SQL ID
  public static final String SELECT_FILES = "";
  public static final String SELECT_FILE_BY_KEYID = "getFileDataByKeyID";
  public static final String UPDATE_FILE_BY_KEYID = "updateFileDataByKeyID";
  public static final String DELETE_FILE_BY_KEYID = "deleteFileDataByKeyID";

  public static final String SELECT_FILE_BY_CLIENTID = "getFileDataByClientID";
  public static final String UPDATE_FILE_BY_CLIENTID = "updateFileDataByClientID";
  public static final String DELETE_FILE_BY_CLIENTID = "deleteFileDataByClientID";

  public static final String SELECT_FILES_BY_STATUS = "getFileListByStatus";
  public static final String SELECT_FILES_BY_CLOUD_STATUS = "getFileListByCloudStatus";
  public static final String INSERT_FILE = "insertFileData";
  public static final String SYNC_CLOUD_FILE_STATUS = "syncCloudFileStatus";
  public static final String UPDATE_FILES = "updateFileData";

}
