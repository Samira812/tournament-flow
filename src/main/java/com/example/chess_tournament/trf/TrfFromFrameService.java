package com.example.chess_tournament.trf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class TrfFromFrameService {

    public String generateTrfFromJsonUsingFrame(String jsonFile, String frameFile, String outputFile) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            Map<String, Object> data = mapper.readValue(new File(jsonFile), Map.class);
            List<String> frameLines = new ArrayList<>();

            // Step 1: Read frame.trf
            try (BufferedReader reader = new BufferedReader(new FileReader(frameFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    frameLines.add(line);
                }
            }

            // Step 2: Copy frame into report
            List<String> outputLines = new ArrayList<>(frameLines);

            // Step 3: Adjust number of 001 lines
            int playerCount = (Integer) data.get("numPlayers");
            List<Integer> playerLineIndexes = new ArrayList<>();
            for (int i = 0; i < outputLines.size(); i++) {
                if (outputLines.get(i).startsWith("001")) {
                    playerLineIndexes.add(i);
                }
            }

            int currentCount = playerLineIndexes.size();
            if (playerCount > currentCount) {
                String lastLine = outputLines.get(playerLineIndexes.get(currentCount - 1));
                for (int i = 0; i < playerCount - currentCount; i++) {
                    outputLines.add(lastLine);
                }
            } else if (playerCount < currentCount) {
                for (int i = currentCount - 1; i >= playerCount; i--) {
                    outputLines.remove((int) playerLineIndexes.get(i));
                }
            }

            // Step 4: Replace header info (012–XXR)
            replaceField(outputLines, "012", "012 XX " + data.get("tournamentName"));
            replaceField(outputLines, "042", "042 " + data.get("startDate"));
            replaceField(outputLines, "052", "052 " + data.get("endDate"));
            replaceField(outputLines, "062", "062 " + data.get("numPlayers"));
            replaceField(outputLines, "092", "092 " + data.get("type"));
            replaceField(outputLines, "102", "102 " +
                    ((Map<?, ?>) data.get("arbiter")).get("name") +
                    " (" + ((Map<?, ?>) data.get("arbiter")).get("fideId") + ")");
            replaceField(outputLines, "XXR", "XXR " + data.get("rounds"));

            // Step 5: Replace player info in-place
            List<Map<String, Object>> players = (List<Map<String, Object>>) data.get("players");
            int pIdx = 0;
            for (int i = 0; i < outputLines.size(); i++) {
                if (outputLines.get(i).startsWith("001") && pIdx < players.size()) {
                    String originalLine = outputLines.get(i);
                    Map<String, Object> player = players.get(pIdx);

                    String updatedLine = overwritePlayerInfo(originalLine,
                            player.get("name").toString(),
                            player.get("fideId").toString(),
                            player.get("birthYear").toString());

                    outputLines.set(i, updatedLine);
                    pIdx++;
                }
            }

            // Step 6: Write to report.trf
            try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
                for (String line : outputLines) {
                    writer.println(line);
                }
            }

            return "report.trf created successfully";

        } catch (IOException e) {
            return "Failed: " + e.getMessage();
        }
    }

    private void replaceField(List<String> lines, String prefix, String newLine) {
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).startsWith(prefix)) {
                lines.set(i, newLine);
                break;
            }
        }
    }

    private String overwritePlayerInfo(String original, String name, String fideId, String birthYear) {
        StringBuilder sb = new StringBuilder(original);

        // replace name (starts at index 17, length 33)
        int nameStart = 14;
        for (int i = 0; i < 33; i++) {
            char c = (i < name.length()) ? name.charAt(i) : ' ';
            sb.setCharAt(nameStart + i, c);
        }

        // fideId (after fed, assumed starts at index ~53)
        int fideEnd = 68;
        int fideStart = fideEnd - fideId.length();
        for (int i = 0; i < fideId.length(); i++) {
            sb.setCharAt(fideStart + i, fideId.charAt(i));
        }

        // birth year (4 digits)
        int birthStart = fideEnd + 1;
        for (int i = 0; i < 4; i++) {
            sb.setCharAt(birthStart + i, birthYear.charAt(i));
        }

        return sb.toString();
    }
}
