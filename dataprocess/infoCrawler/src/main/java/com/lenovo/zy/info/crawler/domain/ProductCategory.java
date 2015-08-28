package com.lenovo.zy.info.crawler.domain;

public class ProductCategory {

    private Long productCategoryId;
    
    private Long categoryId;
    
    private Long productId;
    
    public ProductCategory(Long categoryId, Long productId) {
      super();
      this.categoryId = categoryId;
      this.productId = productId;
    }

    public Long getProductCategoryId() {
      return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
      this.productCategoryId = productCategoryId;
    }

    public Long getCategoryId() {
      return categoryId;
    }

    public void setCategoryId(Long categoryId) {
      this.categoryId = categoryId;
    }

    public Long getProductId() {
      return productId;
    }

    public void setProductId(Long productId) {
      this.productId = productId;
    }
    
    
}
