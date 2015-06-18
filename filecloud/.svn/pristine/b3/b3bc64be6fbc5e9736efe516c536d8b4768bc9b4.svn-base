package com.newroad.fileext.data.model;

import java.io.Serializable;

public class ThumbnailType implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 2332005045892492268L;

  public enum ThumbnailDict {
    // TYPE(weight,height),
    WEB_DEFAULT_TYPE(94, 94), WEB_ICON_TYPE(266, 76), PHONE_H1536(413, 299), PHONE_H1152(309, 224), PHONE_H768(206, 149), PHONE_H640(172,
        125);

    private Integer dictWidth;
    private Integer dictHeight;

    private ThumbnailDict(Integer dictWidth, Integer dictHeight) {
      this.dictWidth = dictWidth;
      this.dictHeight = dictHeight;
    }

    public Integer getDictWidth() {
      return dictWidth;
    }

    public Integer getDictHeight() {
      return dictHeight;
    }

  }

  private Integer width;
  private Integer height;
  private ThumbnailDict dictType = null;

  public ThumbnailType(Integer width, Integer height) {
    this.width = width;
    this.height = height;
    for (ThumbnailDict dict : ThumbnailDict.values()) {
      if (width.equals(dict.getDictWidth()) && height.equals(dict.getDictHeight())) {
        this.dictType = dict;
      }
    }
  }

  public static ThumbnailType getThumbnailDictType(ThumbnailDict dict) {
    return new ThumbnailType(dict.getDictWidth(), dict.getDictHeight());
  }

  public Integer getWidth() {
    return width;
  }

  public void setWidth(Integer width) {
    this.width = width;
  }

  public Integer getHeight() {
    return height;
  }

  public void setHeight(Integer height) {
    this.height = height;
  }

  public ThumbnailDict getDictType() {
    return dictType;
  }
  
  

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((dictType == null) ? 0 : dictType.hashCode());
    result = prime * result + ((height == null) ? 0 : height.hashCode());
    result = prime * result + ((width == null) ? 0 : width.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ThumbnailType other = (ThumbnailType) obj;
    if (height == null) {
      if (other.height != null)
        return false;
    } else if (!height.equals(other.height))
      return false;
    if (width == null) {
      if (other.width != null)
        return false;
    } else if (!width.equals(other.width))
      return false;
    return true;
  }
}
