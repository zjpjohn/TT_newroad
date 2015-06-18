package com.newroad.fileext.utilities;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @info
 * @author tangzj1
 * @date Nov 28, 2013
 * @version
 */
public class SystemProperties {

  private static Logger logger = LoggerFactory.getLogger(SystemProperties.class);

  public final static String appID = "004045d5e5dd1000";
  public final static String devID = "6685babb85d7980fe7b70c576d5645b9";
  public final static String devKey = "e05dcf8d27154bb5be6023f4e4614416";
  public final static String workspace = "lenoteSpace";

  // public final static String appID = "003fc015680eb800";
  // public final static String devID = "534fc6dc1a881ea7dd2e013f752fd391";
  // public final static String devKey = "efb79495631a8ee515dc19ad4f37d69b";
  // public final static String workspace = "lenote";

  public final static String spec = "";
  public final static String bucketName = "lenote";

  public final static String testUser = "yanfei6@lenovo.com";
  public final static String testPassword = "000000";
  public final static String cloudUser = "tangzj1@lenovo.com";
  public final static String cloudPassword = "000000";
  public final static String omsUser = "supernoteOMS@lenovo.com";
  public final static String omsPassword = "000000";
  public final static String realm = "supernote.lenovo.com";

  public static String logPath;
  public static Integer logLevel;
  public static Boolean useListener;

  public static String imageMagickPath;
  public static String graphicMagickPath;
  public static String speexdecPath;
  public static String lamePath;

  // thumbnail task timeout (second)
  public static Long thumbnailTaskTimeout;
  // cache cluster node space monitor interval (second)
  public static Long cacheNodeMonitorInterval;

  // Cloud callback
  public static Boolean cloudCallbackStatus = false;
  public static String cloudCallbackURL;
  public static String cloudRecifyMonitorLogPath;

  static {
    InputStream is = SystemProperties.class.getResourceAsStream("/config/system.properties");
    Properties props = new Properties();
    try {
      props.load(is);
      logPath = props.getProperty("COS_LOGPATH", logPath);
      logLevel = Integer.valueOf(props.getProperty("COS_LOGLEVEL"));
      useListener = Boolean.valueOf(props.getProperty("COS_LISTENER"));

      speexdecPath = props.getProperty("AUDIO_SPEEXDEC_PATH", speexdecPath);
      lamePath = props.getProperty("AUDIO_LAME_PATH", lamePath);
      imageMagickPath = props.getProperty("IMAGEMAGICK_PATH", imageMagickPath);
      graphicMagickPath = props.getProperty("GRAPHICMAGICK_PATH", graphicMagickPath);
      thumbnailTaskTimeout = Long.valueOf(props.getProperty("THUMBNAIL_TASK_TIMEOUT"));
      cacheNodeMonitorInterval = Long.valueOf(props.getProperty("CACHE_NODE_MONITOR_INTERVAL"));
      cloudCallbackStatus = Boolean.valueOf(props.getProperty("CLOUD_CALLBACK_STATUS", "false"));
      // logger.debug("cloudCallbackStatus properties setting is " + cloudCallbackStatus);
      cloudCallbackURL = props.getProperty("CLOUD_CALLBACK_URL", cloudCallbackURL);
      // callbackResourceURL = props.getProperty("CALLBACK_RESOURCE_URL", callbackResourceURL);
      cloudRecifyMonitorLogPath = props.getProperty("CLOUD_RECIFY_MONITOR_LOG_PATH", cloudRecifyMonitorLogPath);

      is.close();
    } catch (Exception e) {
      logger.error("load properties error", e);
    }

  }


}
