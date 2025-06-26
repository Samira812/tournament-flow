package com.example.chess_tournament.model;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tournaments")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String country;
    private String city;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    private int rounds;

    @Column(name = "number_of_players")
    private int numPlayers;

    private String type; // e.g., "Individual Swiss Dutch"

<<<<<<< HEAD
=======
    private float ByeValue;

    private String[] tieBreakers;

>>>>>>> 92d7ca1 (connrcting to front changes)
    @ManyToOne
    @JoinColumn(name = "arbiter_id")
    private Arbiter arbiter;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
    private List<Player> players;
}
