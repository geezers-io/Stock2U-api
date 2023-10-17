package com.hack.stock2u.user.api;

import com.hack.stock2u.file.dto.SimpleFile;
import com.hack.stock2u.user.service.MyService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "M. 마이페이지 - 공용 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/my")
public class MyApi {
  private final MyService myService;

  @PostMapping(value = "/avatar", consumes = "multipart/form-data")
  public ResponseEntity<SimpleFile> changeAvatarImageApi(
      @Parameter(
          name = "file",
          description = "변경할 이미지 파일",
          required = true
      )
      @RequestPart("file")MultipartFile multipartFile
  ) {
    SimpleFile simpleFile = myService.uploadAvatarImage(multipartFile);
    return ResponseEntity.status(HttpStatus.CREATED).body(simpleFile);
  }

}
