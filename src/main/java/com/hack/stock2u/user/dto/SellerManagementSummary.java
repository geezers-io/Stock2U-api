package com.hack.stock2u.user.dto;

import com.hack.stock2u.chat.dto.response.SimpleReservation;
import com.hack.stock2u.product.dto.SimpleProductManage;


public record SellerManagementSummary(
    SimpleProductManage simpleProductManage,
    SimpleReservation simpleReservation,
    String purchaserName
) {
}
