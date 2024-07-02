package com.project.lovlind.domain.chat.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
// TODO 추후 필터를 위해 생성
public class ChatrommSearchFilter {
  private String search;
  private Integer count;
  private Integer page;
  private Integer pageSize;

  public ChatrommSearchFilter(Integer page, Integer pageSize) {
    this.page = page;
    this.pageSize = pageSize;
  }

  public boolean isAllSearch() {
    return search == null && count == null;
  }
}
