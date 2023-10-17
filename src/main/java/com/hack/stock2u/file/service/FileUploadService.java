package com.hack.stock2u.file.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.hack.stock2u.authentication.service.SessionManager;
import com.hack.stock2u.file.dto.FileIdListResponse;
import com.hack.stock2u.file.dto.SimpleFile;
import com.hack.stock2u.file.exception.FileException;
import com.hack.stock2u.file.repository.AttachRepository;
import com.hack.stock2u.global.exception.BasicException;
import com.hack.stock2u.global.exception.GlobalException;
import com.hack.stock2u.models.Attach;
import com.hack.stock2u.models.User;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileUploadService {
  private final S3Service s3Service;
  private final AttachRepository attachRepository;
  private final SessionManager sessionManager;
  private final Tika tika = new Tika();
  private final FileNameProvider fileNameProvider = new FileNameProvider();

  @Transactional
  public FileIdListResponse uploadFiles(List<MultipartFile> files) {
    User user = sessionManager.getSessionUser();
    List<SimpleFile> ret = files.stream().map(f -> upload(f, user)).toList();
    return new FileIdListResponse(ret);
  }

  public SimpleFile upload(MultipartFile file, User u) {
    String contentType = file.getContentType();
    String originalFilename = file.getOriginalFilename();
    String filename = fileNameProvider.convertFilename(originalFilename);

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.addUserMetadata("extension", contentType);
    metadata.addUserMetadata("filename", filename);

    String uploadPath = s3Upload(filename, file, metadata);
    Attach attach = attachRepository.save(
        Attach.builder()
            .name(filename)
            .ext(contentType)
            .uploadPath(uploadPath)
            .user(u)
            .build()
    );

    return new SimpleFile(attach.getId(), uploadPath);
  }

  public InputStream resize(String key) {
    try {
      InputStream stream = s3Service.getObject(key).getObjectContent().getDelegateStream();
      BufferedImage image = ImageIO.read(stream);
      BufferedImage bufferedImage = new BufferedImage(169, 120, image.getType());
      Graphics2D graphics = bufferedImage.createGraphics();
      graphics.drawImage(image, 0, 0, 169, 120, null);
      graphics.dispose();
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      ImageIO.write(image, "png", os);
      return new ByteArrayInputStream(os.toByteArray());
    } catch (IOException ex) {
      throw GlobalException.SERVER_ERROR.create();
    }
  }

  public void remove(Long id) {
    Attach attach = attachRepository.findById(id).orElseThrow(GlobalException.NOT_FOUND::create);
    attachRepository.delete(attach);
  }

  private String s3Upload(String filename, MultipartFile file, ObjectMetadata metadata) {
    try (InputStream inputStream = file.getInputStream()) {
      validateImage(inputStream);
      return s3Service.uploadAndReturnUrl(filename, inputStream, metadata);
    } catch (BasicException ex) {
      throw ex;
    } catch (Exception ex) {
      log.error(ex.getMessage());
      ex.printStackTrace();
      throw GlobalException.SERVER_ERROR.create();
    }
  }

  public void validateImage(InputStream stream) {
    try {
      String mediaType = tika.detect(stream);
      if (!mediaType.startsWith("image")) {
        log.warn("upload file type is {}", mediaType);
        throw FileException.FILE_NOT_IMAGE.create();
      }
    } catch (IOException e) {
      throw GlobalException.SERVER_ERROR.create();
    }

  }

  private File multipartFileToFile(MultipartFile file) {
    try {
      File f = new File(file.getOriginalFilename());
      file.transferTo(f);
      return f;
    } catch (IOException ex) {
      throw GlobalException.SERVER_ERROR.create();
    }
  }

}
