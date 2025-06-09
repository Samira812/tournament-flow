package com.example.chess_tournament.results;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class RecordResultsService {

    public static class GameResult {
        public String white;
        public String black;
        public String result;
    }

    public String updateTrfFromJson(String jsonFilePath, String trfPath) {
        Map<String, String> resultMap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            GameResult[] results = mapper.readValue(new File(jsonFilePath), GameResult[].class);
            for (GameResult r : results) {
                resultMap.put(r.white.trim(), r.result);
                resultMap.put(r.black.trim(), reverseResult(r.result));
            }
        } catch (IOException e) {
            return "Failed to read JSON: " + e.getMessage();
        }

        File inputFile = new File(trfPath);
        File tempFile = new File(trfPath + ".tmp");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("001")) {
                    writer.println(line);
                    continue;
                }

                String name = line.substring(12, 46).trim();
                String result = resultMap.get(name);

                if (result != null) {
                    writer.println(line + " " + result);
                } else {
                    writer.println(line);
                }
            }

        } catch (IOException e) {
            return "Failed to update TRF: " + e.getMessage();
        }

        if (!inputFile.delete())
            return "Failed to delete original file.";
        if (!tempFile.renameTo(inputFile))
            return "Failed to rename temp file.";

        return "Results recorded in original file.";
    }

    private String reverseResult(String result) {
        return switch (result) {
            case "1" -> "0";
            case "0" -> "1";
            case "=" -> "=";
            default -> "0";
        };
    }
}
