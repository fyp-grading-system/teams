package com.fypgradingsystem.teams.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.fypgradingsystem.teams.model.Team;

import io.smallrye.mutiny.Uni;

public interface TeamCrudRepository extends ReactiveCrudRepository<Team, String> {
  
  @Query("{ 'supervisorId': ?0 }")
  Uni<List<Team>> getBySupervisorId(String id);

  @Query("{ 'id' : ?0 }")
  Uni<Team> getById(String id);

  default Uni<Team> persist(Team squad) {
    return Uni.createFrom().future(save(squad).toFuture());
  }
}
