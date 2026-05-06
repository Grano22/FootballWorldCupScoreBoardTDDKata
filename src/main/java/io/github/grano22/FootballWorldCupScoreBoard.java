package io.github.grano22;

import org.jspecify.annotations.NonNull;

import java.util.*;

public final class FootballWorldCupScoreBoard {
    public record MatchTeams(@NonNull String homeTeamName, @NonNull String awayTeamName) {}
    public record MatchScore(int homeTeamScore, int awayTeamScore) {}

    private final LinkedHashMap<MatchTeams, MatchScore> games = new LinkedHashMap<>();
    private final Map<String, MatchTeams> matchRefPerTeam = new HashMap<>();

    public void startGame(@NonNull String homeTeamName, @NonNull String awayTeamName) {
        final var matchTeams = new MatchTeams(homeTeamName, awayTeamName);
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

    @SafeVarargs
    public final void updateScore(Map.Entry<String, Integer>... teamScoresToUpdate) {
        for (final var teamScore : teamScoresToUpdate) {
            final var matchRef = matchRefPerTeam.get(teamScore.getKey());
            games.compute(
                    matchRef,
                    (matchTeams, matchScore) ->
                            teamScore.getKey().equals(matchTeams.homeTeamName) ?
                                new MatchScore(
                                        teamScore.getValue(),
                                        Optional.ofNullable(matchScore).map(s -> s.awayTeamScore).orElse(0)
                                ) :
                                new MatchScore(
                                        Optional.ofNullable(matchScore).map(s -> s.homeTeamScore).orElse(0),
                                        teamScore.getValue()
                                )
            );
        }
    }

    public void finishGame(@NonNull String homeTeamName, @NonNull String awayTeamName) {
        games.remove(new MatchTeams(homeTeamName, awayTeamName));
        matchRefPerTeam.remove(homeTeamName);
        matchRefPerTeam.remove(awayTeamName);
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
}
