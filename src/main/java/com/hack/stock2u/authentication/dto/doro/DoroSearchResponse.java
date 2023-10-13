package com.hack.stock2u.authentication.dto.doro;

import com.hack.stock2u.authentication.dto.doro.DoroAddressItem;
import com.hack.stock2u.authentication.dto.doro.DoroPageDetails;
import java.util.List;

public record DoroSearchResponse(
    DoroPageDetails page,
    List<DoroAddressItem> results
) {}
