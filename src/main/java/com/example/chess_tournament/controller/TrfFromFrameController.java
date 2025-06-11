package com.example.chess_tournament.controller;

import com.example.chess_tournament.trf.TrfFromFrameService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trf")
public class TrfFromFrameController {

    private final TrfFromFrameService trfService;

    public TrfFromFrameController(TrfFromFrameService trfService) {
        this.trfService = trfService;
    }

    @GetMapping("/from-frame")
    public String generateTrfFromFrame(
            @RequestParam String jsonFile,
            @RequestParam String frameFile,
            @RequestParam(defaultValue = "report.trf") String outputFile) {
        return trfService.generateTrfFromJsonUsingFrame(jsonFile, frameFile, outputFile);
    }
}
