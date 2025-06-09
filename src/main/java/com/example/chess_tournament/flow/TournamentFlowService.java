package com.example.chess_tournament.flow;

import com.example.chess_tournament.pairing.JaVaFoService;
import com.example.chess_tournament.results.RecordResultsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TournamentFlowService {

    private final RecordResultsService resultsService;
    private final JaVaFoService javaFoService;

    public TournamentFlowService(RecordResultsService resultsService, JaVaFoService javaFoService) {
        this.resultsService = resultsService;
        this.javaFoService = javaFoService;
    }

    public String runRound(String jsonFile, String trfFile, List<String> tieBreakers) {
        String recordResult = resultsService.updateTrfFromJson(jsonFile, trfFile);
        if (!recordResult.startsWith("R"))
            return recordResult;

        String pairingResult = javaFoService.runPairing(trfFile, tieBreakers);
        return recordResult + "\n" + pairingResult;
    }

    public String startFirstRound(String trfFile, List<String> tieBreakers) {
        return javaFoService.runPairing(trfFile, tieBreakers);
    }
}
