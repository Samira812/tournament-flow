package com.example.chess_tournament.controller;

import com.example.chess_tournament.pairing.JaVaFoService;
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
}
