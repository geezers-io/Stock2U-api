package com.hack.stock2u.constant;

import lombok.Getter;

@Getter
public enum ChatMessageType {
  TEXT("텍스트"),
  IMAGE("이미지");

  private final String name;

  ChatMessageType(String name) {
    this.name = name;
  }
}
