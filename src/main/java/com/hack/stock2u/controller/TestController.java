package com.hack.stock2u.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class TestController {

  @Operation(summary = "Say Hello API", description = "호출하면 Hello 합니다.")
  @GetMapping("/")
  public String hello() {
    return "hello";
  }

}
