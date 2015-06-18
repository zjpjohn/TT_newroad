package com.newroad.fileext.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.newroad.fileext.service.api.FileResourceServiceIf;
import com.newroad.fileext.utilities.FileDataConstant;
import com.newroad.util.apiresult.ApiReturnObjectUtil;
import com.newroad.util.iohandler.TypeConvertor;

@Controller
@RequestMapping("/v{apiVersion}/resource")
public class FileResourceController {
  
  private static Logger logger = LoggerFactory.getLogger(FileResourceController.class);
  
  @Value("${FILE_MAXSIZE}")
  private Long fileMaxSize;
  
  @Autowired
  private FileResourceServiceIf fileResourceService;


  /**
   * 上传文件附件
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/upload")
  public @ResponseBody
  String uploadFileData(HttpServletRequest request, @PathVariable String apiVersion) {
    boolean isMultipart = ServletFileUpload.isMultipartContent(request);
    if (!isMultipart) {
      logger.error(FileDataConstant.FileStatus.NOFILE.toString());
      return null;
    }
//    JSONObject session = TokenAuthFilter.getCurrent();

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
      return fileResourceService.uploadCloudFiles(fileMapList).toString();
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
    cloudFile.setKeyId(keyId);
    cloudFile.setFileName(name);
    cloudFile.setContentType(contentType);
    cloudFile.setCacheFileData(new CacheFileData(fileByte,Long.valueOf(size)));
    return cloudFile;
  }
  
  public void setFileResourceService(FileResourceServiceIf fileResourceService) {
    this.fileResourceService = fileResourceService;
  }
}
