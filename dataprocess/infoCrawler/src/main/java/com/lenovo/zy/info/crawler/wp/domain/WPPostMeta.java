package com.lenovo.zy.info.crawler.wp.domain;

public class WPPostMeta {
    private Long id;
    private Long postId;
    private String metaKey;
    private String metaValue;
    
    
    public WPPostMeta(Long postId, String metaKey, String metaValue) {
      super();
      this.postId = postId;
      this.metaKey = metaKey;
      this.metaValue = metaValue;
    }
    public Long getId() {
      return id;
    }
    public void setId(Long id) {
      this.id = id;
    }
    public Long getPostId() {
      return postId;
    }
    public void setPostId(Long postId) {
      this.postId = postId;
    }
    public String getMetaKey() {
      return metaKey;
    }
    public void setMetaKey(String metaKey) {
      this.metaKey = metaKey;
    }
    public String getMetaValue() {
      return metaValue;
    }
    public void setMetaValue(String metaValue) {
      this.metaValue = metaValue;
    }
    
    
    
}
