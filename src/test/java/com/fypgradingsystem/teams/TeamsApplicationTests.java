package com.fypgradingsystem.teams;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fypgradingsystem.teams.model.Member;
import com.fypgradingsystem.teams.model.Team;
import com.fypgradingsystem.teams.repository.TeamRepository;

@SpringBootTest
class TeamsApplicationTests {

  @Autowired
  private TeamRepository repository;

	@Test
	void contextLoads() {
    var squad = Team.builder()
      .number("Team 1")
      .members(List.of(Member.builder().email("yammine.yammine@net.usj.edu.lb").build()))
      .build();
    repository.createTeam(squad).await().indefinitely();
	}
}
