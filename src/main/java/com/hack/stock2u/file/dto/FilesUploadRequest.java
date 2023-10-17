package com.hack.stock2u.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record FilesUploadRequest(
    @Schema(required = true, description = "파일 객체 (최대 5개)")
    @NotNull
    List<MultipartFile> files
) {}
