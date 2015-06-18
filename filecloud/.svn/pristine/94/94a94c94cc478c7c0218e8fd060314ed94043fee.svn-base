package com.newroad.fileext;

import com.newroad.fileext.service.picture.ThumbnailDesigner;

/**
 * @info
 * @author tangzj1
 * @date Dec 10, 2013
 * @version
 */
public class MakeThumbnailTest {

  /**
   * @param args
   */
  public static void main(String[] xxx) {
    String[] srcArray =
        {"D:/testImage/00407da365960100", "D:/testImage/00407dc7b9670300", "D:/testImage/00407dc7db9f0100",
            "D:/testImage/00407e8185c70100", "D:/testImage/00407e8700ab0100", "D:/testImage/00407e7136430300",
            "D:/testImage/00407ec4792a0100", "D:/testImage/00407ec427110400", "D:/testImage/00407faa26ae0300",
            "D:/testImage/00407faa760e0300", "D:/testImage/004082fda23e0100", "D:/testImage/0040776f6e770400",
            "D:/testImage/0040776fe5140300", "D:/testImage/00407774e98b0400", "D:/testImage/004076863fd10400",
            "D:/testImage/IMG_1290yuan.JPG"};

    int aaa = 500;
    Thread[] testThread = new Thread[aaa];
    for (int i = 0; i < aaa; i++) {
      testThread[i] = new makeThumb("D:/testImage/00407da365960100");
    }
    for (int i = 0; i < aaa; i++) {
      testThread[i].start();
    }

  }

}


class makeThumb extends Thread {

  private String src;

  public makeThumb(String src) {
    this.src = src;
  }

  @Override
  public void run() {
    ThumbnailDesigner.makeThumbnail(src,false, 94, 94);
  }
}
