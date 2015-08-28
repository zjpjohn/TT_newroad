package com.lenovo.zy.info.crawler.domain;

public class ProductBanner {
  
    private Long productBannerId;
    
    private Long productId;
    
    private Long photoId;

    
    public ProductBanner(Long productId, Long photoId) {
      super();
      this.productId = productId;
      this.photoId = photoId;
    }

    public Long getProductBannerId() {
      return productBannerId;
    }

    public void setProductBannerId(Long productBannerId) {
      this.productBannerId = productBannerId;
    }

    public Long getProductId() {
      return productId;
    }

    public void setProductId(Long productId) {
      this.productId = productId;
    }

    public Long getPhotoId() {
      return photoId;
    }

    public void setPhotoId(Long photoId) {
      this.photoId = photoId;
    }
    
    
}
