package com.hack.stock2u.file.api;

import com.hack.stock2u.file.dto.FileIdListResponse;
import com.hack.stock2u.file.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "F. 이미지 업로드 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/images")
public class ImageUploadApi {
  private final FileUploadService uploadService;

  @Operation(summary = "다중 이미지 업로드 API", description = "최대 5개의 이미지 업로드를 수행합니다.")
  @PostMapping(consumes = "multipart/form-data")
  public ResponseEntity<FileIdListResponse> uploadFilesApi(
      @RequestPart("images") List<MultipartFile> files
  ) {
    FileIdListResponse fileIds = uploadService.uploadFiles(files);
    return ResponseEntity.status(HttpStatus.CREATED).body(fileIds);
  }

  @Operation(summary = "이미지 삭제 API", description = "이미지 삭제를 수행합니다.")
  @DeleteMapping
  public ResponseEntity<Void> removeFileApi(
      @Parameter(
          name = "id",
          description = "파일 고유 번호",
          required = true
      )
      @RequestParam(name = "id") Long fileId
  ) {
    uploadService.remove(fileId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

}
