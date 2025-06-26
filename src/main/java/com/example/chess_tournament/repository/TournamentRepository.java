package com.example.chess_tournament.repository;

import com.example.chess_tournament.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
<<<<<<< HEAD
    Tournament findByTournamentName(String name);
=======
    Tournament findByName(String name);
>>>>>>> 92d7ca1 (connrcting to front changes)
}
