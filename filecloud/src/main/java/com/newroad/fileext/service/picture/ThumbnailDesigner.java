package com.newroad.fileext.service.picture;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;
import org.im4java.core.Info;
import org.im4java.process.ArrayListOutputConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newroad.fileext.data.model.ThumbnailType.ThumbnailDict;
import com.newroad.fileext.utilities.FileResourceException;
import com.newroad.fileext.utilities.SystemProperties;

/**
 * @info
 * @author xupeng,tangzj1
 * @date Nov 22, 2013
 * @version
 */
public class ThumbnailDesigner {

  private static Logger logger = LoggerFactory.getLogger(ThumbnailDesigner.class);

  private static final String IMAGE_MAGICK_PATH = SystemProperties.imageMagickPath;
  private static final String GRAPHIC_MAGICK_PATH = SystemProperties.graphicMagickPath;
  private static final Long TUUMBNAIL_TASK_TIMEOUT = SystemProperties.thumbnailTaskTimeout;
  // private static final String IMAGE_MAGICK_PATH = "C:/Program Files/ImageMagick-6.8.9-Q16";
  // private static final String GRAPHIC_MAGICK_PATH = "C:/Program Files/GraphicsMagick-1.3.20-Q8";
  // private static final Long TUUMBNAIL_TASK_TIMEOUT = 30l;
  private static final String HOLDER_WIDTH = "width";
  private static final String HOLDER_HEIGHT = "height";
  private static final String EXT = ".jpg";
  private static final String THUMBNAIL_SUFFIX = "_" + HOLDER_WIDTH + "_" + HOLDER_HEIGHT;

  // 校验
  // private static final String[] LEGAL_SUFFIX = {"jpg", "png", "bmp", "gif",
  // "jpeg", "JPG", "PNG", "BMP", "GIF", "JPEG"};

  /**
   * 根据指定的尺寸,生成图片副本
   * 
   * @param width
   * @param height
   * @param srcFiles
   * @return
   */
  public List<String> resizeImages(int width, int height, String... srcFiles) {
    if (width <= 0) {
      throw new IllegalArgumentException("width must greater than zero.");
    }
    if (height <= 0) {
      throw new IllegalArgumentException("height must greater than zero.");
    }
    if (srcFiles == null || srcFiles.length == 0) {
      logger.error("The srcFiles is null or the size is zero in resizeImages method!");
      return null;
    }
    List<String> targetFilePath = new ArrayList<String>(srcFiles.length);

    // 命令行
    ConvertCmd cmd = new ConvertCmd();
    cmd.setSearchPath(IMAGE_MAGICK_PATH);
    cmd.setAsyncMode(false); // 同步模式

    // 命令
    IMOperation op = new IMOperation();
    op.thumbnail(width, height);
    op.addImage();
    op.addImage();

    // 执行
    for (String srcImage : srcFiles) {
      String dstFile = genFileNameSuffix(srcImage, width, height, EXT);
      try {
        if (dstFile != null) {
          cmd.run(op, srcImage, dstFile);
          targetFilePath.add(dstFile);
        }
      } catch (Exception e) {
        logger.error("bring exception when resizeImages:", e);
      }
    }
    return targetFilePath;
  }

  /**
   * 作为线程池任务,生成指定尺寸的图片副本
   * 
   * @param executor
   * @param width
   * @param height
   * @param srcFiles
   * @return
   */
  public List<String> resizeImages(Executor executor, final int width, final int height, final String... srcFiles) {
    if (executor == null) {
      throw new FileResourceException("resizeImages Executor is null.");
    }
    FutureTask<List<String>> task = new FutureTask<List<String>>(new Callable<List<String>>() {
      @Override
      public List<String> call() throws Exception {
        return resizeImages(width, height, srcFiles);
      }
    });
    executor.execute(task);
    try {
      if (TUUMBNAIL_TASK_TIMEOUT != null && TUUMBNAIL_TASK_TIMEOUT > 0) {
        return task.get(TUUMBNAIL_TASK_TIMEOUT, TimeUnit.SECONDS);
      }
      return task.get();
    } catch (Exception e) {
      task.cancel(true);
      logger.error("bring expcetion when resizeImages as task:", e);
    }
    return null;
  }

  /**
   * 生成新文件名
   * 
   * @param srcFileName
   * @param width
   * @param height
   * @return
   */
  private static String genFileNameSuffix(String srcFileName, int width, int height, String ext) {
    if (srcFileName == null) {
      return null;
    }
    String file = srcFileName;
    String suffix = THUMBNAIL_SUFFIX.replaceAll(HOLDER_WIDTH, String.valueOf(width)).replaceAll(HOLDER_HEIGHT, String.valueOf(height));
    int lastDot = file.lastIndexOf('.');
    if (lastDot == -1) {
      file += suffix + ext;
    } else {
      file = file.substring(0, lastDot) + suffix + ext;
    }
    return file;
  }

  /**
   * ImagcMagick生成缩略图
   * 
   * @param size
   * @param srcFiles
   * @return
   */
  public static List<String> makeIMThumbnail(final int size, boolean isAsync, final String... srcFiles) {
    if (size <= 0) {
      throw new IllegalArgumentException("width must greater than zero.");
    }
    if (srcFiles == null || srcFiles.length == 0) {
      logger.error("The srcFiles is null or the size is zero in makeIMThumbnail method!");
      return null;
    }
    List<String> targetPathList = new ArrayList<String>(srcFiles.length);

    // 命令行
    // For GraphicMagic
    // ConvertCmd cmd = new ConvertCmd(true);
    ConvertCmd cmd = new ConvertCmd();
    // String osName = System.getProperty("os.name").toLowerCase();
    // if (osName.indexOf("win") >= 0) {
    cmd.setSearchPath(IMAGE_MAGICK_PATH);
    // }
    cmd.setAsyncMode(isAsync); // 同步模式

    // 命令
    IMOperation op = null;
    // 执行
    for (String srcImage : srcFiles) {
      String dstFilePath = genFileNameSuffix(srcImage, size, size, EXT);
      int dot = srcImage.lastIndexOf('.');
      if (dot >= 0 && ".gif".equals(srcImage.substring(dot))) {
        srcImage += "[0]";
      }
      try {
        Info imgInfo = new Info(srcImage, true);
        int imgWidth = imgInfo.getImageWidth();
        int imgHeight = imgInfo.getImageHeight();
        int width = imgWidth;
        int height = imgHeight;

        op = new IMOperation();
        if (imgWidth < size && imgHeight < size) {
          op.thumbnail(width, height);
        } else {
          float x = imgWidth * 1.0F / imgHeight;
          if (x <= 1.1F && x >= 0.9F) {
            // 按长边=size缩小,返回
            if (x >= 1) {// 宽 >= 高
              op.thumbnail(size, (int) (size / x));
            } else {// 宽 < 高
              op.thumbnail((int) (size * x), size);
            }
          } else {
            if (imgWidth > size && imgHeight > size) {
              // 按短边=size缩小,截取
              if (x >= 1) { // 宽 >= 高
                op.thumbnail((int) (size * x), size);
              } else { // 宽 < 高
                op.thumbnail(size, (int) (size / x));
              }
              op.crop(size, size, 0, 0);
            } else {
              // 有一边<size时,不缩小
              if (imgWidth < size) {
                op.crop(imgWidth, size, 0, 0);
              } else {
                op.crop(size, imgHeight, 0, 0);
              }
            }
            // 中心截取 size X size + 0 + 0
            op.gravity("center");
          }
        }
        op.addImage();
        op.addImage();
        if (dstFilePath != null) {
          cmd.run(op, srcImage, dstFilePath);
          File dstFile = new File(dstFilePath);
          if (isAsync) {
            targetPathList.add(dstFilePath);
          } else {
            if (dstFile.exists()) {
              targetPathList.add(dstFilePath);
            } else {
              logger.error("Fail to make thumbnail using imageMagic!");
            }
          }
        }
      } catch (Exception e) {
        logger.error("throw exception when making Thumbnail:", e);
      }
    }
    return targetPathList;
  }


  /**
   * GraphicMagick生成缩略图
   * 
   * @param size
   * @param srcFiles
   * @return
   */
  public static List<String> batchMakeThumbnail(boolean isAsync, final int thumbnailWidth, final String... srcFiles) {
    if (srcFiles == null || srcFiles.length == 0) {
      logger.error("Fail to get source file to make Thumbnail because the srcFiles is null or the size is zero!");
      return null;
    }
    if (thumbnailWidth <= 0) {
      logger.error("Thumbnail width for source file " + Arrays.toString(srcFiles) + " must greater than zero!");
      return null;
    }
    int size = thumbnailWidth;
    List<String> targetPathList = new ArrayList<String>(srcFiles.length);

    // 命令行
    // For GraphicMagic
    // ConvertCmd cmd = new ConvertCmd(true);
    ConvertCmd cmd = new ConvertCmd(true);
    // String osName = System.getProperty("os.name").toLowerCase();
    // if (osName.indexOf("win") >= 0) {
    cmd.setSearchPath(GRAPHIC_MAGICK_PATH);
    // }
    cmd.setAsyncMode(isAsync); // 同步模式

    // 执行
    for (String srcImage : srcFiles) {
      String dstFilePath = genFileNameSuffix(srcImage, size, size, EXT);
      if (dstFilePath == null) {
        logger.error("Fail to get destination file to make Thumbnail because the destFilePath is null!");
        continue;
      }
      int dot = srcImage.lastIndexOf('.');
      if (dot >= 0 && ".gif".equals(srcImage.substring(dot))) {
        srcImage += "[0]";
      }
      try {
        Map<String, Object> imgInfo = getImageInfo(srcImage);
        if (imgInfo == null) {
          logger.error("Fail to get source file info in the path " + srcImage + "!");
          continue;
        }
        int imgWidth = Integer.valueOf((String) imgInfo.get("width"));
        int imgHeight = Integer.valueOf((String) imgInfo.get("height"));

        IMOperation op = new IMOperation();
        if (imgWidth < size && imgHeight < size) {
          op.resize(imgWidth, imgHeight);
        } else {
          float x = imgWidth * 1.0F / imgHeight;
          op.gravity("Center"); // GraphicMagick gravity must be set before crop.
          if (x <= 1.1F && x >= 0.9F) {
            // 按长边=size缩小,返回
            if (x >= 1) {// 宽 >= 高
              op.resize(size, (int) (size / x));
            } else {// 宽 < 高
              op.resize((int) (size * x), size);
            }
          } else {
            if (imgWidth > size && imgHeight > size) {
              // 按短边=size缩小,截取
              if (x >= 1) { // 宽 >= 高
                op.resize((int) (size * x), size);
              } else { // 宽 < 高
                op.resize(size, (int) (size / x));
              }
              op.crop(size, size, 0, 0);
            } else {
              // 有一边<size时,不缩小
              if (imgWidth < size) {
                op.crop(imgWidth, size, 0, 0);
              } else {
                op.crop(size, imgHeight, 0, 0);
              }
            }
          }
        }
        op.addImage(srcImage);
        op.addImage(dstFilePath);

        cmd.run(op);
        File dstFile = new File(dstFilePath);
        if (isAsync) {
          targetPathList.add(dstFilePath);
        } else {
          if (dstFile.exists()) {
            targetPathList.add(dstFilePath);
          } else {
            logger.error("Fail to make thumbnail using GraphicMagic!");
          }
        }
      } catch (Exception e) {
        logger.error("throw exception when making Thumbnail:", e);
      }
    }
    return targetPathList;
  }

  /**
   * GraphicMagick生成缩略图
   * 
   * @param srcFile
   * @param thumbnailWidth
   * @param thumbnailHeight
   * @return
   */
  public static String makeThumbnail(String srcFilePath, boolean isAsync, Integer thumbnailWidth, Integer thumbnailHeight) {
    File srcFile = new File(srcFilePath);
    if (srcFilePath == null || !srcFile.exists()) {
      logger.error("Fail to get source file to make Thumbnail in the path " + srcFilePath + "!");
      return null;
    }
    if ((thumbnailWidth == null || thumbnailWidth <= 0) && (thumbnailHeight == null || thumbnailHeight <= 0)) {
      logger.error("Thumbnail width and height for source file " + srcFilePath + " must greater than zero!");
      return null;
    } else if (thumbnailWidth == null || thumbnailWidth <= 0) {
      thumbnailWidth = thumbnailHeight;
    } else if (thumbnailHeight == null || thumbnailHeight <= 0) {
      thumbnailHeight = thumbnailWidth;
    }

    String dstFilePath = genFileNameSuffix(srcFilePath, thumbnailWidth, thumbnailHeight, EXT);
    if (dstFilePath == null) {
      logger.error("Fail to get destination file to make Thumbnail because the destFilePath is null!");
      return null;
    }
    int dot = srcFilePath.lastIndexOf('.');
    if (dot >= 0 && ".gif".equals(srcFilePath.substring(dot))) {
      srcFilePath += "[0]";
    }

    try {
      Info imgInfo = new Info(srcFilePath, true);
      int imgWidth = imgInfo.getImageWidth();
      int imgHeight = imgInfo.getImageHeight();

      float thumbnailProp = thumbnailWidth * 1.0F / thumbnailHeight;
      float sourceProp = imgWidth * 1.0F / imgHeight;
      Float compare = sourceProp / thumbnailProp;

      IMOperation op = new IMOperation();
      // op.quality(80.0);
      if (imgWidth < thumbnailWidth && imgHeight < thumbnailHeight) {
        op.resize(imgWidth, imgHeight);
      } else {
        op.gravity("Center"); // GraphicMagick gravity must be set before crop.
        if (compare <= 1.1F && compare >= 0.9F) {
          // 按长边=size缩小,返回
          if (compare >= 1) {// 宽 >= 高
            op.resize(thumbnailWidth, (int) (thumbnailHeight / compare));
          } else {// 宽 < 高
            op.resize((int) (thumbnailWidth * compare), thumbnailHeight);
          }
        } else {
          if (imgWidth > thumbnailWidth && imgHeight > thumbnailHeight) {
            // 按短边=size缩小,截取
            if (compare >= 1) { // 宽 >= 高
              op.resize((int) (thumbnailWidth * compare), thumbnailHeight);
            } else { // 宽 < 高
              op.resize(thumbnailWidth, (int) (thumbnailHeight / compare));
            }
            op.crop(thumbnailWidth, thumbnailHeight, 0, 0);
          } else if (imgWidth <= thumbnailWidth && imgHeight > thumbnailHeight) {
            // 有一边<size时,不缩小
            op.crop(imgWidth, thumbnailHeight, 0, 0);
          } else if (imgWidth > thumbnailWidth && imgHeight <= thumbnailHeight) {
            op.crop(thumbnailWidth, imgHeight, 0, 0);
          }
        }
      }
      op.addImage();
      op.addImage();
      // 命令行
      // For GraphicMagic
      ConvertCmd cmd = new ConvertCmd(true);
      cmd.setSearchPath(GRAPHIC_MAGICK_PATH);
      cmd.setAsyncMode(isAsync); // 同步模式
      cmd.run(op, srcFilePath, dstFilePath);
      File dstFile = new File(dstFilePath);
      if (isAsync) {
        return dstFilePath;
      } else {
        if (dstFile.exists()) {
          return dstFilePath;
        } else {
          logger.error("Fail to make thumbnail in the path " + dstFile + " using GraphicMagic!");
        }
      }
    } catch (Exception e) {
      logger.error("throw exception when making Thumbnail:", e);
    }
    return null;
  }

  /**
   * GraphicMagick生成缩略图
   * 
   * @param size
   * @param srcFiles
   * @return
   */
  public static List<String> makeMultipleThumbnails(final String srcFile, boolean isAsync, ThumbnailDict... dictTypes) {
    if (srcFile == null) {
      logger.error("Fail to get source file to make multiple thumbnail because the srcFile is null!");
      return null;
    }
    List<String> targetPaths = new ArrayList<String>(dictTypes.length);
    for (ThumbnailDict dict : dictTypes) {
      String targetPath = makeThumbnail(srcFile, isAsync, dict.getDictWidth(), dict.getDictHeight());
      targetPaths.add(targetPath);
    }
    return targetPaths;
  }

  public static Map<String, Object> getImageInfo(String srcImage) {
    Map<String, Object> map = null;
    try {
      IMOperation op = new IMOperation();
      op.format("width:%w,height:%h,path:%d%f,size:%b%[EXIF:DateTimeOriginal]");
      op.addImage(1);
      IdentifyCmd identify = new IdentifyCmd(true);
      ArrayListOutputConsumer output = new ArrayListOutputConsumer();
      identify.setOutputConsumer(output);
      identify.run(op, srcImage);
      ArrayList<String> cmdOutput = output.getOutput();
      String[] line = cmdOutput.get(0).split(",");

      map = new HashMap<String, Object>(line.length);
      for (String argument : line) {
        String[] arguLine = argument.split(":");
        map.put(arguLine[0], arguLine[1]);
      }
    } catch (Exception e) {
      logger.error("getImageInfo Exception in the path " + srcImage + ":", e);
    }
    return map;
  }

  public static void main(String[] args) {
    // getImageInfo("D://3.jpg");
    System.out.println(System.getProperty("java.library.path"));
    long start = System.currentTimeMillis();
    makeIMThumbnail(200, false, "D:/1_im.gif");
    long end = System.currentTimeMillis() - start;
    System.out.println("ImageMagick time:" + end);
    long start2 = System.currentTimeMillis();
    makeThumbnail("D:/1_gm.gif", false, 200, 200);
    long end2 = System.currentTimeMillis() - start2;
    System.out.println("GraphicMagick time:" + end2);
    // makeMultipleThumbnails("D:/3.jpg", true, ThumbnailDict.PHONE_H640, ThumbnailDict.PHONE_H768,
    // ThumbnailDict.PHONE_H1152,
    // ThumbnailDict.PHONE_H1536);
  }
}
