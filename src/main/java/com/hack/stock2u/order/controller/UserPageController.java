package com.hack.stock2u.order.controller;

import com.hack.stock2u.authentication.service.AuthService;
import com.hack.stock2u.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RequiredArgsConstructor
@Controller
@Transactional

public class UserPageController {

      private final UserPageController userPageController;
      private final AuthService authService;

    @GetMapping("/user/{id}/order")
    public String myOrderPage
        (@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails)
    {
      // 로그인 User == 접속 User
      if (principalDetails.buyer_id().getId() == id) {

      }
        // 구매자의 주문내역
        User user = userPageService.findUser(id);
        List<Order> orderList = user.getOrders();

        model.addAttribute("orderList", orderList);
        model.addAttribute("user", user);

        return "/user/order";
      {

        // return "redirect:/main";
      }
    }



