package com.fypgradingsystem.teams.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fypgradingsystem.teams.messaging.Message;
import com.fypgradingsystem.teams.messaging.QueueSender;
import com.fypgradingsystem.teams.model.Team;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

@Service
public class TeamRepository {

  @Autowired
  TeamCrudRepository crudRepository;

  @Autowired
  QueueSender sender;

  public Uni<List<Team>> getAllTeams() {
    return Uni.createFrom().future(crudRepository.findAll().collectList().toFuture()).onItem().transform(squads -> {
      return squads;
    });
  }

  public Uni<Void> deleteTeam(String id) {
    return Uni.createFrom().future(crudRepository.deleteById(id).toFuture());
  }

  public Uni<Team> createTeam(Team squad) {
    squad.setJury(new ArrayList<>());
    return crudRepository.persist(squad).onItem().transform(squadPersisted -> {
      var message = Message
          .builder()
          .queue("notifications")
          .type("team-created")
          .order(Json.encode(squad.getMembers()))
          .build();
      sender.send(message);
      return squadPersisted;
    });
  }

  public Uni<Team> assignJuryToTeam(String teamUUID, String juryEmail) {
    return crudRepository.getByTeamUUID(teamUUID).onItem().transform(persistedTeam -> {
      persistedTeam.assignToJury(juryEmail);

      if (juryEmail != null) {
        var json = new JsonObject();
        json.put("jury_email", juryEmail);
        json.put("members", persistedTeam.getMembers());
        var message = Message
            .builder()
            .queue("notifications")
            .type("jury-assigned")
            .order(Json.encode(json).toString())
            .build();
        sender.send(message);
      }
      return persistedTeam;
    });
  }

}
