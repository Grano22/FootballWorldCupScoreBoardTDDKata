package io.github.grano22;

import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.stream.Stream;

public final class FootballWorldCupScoreBoard {
    public record MatchTeams(@NonNull String homeTeamName, @NonNull String awayTeamName) {}
    public record MatchScore(int homeTeamScore, int awayTeamScore) {}

    private final LinkedHashMap<MatchTeams, MatchScore> games = new LinkedHashMap<>();
    private final Map<String, MatchTeams> matchRefPerTeam = new HashMap<>();

    public void startGame(@NonNull String homeTeamName, @NonNull String awayTeamName) {
        preventInvalidTeamNames(homeTeamName, awayTeamName);

        final var matchTeams = new MatchTeams(homeTeamName, awayTeamName);
        if (games.containsKey(matchTeams)) {
            throw new IllegalArgumentException("Match between %s and %s already registered".formatted(homeTeamName, awayTeamName));
        }

        preventRegisteringGameWithSameTeamTwice(homeTeamName, awayTeamName);

        games.put(matchTeams, new MatchScore(0, 0));
        matchRefPerTeam.put(homeTeamName, matchTeams);
        matchRefPerTeam.put(awayTeamName, matchTeams);
    }

    public @NonNull String getASummaryOfGamesByTotalScore() {
        if (games.isEmpty()) {
            return "";
        }

        final var summary = new StringJoiner("\n");
        for (final var game : getSortedMatchesByTotalScoreAndRegistrationDate()) {
            final var matchScore = new StringBuilder();
            matchScore
                .append(game.getKey().homeTeamName()).append(' ').append(game.getValue().homeTeamScore()).append(" - ")
                .append(game.getKey().awayTeamName()).append(' ').append(game.getValue().awayTeamScore())
            ;
            summary.add(matchScore);
        }

        return summary.toString();
    }

    public void updateScore(String teamName, int score) {
        if (score < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }

        if (teamName.isBlank()) {
            throw new IllegalArgumentException("Team name cannot be empty");
        }

        final var matchRef = matchRefPerTeam.get(teamName);

        if (matchRef == null) {
            throw new IllegalArgumentException("Team %s is not registered".formatted(teamName));
        }

        games.computeIfPresent(
                matchRef,
                (_, matchScore) ->
                        teamName.equals(matchRef.homeTeamName) ?
                            new MatchScore(
                                    guardAgainstLowerScore(score, matchScore.homeTeamScore),
                                    matchScore.awayTeamScore
                            ) :
                            new MatchScore(
                                    matchScore.homeTeamScore,
                                    guardAgainstLowerScore(score, matchScore.awayTeamScore)
                            )
        );
    }

    public void finishGame(@NonNull String homeTeamName, @NonNull String awayTeamName) {
        preventInvalidTeamNames(homeTeamName, awayTeamName);

        final var matchTeams = new MatchTeams(homeTeamName, awayTeamName);
        if (!games.containsKey(matchTeams)) {
            throw new IllegalArgumentException("Match between %s and %s is not registered".formatted(homeTeamName, awayTeamName));
        }

        games.remove(matchTeams);
        matchRefPerTeam.remove(homeTeamName);
        matchRefPerTeam.remove(awayTeamName);
    }

    private void preventInvalidTeamNames(@NonNull String homeTeamName, @NonNull String awayTeamName) {
        Objects.requireNonNull(homeTeamName, "Home team name is required");
        Objects.requireNonNull(awayTeamName, "Away team name is required");

        if (homeTeamName.isBlank()) {
            throw new IllegalArgumentException("Home team name cannot be empty");
        }

        if (awayTeamName.isBlank()) {
            throw new IllegalArgumentException("Away team name cannot be empty");
        }
    }

    private void preventRegisteringGameWithSameTeamTwice(@NonNull String homeTeamName, @NonNull String awayTeamName) {
        Stream.of(homeTeamName, awayTeamName)
                .filter(matchRefPerTeam::containsKey)
                .findFirst()
                .ifPresent(team -> { throw new IllegalArgumentException("Team %s is already registered".formatted(team)); });
    }

    private List<Map.Entry<MatchTeams, MatchScore>> getSortedMatchesByTotalScoreAndRegistrationDate() {
        return games.reversed().entrySet()
            .stream()
            .sorted(Comparator.comparingInt(
                    (Map.Entry<MatchTeams, MatchScore> game) ->
                            game.getValue().homeTeamScore() + game.getValue().awayTeamScore()
            ).reversed())
            .toList()
        ;
    }

    private int guardAgainstLowerScore(int nextScore, int actualScore) {
        if (nextScore < actualScore) {
            throw new IllegalArgumentException("Score cannot be lower than previous score");
        }

        return nextScore;
    }
}
