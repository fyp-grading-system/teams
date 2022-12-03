package com.fypgradingsystem.teams.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Document
@Builder
public class Team {
  @Id
  private String id;
  private String number;
  private List<Member> members;
  private Presentation presentation;
  private String subject;
  private String supervisorId;
}
