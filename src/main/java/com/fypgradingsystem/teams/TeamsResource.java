package com.fypgradingsystem.teams;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.fypgradingsystem.teams.model.Team;
import com.fypgradingsystem.teams.repository.TeamRepository;
import io.smallrye.mutiny.Uni;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class TeamsResource {

  @Autowired
  TeamRepository repository;

  @PostMapping("teams/create")
  public Uni<Team> createTeam(@RequestBody Team team) {
    return repository.createTeam(team);
  }

  @GetMapping("teams")
  public Uni<List<Team>> getAllSquads() {
    return repository.getAllTeams();
  }

  @PostMapping("teams/delete/{id}")
  public Uni<Void> deleteTeam(@PathVariable String id) {
    return repository.deleteTeam(id);
  }
}
