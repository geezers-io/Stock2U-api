package com.hack.stock2u.authentication.service;

import com.hack.stock2u.authentication.AuthException;
import com.hack.stock2u.authentication.dto.doro.DoroSearchResponse;
import com.hack.stock2u.authentication.dto.doro.DoroAddressItem;
import com.hack.stock2u.authentication.dto.doro.DoroPageDetails;
import com.hack.stock2u.authentication.dto.doro.DoroResponse;
import com.hack.stock2u.authentication.dto.doro.DoroResult;
import com.hack.stock2u.global.exception.GlobalException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class RoadNameAddressService {

  @SuppressWarnings("checkstyle:Indentation")
  private final String url = "https://business.juso.go.kr/addrlink/addrLinkApi.do";
  private final String parameters = "?confmKey={0}&currentPage={1}&countPerPage={2}&resultType=json&keyword={3}";

  @Value("${app.doro.accessKey}")
  private String accessKey;

  public DoroSearchResponse getAddress(String keyword, Integer page, Integer size) {
    int pageNum = Objects.requireNonNullElse(page, 1);
    int sizeNum = Objects.requireNonNullElse(size, 10);

    String paramsUrl = MessageFormat.format(parameters, accessKey, pageNum, sizeNum, keyword);
    WebClient client = WebClient.create(url + paramsUrl);
    DoroResponse doroResponse = client
        .get()
        .retrieve()
        .bodyToFlux(DoroResponse.class)
        .blockFirst();

    DoroResult results = doroResponse.results();
    String errorCode = results.common().errorCode();
    validateErrorCode(errorCode);

    DoroPageDetails pageDetails = DoroPageDetails.from(results.common());
    List<DoroAddressItem> addressItems =
        results.juso().stream().map(DoroAddressItem::from).toList();

    return new DoroSearchResponse(pageDetails, addressItems);
  }

  private void validateErrorCode(String errorCode) {
    if (errorCode.equals("0")) {
      return;
    }

    switch (errorCode) {
      case "E0008" -> throw AuthException.DORO_ONLY_ONE_CHAR.create();
      case "E0009" -> throw AuthException.DORO_CHARACTER.create();
      case "E0010" -> throw AuthException.DORO_TOO_LONG.create();
      case "E0012" -> throw AuthException.DORO_BAD_FORM.create();
      case "E0014" -> throw AuthException.DORO_KEY_EXPIRED.create();
      case "E0015" -> throw AuthException.DORO_OVER_SEARCHING.create();
      case "-999" -> throw GlobalException.SERVER_ERROR.create();
      default -> {
        throw GlobalException.SERVER_ERROR.create();
      }
    }
  }

}
