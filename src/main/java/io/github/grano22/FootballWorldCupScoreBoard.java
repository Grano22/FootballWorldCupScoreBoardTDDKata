package io.github.grano22;

import java.util.*;

public final class FootballWorldCupScoreBoard {
    private final LinkedHashMap<Map.Entry<String, String>, Map.Entry<Integer, Integer>> games = new LinkedHashMap<>();

    public void startGame(String homeTeamName, String awayTeamName) {
        games.put(
            new AbstractMap.SimpleImmutableEntry<>(homeTeamName, awayTeamName),
            new AbstractMap.SimpleEntry<>(0, 0)
        );
    }

    public String getASummaryOfGamesByTotalScore() {
        if (games.isEmpty()) {
            return "";
        }

        final var summary = new StringJoiner("\n");
        for (var game : games.reversed().entrySet()) {
            final var matchScore = new StringBuilder();
            matchScore
                .append(game.getKey().getKey()).append(' ').append(game.getValue().getKey()).append(" - ")
                .append(game.getKey().getValue()).append(' ').append(game.getValue().getValue())
            ;
            summary.add(matchScore);
        }

        return summary.toString();
    }
}
