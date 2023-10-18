package com.hack.stock2u.file.dto;

import com.hack.stock2u.models.Attach;

public record SimpleFile(Long id, String uploadPath) {
  public static SimpleFile attach(Attach a) {
    return new SimpleFile(a.getId(), a.getUploadPath());
  }
}
