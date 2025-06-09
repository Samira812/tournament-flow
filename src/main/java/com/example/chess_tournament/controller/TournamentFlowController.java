package com.example.chess_tournament.controller;

import com.example.chess_tournament.flow.TournamentFlowService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flow")
public class TournamentFlowController {

    private final TournamentFlowService flowService;

    public TournamentFlowController(TournamentFlowService flowService) {
        this.flowService = flowService;
    }

    @GetMapping("/start")
    public String startTournament(
            @RequestParam String trfFile,
            @RequestParam(required = false) List<String> tiebreakers) {
        return flowService.startFirstRound(trfFile, tiebreakers);
    }

    @GetMapping("/next")
    public String runNextRound(
            @RequestParam String jsonFile,
            @RequestParam String trfFile,
            @RequestParam(required = false) List<String> tiebreakers) {
        return flowService.runRound(jsonFile, trfFile, tiebreakers);
    }

}
