package com.fypgradingsystem.teams.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Document
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Team {
  @Id
  private String id;
  private String uuid;
  private String number;
  private String description;
  private List<Member> members;
  private Presentation presentation;
  private String subjectName;
  private Supervisor supervisor;
  private List<String> jury;

  public void assignToJury(String id) {
    jury.add(id);
  }
}
