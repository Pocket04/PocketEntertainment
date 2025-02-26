package com.pockEtentertainmentApp.game.repository;

import com.pockEtentertainmentApp.game.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {

    Optional<Game> getGameByName(String name);
    Optional<Game> getGameById(UUID id);
}
