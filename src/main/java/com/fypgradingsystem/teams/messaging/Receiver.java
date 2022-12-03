package com.fypgrading.teams.messaging;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fypgradingsystem.teams.model.Member;
import com.fypgradingsystem.teams.model.Team;
import com.fypgradingsystem.teams.model.Supervisor;
import com.fypgradingsystem.teams.repository.TeamRepository;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@Component
public class Receiver {

  @Autowired
  TeamRepository teamRepository;

  @RabbitListener(queues = "${queue.name}")
  public void receiveMessage(String message) {
    var messageEntity = Json.decodeValue(message, Message.class);
    switch (messageEntity.getType()) {
      case "create-jury":
        System.out.println("Received message: " + messageEntity.getMessage());
        var order = new JsonObject(messageEntity.getOrder());
        teamRepository.assignJuryToTeam(order.getString("teamUUID"), order.getString("email")).await().indefinitely();
        break;
      case "create-team":
        var createTeamOrder = new JsonObject(messageEntity.getOrder());
        Team team = Team
            .builder()
            .subjectName(createTeamOrder.getString("subject"))
            .supervisor(Supervisor
                .builder()
                .name(createTeamOrder.getString("supervisor").split(":")[0])
                .email(createTeamOrder.getString("supervisor").split(":")[1])
                .build())
            .uuid(createTeamOrder.getString("teamUUID"))
            .members(serializeMembers(createTeamOrder.getJsonArray("members")))
            .description(createTeamOrder.getString("description"))
            .build();
        teamRepository.createTeam(team).await().indefinitely();

      case "assign-jury":
        var assignJuryOrder = new JsonObject(messageEntity.getOrder());
        System.out.println("Received message: " + messageEntity.getOrder());
        teamRepository.assignJuryToTeam(assignJuryOrder.getString("teamUUID"), assignJuryOrder.getString("email")).await().indefinitely();
        break;
      default:
        break;
    }
  }

  private List<Member> serializeMembers(JsonArray members) {
    return members.stream().map(member -> {
      var memberJson = new JsonObject(member.toString());
      return Member
          .builder()
          .name(memberJson.getString("name"))
          .email(memberJson.getString("email"))
          .major(memberJson.getString("major"))
          .build();
    }).collect(Collectors.toList());
  }
}