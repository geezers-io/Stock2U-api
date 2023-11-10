package com.hack.stock2u.chat.dto;

import com.hack.stock2u.models.Product;
import com.hack.stock2u.models.Reservation;
import com.hack.stock2u.models.User;

public record ReservationProductPurchaser(
    Reservation reservation,
    User purchaser
) {}
