package com.fypgradingsystem.teams.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fypgradingsystem.teams.messaging.Message;
import com.fypgradingsystem.teams.messaging.QueueSender;
import com.fypgradingsystem.teams.model.Team;

import io.smallrye.mutiny.Uni;
import io.vertx.core.json.Json;

@Service
public class TeamRepository {
  
  @Autowired
  TeamCrudRepository crudRepository;

  @Autowired
  QueueSender sender;

  public Uni<Team> createTeam(Team team) {
    return crudRepository.persist(team).onItem().transform(teamPersisted -> {
      var message = Message
      .builder()
      .queue("auth")
      .type("create-accounts")
      .order(Json.encode(team.getMembers()))
      .build();

      sender.send(message);
      return teamPersisted;
    });
  }
}
