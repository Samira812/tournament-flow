package com.example.chess_tournament.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Tournament {
    @Id
    @GeneratedValue
    private Long id;
    private String tournamentName;
    @ManyToOne
    @JoinColumn(name = "arbiter_id")
    private Arbiter arbiter;
    private List<Player> players;
}
