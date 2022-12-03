package com.fypgradingsystem.teams.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Member {
  private String name;
  private String email;
  private String major;
}
