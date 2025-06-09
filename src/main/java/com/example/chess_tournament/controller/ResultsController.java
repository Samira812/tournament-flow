package com.example.chess_tournament.controller;

import com.example.chess_tournament.results.RecordResultsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/results")
public class ResultsController {

    private final RecordResultsService resultsService;

    public ResultsController(RecordResultsService resultsService) {
        this.resultsService = resultsService;
    }

    @GetMapping("/record-json")
    public String recordFromJson(
            @RequestParam String jsonFile,
            @RequestParam(defaultValue = "report.trf") String trfFile) {
        return resultsService.updateTrfFromJson(jsonFile, trfFile);
    }
}
