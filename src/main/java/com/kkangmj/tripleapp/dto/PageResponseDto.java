package com.kkangmj.tripleapp.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PageResponseDto<T> {

  private int page;
  private int totalPages;
  private List<T> data;

  public static <T> PageResponseDto<T> of(int page, int totalPages, List<T> data) {
    return new PageResponseDto<>(page, totalPages, data);
  }
}
