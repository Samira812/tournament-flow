package com.example.chess_tournament.controller;

import com.example.chess_tournament.model.Tournament;
import com.example.chess_tournament.pairing.JaVaFoService;
import com.example.chess_tournament.utils.TournamentToTRF;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/pairing")
public class PairingController {

    private final JaVaFoService javaFoService;

    public PairingController(JaVaFoService javaFoService) {
        this.javaFoService = javaFoService;
    }

    @GetMapping("/generate")
    public String generatePairing(
            @RequestParam(defaultValue = "report.trf") String trfFile,
            @RequestParam(required = false) List<String> tiebreakers) {
        return javaFoService.runPairing(trfFile, tiebreakers);
    }
    @GetMapping("/generate2")
    public String generatePairing2(
            @RequestParam(defaultValue = "report.trf") Tournament tournament,
            @RequestParam(required = false) List<String> tiebreakers) {
        String trfFile= TournamentToTRF.convertToTRF(tournament);
        return javaFoService.runPairing(trfFile, tiebreakers);
    }
}
