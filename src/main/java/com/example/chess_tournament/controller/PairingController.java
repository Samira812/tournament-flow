package com.example.chess_tournament.controller;

import com.example.chess_tournament.model.Tournament;
import com.example.chess_tournament.repository.TournamentRepository;
import com.example.chess_tournament.pairing.JaVaFoService;
import com.example.chess_tournament.utils.TournamentToTRF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/pairing")
public class PairingController {

    private final JaVaFoService javaFoService;
    private final TournamentRepository tournamentRepository;

    @Autowired
    public PairingController(JaVaFoService javaFoService, TournamentRepository tournamentRepository) {
        this.javaFoService = javaFoService;
        this.tournamentRepository = tournamentRepository;
    }

    @GetMapping("/generate")
    public String generatePairing(
            @RequestParam(defaultValue = "report.trf") String trfFile,
            @RequestParam(required = false) List<String> tiebreakers) {
        return javaFoService.runPairing(trfFile, tiebreakers);
    }

    @GetMapping("/generate2")
    public String generatePairing2(
            @RequestParam Long tournamentId,
            @RequestParam(required = false) List<String> tiebreakers) {

        // bring tournament from database
        Tournament tournament = tournamentRepository.findById(tournamentId).orElse(null);
        if (tournament == null) {
            return "Tournament not found.";
        }

        // generate temp TRF file
        String frameFile = "src/main/resources/frame.trf";
        String trfFile = TournamentToTRF.convertToTRF(tournament, frameFile);

        // run JaVaFo
        return javaFoService.runPairing(trfFile, tiebreakers);
    }

}
