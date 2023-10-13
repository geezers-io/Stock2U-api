package com.hack.stock2u.authentication.dto.doro;

public record DoroPageDetails(
    int countPerPage,
    int totalCount,
    int currentPage
) {
  public static DoroPageDetails from(DoroCommon common) {
    int totalCount = Integer.parseInt(common.totalCount());
    return new DoroPageDetails(common.countPerPage(), totalCount, common.currentPage());
  }
}
