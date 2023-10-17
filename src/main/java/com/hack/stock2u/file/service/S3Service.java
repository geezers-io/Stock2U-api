package com.hack.stock2u.file.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class S3Service {
  private final AmazonS3 s3;
  private final String bucket = "stock2u-bucket";

  public String uploadAndReturnUrl(String filename, InputStream stream, ObjectMetadata metadata) {
    PutObjectRequest request = new PutObjectRequest(bucket, filename, stream, metadata)
        .withCannedAcl(CannedAccessControlList.PublicReadWrite);
    s3.putObject(request);
    return s3.getUrl(bucket, filename).toString();
  }

  public String getKey(String url) {
    String[] tokens = url.split("amazonaws.com");
    return tokens[1].substring(1);
  }

}
