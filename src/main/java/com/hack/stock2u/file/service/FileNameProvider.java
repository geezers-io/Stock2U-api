package com.hack.stock2u.file.service;

import com.hack.stock2u.file.exception.FileException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class FileNameProvider {

  public String convertFilename(String name) {
    String ext = getExt(name);
    return URLEncoder.encode(
        getRemovedExtFilename(name),
        StandardCharsets.UTF_8
    ) + ext;
  }

  public String getRemovedExtFilename(String filename) {
    int dot = getDotIdx(filename);
    return filename.substring(0, dot);
  }

  public String getExt(String filename) {
    int dot = getDotIdx(filename);
    return filename.substring(dot);
  }

  public int getDotIdx(String filename) {
    int dot = filename.indexOf('.');
    if (dot == -1) {
      throw FileException.NOT_EXT.create();
    }
    return dot;
  }

}
