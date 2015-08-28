package com.lenovo.zy.info.crawler.dao;

import java.util.List;

import com.lenovo.zy.info.crawler.domain.Product;

public interface ProductDaoIf {
  
  public int insertProduct(Product product);
  
  public List<Product> selectProductList();
}
