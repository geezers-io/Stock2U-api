package com.hack.stock2u.file.service;

import com.hack.stock2u.file.repository.JpaAttachRepository;
import com.hack.stock2u.global.exception.GlobalException;
import com.hack.stock2u.models.Attach;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ImageProvider {
  private final JpaAttachRepository attachRepository;

  public List<String> getImageUrls(List<Long> imageIds) {
    if (imageIds == null) {
      return null;
    }

    return attachRepository.findAllById(imageIds)
        .stream()
        .map(Attach::getUploadPath)
        .toList();
  }

  public Attach findAttachOrThrow(Long id) {
    if (id == null) {
      return null;
    }

    return attachRepository.findById(id).orElseThrow(GlobalException.NOT_FOUND::create);
  }

  public String getImageOrElseThrow(Long id) {
    if (id == null) {
      return null;
    }

    return attachRepository.findById(id)
        .orElseThrow(GlobalException.NOT_FOUND::create)
        .getUploadPath();
  }

}
