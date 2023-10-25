package com.hack.stock2u.user.service;

import com.hack.stock2u.authentication.service.AuthCodeProvider;
import com.hack.stock2u.authentication.service.SessionManager;
import com.hack.stock2u.file.dto.SimpleFile;
import com.hack.stock2u.file.service.FileUploadService;
import com.hack.stock2u.models.User;
import com.hack.stock2u.user.dto.PurchaserRequest;
import com.hack.stock2u.user.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class MyService {
  private final FileUploadService fileUploadService;
  private final SessionManager sessionManager;
  private final JpaUserRepository userRepository;
  private final AuthCodeProvider authCodeProvider;

  public SimpleFile uploadAvatarImage(MultipartFile multipartFile) {
    User user = sessionManager.getSessionUserByRdb();
    SimpleFile simpleFile = fileUploadService.upload(multipartFile, user);
    user.changeAvatarId(simpleFile.id());
    userRepository.save(user);
    return simpleFile;
  }

  public void update(PurchaserRequest.Update updateRequest) {
    authCodeProvider.verifyCode(updateRequest.phone(), updateRequest.authCode());
    User user = sessionManager.getSessionUserByRdb();
    user.changePhone(updateRequest.phone());
    userRepository.save(user);
  }

}
