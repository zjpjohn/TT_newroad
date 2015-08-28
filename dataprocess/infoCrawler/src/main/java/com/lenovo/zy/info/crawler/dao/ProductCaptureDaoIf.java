package com.lenovo.zy.info.crawler.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lenovo.zy.info.crawler.domain.ProductCapture;

public interface ProductCaptureDaoIf {

  public Integer countProductCaptures(@Param("status") int status);

  public ProductCapture selectOneProductCapture(@Param("captureId") long captureId);
  
  public List<ProductCapture> selectProductCaptures(@Param("status") int status, @Param("offset") int offset, @Param("limit") int limit);
  
  public Integer updateProductCaptureStatus(@Param("status") int status, @Param("id") long id);
}
