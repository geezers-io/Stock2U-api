package com.hack.stock2u.authentication.dto.doro;

/**
 * 사용자에게 제공되는 용도 (DoroAddress 객체는 도로명주소 API 서버의 응답 데이터)
 */
public record DoroAddressItem(
    String fullRoadAddrName,
    String roadAddrPart1,
    String roadAddrPart2,
    int zipCode,
    String buildingName
) {
  public static DoroAddressItem from(DoroAddress address) {
    int zipNumber = Integer.parseInt(address.zipNo());
    return new DoroAddressItem(
        address.roadAddr(),
        address.roadAddrPart1(),
        address.roadAddrPart2(),
        zipNumber,
        address.bdNm()
    );
  }
}
