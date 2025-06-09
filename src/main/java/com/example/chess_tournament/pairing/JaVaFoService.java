package com.example.chess_tournament.pairing;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class JaVaFoService {

    private static final Map<String, String> TIEBREAKER_MAP = Map.of(
            "DirectEncounter", "1",
            "Buchholz", "2",
            "BuchholzCut1", "3",
            "BuchholzCut2", "4",
            "Berger", "5",
            "Cumulative", "6",
            "CumulativeOpponent", "7");

    private static final String DEFAULT_TIEBREAKERS = "1,3,2"; // DirectEncounter, BuchholzCut1, Buchholz

    public String runPairing(String trfFile, List<String> tieBreakers) {
        try {
            String tbArgument = (tieBreakers == null || tieBreakers.isEmpty())
                    ? DEFAULT_TIEBREAKERS
                    : mapTieBreakers(tieBreakers);

            if (!tbArgument.isEmpty()) {
                ensureTieBreakersInTrf(new File(trfFile), tbArgument);
            }

            List<String> command = new ArrayList<>(Arrays.asList(
                    "java", "-jar", "lib/javafo.jar",
                    "-o", trfFile,
                    trfFile));

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            process.waitFor();

            if (!tbArgument.isEmpty()) {
                ensureTieBreakersInTrf(new File(trfFile), tbArgument);
            }

            return "Pairing done.\n" + output;

        } catch (Exception e) {
            return "Failed to run JaVaFo: " + e.getMessage();
        }
    }

    private String mapTieBreakers(List<String> names) {
        List<String> codes = new ArrayList<>();
        for (String name : names) {
            for (Map.Entry<String, String> entry : TIEBREAKER_MAP.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(name)) {
                    codes.add(entry.getValue());
                }
            }
        }
        return String.join(",", codes);
    }

    private void ensureTieBreakersInTrf(File trfFile, String tbArgument) throws IOException {
        List<String> lines = new ArrayList<>();
        boolean line132Found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(trfFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("132")) {
                    line = "132 -tiebreakers=" + tbArgument;
                    line132Found = true;
                }
                lines.add(line);
            }
        }

        if (!line132Found) {
            lines.add("132 -tiebreakers=" + tbArgument);
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(trfFile))) {
            for (String l : lines) {
                writer.println(l);
            }
        }
    }
}
