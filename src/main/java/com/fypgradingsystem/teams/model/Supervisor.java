package com.fypgradingsystem.teams.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Supervisor {
  private String name;
  private String email;
}
