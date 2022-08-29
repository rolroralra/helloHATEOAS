package com.example.oas.controller.dto;

import com.example.oas.entity.HomeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeDto {
  private String id;
  private String content;

  public static HomeDto of(HomeEntity homeEntity) {
    return HomeDto.builder().id(homeEntity.getId()).content(homeEntity.getContent()).build();
  }
}
