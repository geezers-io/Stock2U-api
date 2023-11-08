package com.hack.stock2u.order.controller;

import com.hack.stock2u.authentication.service.AuthService;
import com.hack.stock2u.models.Buyer;
import com.hack.stock2u.models.Reservation;
import com.hack.stock2u.models.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController

public class UserPageController implements UserDetails {



      private final UserPageController userPageController;
      private final AuthService authService;

    @GetMapping("/user/{id}/order")
    public String Buyer (@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails)

    {


      // 로그인 User == 접속 User
      if (principalDetails.buyer_id().getId() == id) {

      }
        // 구매자의 주문내역
        User user = Buyer.findUser(id);
        List<Buyer> orderList = user.getBuyers();

        model.addAttribute("orderList", orderList);
        model.addAttribute("user", user);

        return "/user/order";
      {

      }
    }



