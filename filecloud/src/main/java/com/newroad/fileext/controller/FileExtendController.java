package com.newroad.fileext.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadBase.InvalidContentTypeException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newroad.fileext.data.model.CacheFileData;
import com.newroad.fileext.data.model.CloudFileData;
import com.newroad.fileext.data.model.FileMimeType;
import com.newroad.fileext.data.model.ThumbnailType;
import com.newroad.fileext.data.model.ThumbnailType.ThumbnailDict;
import com.newroad.fileext.filter.TokenAuthFilter;
import com.newroad.fileext.service.FileExtendRestClient;
import com.newroad.fileext.service.api.FileExtendServiceIf;
import com.newroad.fileext.service.cloud.CloudManageService;
import com.newroad.fileext.utilities.FileDataConstant;
import com.newroad.util.StringHelper;
import com.newroad.util.apiresult.ApiReturnObjectUtil;
import com.newroad.util.apiresult.ServiceResult;
import com.newroad.util.auth.TokenUtil;
import com.newroad.util.iohandler.FileHelper;
import com.newroad.util.iohandler.TypeConvertor;

@Controller
@RequestMapping("/v{apiVersion}/extend")
public class FileExtendController {

  private static Logger logger = LoggerFactory.getLogger(FileExtendController.class);

  @Value("${FILE_MAXSIZE}")
  private Long fileMaxSize;

  @Autowired
  private FileExtendServiceIf fileExtendService;

  @Autowired
  private CloudManageService cloudManageService;

  @Autowired
  private FileExtendRestClient restClient;

  @RequestMapping(value = "/getCosConnector", produces = FileDataConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String getCosConnector(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    try {
      JSONObject param = StringHelper.getRequestEntity(request);
      if (param.containsKey("error")) {
        return ApiReturnObjectUtil.getReturnObjFromException(new Exception("Parametes Error")).toString();
      }
      String realm = (String) param.get(FileDataConstant.REALM);
      String lenovoST = (String) param.get(FileDataConstant.LENOVO_ST);
      return cloudManageService.getConnector(realm, lenovoST);
    } catch (Exception e) {
      logger.error("Get Cos Connector Exception!", e);
      return ApiReturnObjectUtil.getReturnObjFromException(e).toString();
    }
  }

  @RequestMapping(value = "/getCloudToken", produces = FileDataConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String getCloudToken(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    try {
      JSONObject session = TokenAuthFilter.getCurrent();
      if(session==null){
        return ApiReturnObjectUtil.getReturnObjFromException(new Exception("Session is null")).toString();
      }
      return cloudManageService.getCloudToken(session).toString();
    } catch (Exception e) {
      logger.error("Get Cloud Token Exception!", e);
      return ApiReturnObjectUtil.getReturnObjFromException(e).toString();
    }
  }

  @RequestMapping(value = "/listObjectNumber", produces = FileDataConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String listObjectNumber(HttpServletRequest request, @PathVariable String apiVersion) throws Exception {
    try {
      JSONObject session = StringHelper.getRequestEntity(request);
      int objectNum = cloudManageService.listObjectNumber(session);
      return String.valueOf(objectNum);
    } catch (Exception e) {
      logger.error("List Object Number Exception!", e);
      return ApiReturnObjectUtil.getReturnObjFromException(e).toString();
    }
  }

  /**
   * 上传文件附件
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/uploadFile")
  public @ResponseBody
  String uploadFile(HttpServletRequest request, @PathVariable String apiVersion) {
    boolean isMultipart = ServletFileUpload.isMultipartContent(request);
    if (!isMultipart) {
      logger.error(FileDataConstant.FileStatus.NOFILE.toString());
      return null;
    }
    Map<String, Object> params = new TreeMap<String, Object>();
    JSONObject session = TokenAuthFilter.getCurrent();

    ServletFileUpload sfu = new ServletFileUpload();
    sfu.setFileSizeMax(fileMaxSize);
    sfu.setHeaderEncoding("utf-8");
    List<CloudFileData> fileMapList = new ArrayList<CloudFileData>(1);
    try {
      FileItemIterator fileItemIter = sfu.getItemIterator(request);
      // Iterator<FileItem> iter = fileItemList.iterator();
      while (fileItemIter.hasNext()) {
        FileItemStream item = fileItemIter.next();
        if (!item.isFormField()) {
          CloudFileData fileData = processUploadedFile(item);
          //String responseCapacity = restClient.checkUserCapacity((Integer) fileData.getCacheFileData().getCacheSize().intValue());
          //logger.info("User Capacity info:" + responseCapacity);
          //if (responseCapacity != null) {
          //  return responseCapacity;
          //}
          fileMapList.add(fileData);
        }
      }
      params.put(FileDataConstant.FILE_FIELD, fileMapList);
      return fileExtendService.uploadData(session, params).toString();
    } catch (SizeLimitExceededException e) {
      logger.error("Upload File " + FileDataConstant.FileStatus.SIZE.getMessage() + " Exception!", e);
      return ApiReturnObjectUtil.getReturnObjFromException(e).toString();
    } catch (InvalidContentTypeException e) {
      logger.error("Upload File " + FileDataConstant.FileStatus.ENTYPE.getMessage() + " Exception!", e);
      return ApiReturnObjectUtil.getReturnObjFromException(e).toString();
    } catch (FileUploadException e) {
      logger.error("Upload File " + FileDataConstant.FileStatus.REQUEST.getMessage() + " Exception!", e);
      return ApiReturnObjectUtil.getReturnObjFromException(e).toString();
    } catch (Exception e) {
      logger.error("Upload File " + FileDataConstant.FileStatus.UNKNOWN.getMessage() + " Exception!", e);
      return ApiReturnObjectUtil.getReturnObjFromException(e).toString();
    }
  }

  /**
   * 上传文件附件
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/saveMessageResource")
  public @ResponseBody
  String saveMessageResource(HttpServletRequest request, @PathVariable String apiVersion, HttpServletResponse response) {
    String token = request.getHeader(TokenUtil.TOKEN);
    if ("".equals(token) || !"oms".equalsIgnoreCase(token)) {
      logger.error("No AuthToken in Http head!");
      return "{returnCode:401, returnMessage:'No AuthToken in Http head!'}";
    }

    boolean isMultipart = ServletFileUpload.isMultipartContent(request);
    if (!isMultipart) {
      logger.error(FileDataConstant.FileStatus.NOFILE.toString());
      return null;
    }
    Map<String, Object> params = new TreeMap<String, Object>();

    ServletFileUpload sfu = new ServletFileUpload();
    sfu.setFileSizeMax(fileMaxSize);
    sfu.setHeaderEncoding("utf-8");
    List<CloudFileData> fileMapList = new ArrayList<CloudFileData>(1);
    try {
      FileItemIterator fileItemIter = sfu.getItemIterator(request);
      // Iterator<FileItem> iter = fileItemList.iterator();
      while (fileItemIter.hasNext()) {
        FileItemStream item = fileItemIter.next();
        if (!item.isFormField()) {
          CloudFileData fileData = processUploadedFile(item);
          fileMapList.add(fileData);
        }
      }
      params.put(FileDataConstant.FILE_FIELD, fileMapList);
      String resultContent = fileExtendService.publishInformation(params).toString();
      return resultContent;
    } catch (SizeLimitExceededException e) {
      logger.error("saveMessageResource " + FileDataConstant.FileStatus.SIZE.getMessage() + " Exception!", e);
      return ApiReturnObjectUtil.getReturnObjFromException(e).toString();
    } catch (InvalidContentTypeException e) {
      logger.error("saveMessageResource " + FileDataConstant.FileStatus.ENTYPE.getMessage() + " Exception!", e);
      return ApiReturnObjectUtil.getReturnObjFromException(e).toString();
    } catch (FileUploadException e) {
      logger.error("saveMessageResource " + FileDataConstant.FileStatus.REQUEST.getMessage() + " Exception!", e);
      return ApiReturnObjectUtil.getReturnObjFromException(e).toString();
    } catch (Exception e) {
      logger.error("saveMessageResource " + FileDataConstant.FileStatus.UNKNOWN.getMessage() + " Exception!", e);
      return ApiReturnObjectUtil.getReturnObjFromException(e).toString();
    }
  }

  /**
   * 下载文件附件
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @SuppressWarnings("rawtypes")
  @RequestMapping(value = "/downloadFile", produces = FileDataConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String downloadFile(HttpServletRequest request, @PathVariable String apiVersion, HttpServletResponse response) {
    JSONObject param = getFileInfo(request);
    if (param == null) {
      return ApiReturnObjectUtil.getReturnObjFromException(new Exception("Parametes Error")).toString();
    }
    JSONObject session = TokenAuthFilter.getCurrent();
    try {
      ServiceResult serviceResult = fileExtendService.downloadData(session, param);
      if (serviceResult.checkOK()) {
        JSONObject resultParam = (JSONObject) serviceResult.getBusinessResult();
        String localfileName = (String) resultParam.get(FileDataConstant.FILE_NAME);
        String localContentType = (String) resultParam.get(FileDataConstant.FILE_CONTENT_TYPE);
        String tempFilePath = (String) resultParam.get(FileDataConstant.TEMP_FILE_PATH);
        if (tempFilePath != null) {
          File tempFile = new File(tempFilePath);
          if (tempFile.exists()) {
            if (request.getHeader("User-Agent") != null) {
              localfileName = FileHelper.encodingFileName(request.getHeader("User-Agent"), localfileName);
            }
            outputResponse(tempFile, localContentType, localfileName, response);
          }
        }
      }
      return serviceResult.toString();
    } catch (Exception e) {
      logger.error("Download File Exception!", e);
      return ApiReturnObjectUtil.getReturnObjFromException(e).toString();
    }
  }

  /**
   * 下载文件附件
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @SuppressWarnings("rawtypes")
  @RequestMapping(value = "/thumbnail", produces = FileDataConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String thumbnail(HttpServletRequest request, @PathVariable String apiVersion, HttpServletResponse response) {
    JSONObject param = getFileInfo(request);
    if (param == null) {
      return ApiReturnObjectUtil.getReturnObjFromException(new Exception("Parametes Error")).toString();
    }

    JSONObject session = TokenAuthFilter.getCurrent();
    try {
      ServiceResult serviceResult = null;
      ThumbnailType thumbnailType = null;
      if (param.get(FileDataConstant.THUMBNAIL_SCALE) == null) {
        thumbnailType = ThumbnailType.getThumbnailDictType(ThumbnailDict.WEB_DEFAULT_TYPE);
        serviceResult = fileExtendService.getThumbnail(session, param, thumbnailType);
      } else {
        String scale = (String) param.get(FileDataConstant.THUMBNAIL_SCALE);
        String[] sacleArray = scale.split("x");
        thumbnailType = new ThumbnailType(Integer.valueOf(sacleArray[0]), Integer.valueOf(sacleArray[1]));
        //serviceResult = fileExtendService.getThumbnail(session, param, thumbnailType);
        serviceResult = fileExtendService.getCloudThumbnail(session, param, thumbnailType);
      }
      if (serviceResult.checkOK()) {
        JSONObject resultParam = (JSONObject) serviceResult.getBusinessResult();
        String localfileName = (String) resultParam.get(FileDataConstant.FILE_NAME);
        String localContentType = (String) resultParam.get(FileDataConstant.FILE_CONTENT_TYPE);
        String tempFilePath = (String) resultParam.get(FileDataConstant.TEMP_FILE_PATH);
        if (tempFilePath != null) {
          File tempFile = new File(tempFilePath);
          if (tempFile.exists()) {
            if (request.getHeader("User-Agent") != null) {
              localfileName = FileHelper.encodingFileName(request.getHeader("User-Agent"), localfileName);
            }
            outputResponse(tempFile, localContentType, localfileName, response);
          }
        }
      }
      return serviceResult.toString();
    } catch (Exception e) {
      logger.error("Thumbnail File Exception!", e);
      return ApiReturnObjectUtil.getReturnObjFromException(e).toString();
    }
  }

  /**
   * 下载文件附件
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @SuppressWarnings("rawtypes")
  @RequestMapping(value = "/fileIcon", produces = FileDataConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String fileIcon(HttpServletRequest request, @PathVariable String apiVersion, HttpServletResponse response) {
    JSONObject param = getFileInfo(request);
    if (param == null) {
      return ApiReturnObjectUtil.getReturnObjFromException(new Exception("Parametes Error")).toString();
    }
    String agent = request.getHeader("User-Agent");

    JSONObject session = TokenAuthFilter.getCurrent();
    try {
      ServiceResult serviceResult =
          fileExtendService.getThumbnail(session, param, ThumbnailType.getThumbnailDictType(ThumbnailDict.WEB_ICON_TYPE));
      if (serviceResult.checkOK()) {
        JSONObject resultParam = (JSONObject) serviceResult.getBusinessResult();
        String localfileName = (String) resultParam.get(FileDataConstant.FILE_NAME);
        String localContentType = (String) resultParam.get(FileDataConstant.FILE_CONTENT_TYPE);
        String tempFilePath = (String) resultParam.get(FileDataConstant.TEMP_FILE_PATH);
        if (tempFilePath != null) {
          File tempFile = new File(tempFilePath);
          if (tempFile.exists()) {
            outputResponse(tempFile, localContentType, FileHelper.encodingFileName(agent, localfileName), response);
          }
        }
      }
      return serviceResult.toString();
    } catch (Exception e) {
      logger.error("Download File Exception!", e);
      return ApiReturnObjectUtil.getReturnObjFromException(e).toString();
    }
  }

  /**
   * delete File
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/deleteFile", produces = FileDataConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String deleteFile(HttpServletRequest request, @PathVariable String apiVersion, HttpServletResponse response) {
    // JSON Request format
    JSONObject param = StringHelper.getRequestEntity(request);
    JSONObject session = TokenAuthFilter.getCurrent();
    try {
      return fileExtendService.deleteData(session, param).toString();
    } catch (Exception e) {
      logger.error("Delete File Exception!", e);
      return ApiReturnObjectUtil.getReturnObjFromException(e).toString();
    }
  }

  /**
   * batch create public link File
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/batchCreatePublicLink", produces = FileDataConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String batchCreatePublicLink(HttpServletRequest request, @PathVariable String apiVersion, HttpServletResponse response) {
    // JSON Request format
    JSONObject param = StringHelper.getRequestEntity(request);
    JSONObject session = TokenAuthFilter.getCurrent();
    try {
      return fileExtendService.batchCreatePublicLink(session, param).toString();
    } catch (Exception e) {
      logger.error("Batch Create Public Link Exception!", e);
      return ApiReturnObjectUtil.getReturnObjFromException(e).toString();
    }
  }

  /**
   * delete public link
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/deletePublicLink", produces = FileDataConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String deletePublicLink(HttpServletRequest request, @PathVariable String apiVersion, HttpServletResponse response) {
    JSONObject param = StringHelper.getRequestEntity(request);
    JSONObject session = TokenAuthFilter.getCurrent();
    try {
      return fileExtendService.deletePublicLink(session, param).toString();
    } catch (Exception e) {
      logger.error("Delete Public Link Exception!", e);
      return ApiReturnObjectUtil.getReturnObjFromException(e).toString();
    }
  }

  /**
   * share kk file
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/getShareFile", produces = FileDataConstant.CONTENT_TYPE_JSON)
  public @ResponseBody
  String getShareFile(HttpServletRequest request, @PathVariable String apiVersion, HttpServletResponse response) {
    JSONObject param = getFileInfo(request);
    if (param == null) {
      return ApiReturnObjectUtil.getReturnObjFromException(new Exception("Parametes Error")).toString();
    }
    String agent = request.getHeader("User-Agent");

    try {
      ServiceResult<JSONObject> serviceResult = fileExtendService.getShareResource(param);
      if (serviceResult.checkOK()) {
        JSONObject resultParam = serviceResult.getBusinessResult();
        String localfileName = (String) resultParam.get(FileDataConstant.FILE_NAME);
        String localContentType = (String) resultParam.get(FileDataConstant.FILE_CONTENT_TYPE);
        String tempFilePath = (String) resultParam.get(FileDataConstant.TEMP_FILE_PATH);
        File tempFile = new File(tempFilePath);
        outputResponse(tempFile, localContentType, FileHelper.encodingFileName(agent, localfileName), response);
      }
      return serviceResult.toString();
    } catch (Exception e) {
      logger.error("Get Share File (for Audio) Exception!", e);
      return ApiReturnObjectUtil.getReturnObjFromException(e).toString();
    }
  }

  private CloudFileData processUploadedFile(FileItemStream item) throws IOException {
    String name = item.getName();
    String contentType = item.getContentType();
    String keyId = CloudFileData.generateKeyId();
    int extIndex = name.lastIndexOf(".");
    if (extIndex > 0) {
      String ext = name.substring(extIndex);
      contentType = FileMimeType.getContentType(ext);
    }
    InputStream is = item.openStream();
    byte[] fileByte = TypeConvertor.inputStream2byte(is);
    Integer size = (Integer) fileByte.length;
    is.close();
    logger.info("Upload File " + keyId + ",contentType:" + contentType + ",size(byte):" + size);
    CloudFileData cloudFile = new CloudFileData();
    cloudFile.setKey(keyId);
    cloudFile.setFileName(name);
    cloudFile.setContentType(contentType);
    cloudFile.setCacheFileData(new CacheFileData(fileByte,Long.valueOf(size)));
    return cloudFile;
  }

  private JSONObject getFileInfo(HttpServletRequest request) {
    String clientID = request.getParameter("clientResourceID");
    String fileName = request.getParameter("fileName");
    String publicLink = request.getParameter("publicLink");
    String scale = request.getParameter("scale");
    logger.info("Get file clientID:" + clientID);
    if (clientID == null) {
      return null;
    }
    JSONObject param = new JSONObject();
    param.put(FileDataConstant.CLIENT_RESOURCE_ID, clientID);
    param.put(FileDataConstant.FILE_NAME, fileName);
    if (publicLink != null) {
      publicLink = FileDataConstant.PUBLIC_LINK_TAG + publicLink;
      param.put(FileDataConstant.RESOURCE_PUBLIC_LINK, publicLink);
    }
    if (scale != null) {
      param.put(FileDataConstant.THUMBNAIL_SCALE, scale);
    }
    return param;
  }

  // Make sure http response could send the file byte correctly.
  private void outputResponse(File tempFile, String contentType, String fileName, HttpServletResponse response) throws Exception {
    InputStream bis = new BufferedInputStream(new FileInputStream(tempFile));
    OutputStream bos = null;
    OutputStream ros = null;
    try {
      response.reset();
      response.setCharacterEncoding("utf-8");
      response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
      response.setHeader("Content-Length", "" + tempFile.length());
      response.setContentType(contentType);

      ros = response.getOutputStream();
      bos = new BufferedOutputStream(ros);
      byte[] buff = new byte[10 * 1024];
      int bytesRead;
      while (-1 != (bytesRead = bis.read(buff))) {
        bos.write(buff, 0, bytesRead);
        bos.flush();
      }
    } finally {
      try {
        if (bos != null) {
          bos.close();
        }
        if (ros != null) {
          ros.close();
        }
        if (bis != null) {
          bis.close();
        }
      } catch (IOException e) {
        logger.error("Close Stream exception:" + e);
      }
    }
  }
}
