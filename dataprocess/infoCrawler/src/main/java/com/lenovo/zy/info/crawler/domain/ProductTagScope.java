package com.lenovo.zy.info.crawler.domain;

public class ProductTagScope {

  private Long tagScopeId;
  
  private Long productId;
  
  private Long tagId;

  public ProductTagScope(Long productId, Long tagId) {
    super();
    this.productId = productId;
    this.tagId = tagId;
  }

  public Long getTagScopeId() {
    return tagScopeId;
  }

  public void setTagScopeId(Long tagScopeId) {
    this.tagScopeId = tagScopeId;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public Long getTagId() {
    return tagId;
  }

  public void setTagId(Long tagId) {
    this.tagId = tagId;
  }
  
  
  
}
