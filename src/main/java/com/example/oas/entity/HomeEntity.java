package com.example.oas.entity;

import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HomeEntity {
  private String id;
  private String content;
}
