package io.github.grano22;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FootballWorldCupScoreBoardTest {
    @Test
    public void scoreBoardIsEmptyWhenNoGamesStarted() {
        // Arrange
        var footballWorldCupScoreBoard = new FootballWorldCupScoreBoard();

        // Assert & Act
        assertEquals("", footballWorldCupScoreBoard.getASummaryOfGamesByTotalScore());
    }

    @Test
    public void scoreBoardHasShowedRegisteredInitialMatchCorrectly() {
        // Arrange
        var footballWorldCupScoreBoard = new FootballWorldCupScoreBoard();

        // Act
        footballWorldCupScoreBoard.startGame("Mexico", "Canada");

        // Assert
        assertEquals(
                """
                Mexico 0 - Canada 0""",
                footballWorldCupScoreBoard.getASummaryOfGamesByTotalScore()
        );
    }

    @Test
    public void scoreBoardHasShowedRegisteredInitialMatchesOrderedByMostRecentlyAddedEntries() {
        // Arrange
        var footballWorldCupScoreBoard = new FootballWorldCupScoreBoard();

        // Act
        startGames(footballWorldCupScoreBoard, List.of(
            Map.entry("Mexico", "Canada"),
            Map.entry("Spain", "Brazil"),
            Map.entry("Germany", "France"),
            Map.entry("Uruguay", "Italy"),
            Map.entry("Argentina", "Australia")
        ));

        // Assert
        assertEquals(
                """
                Argentina 0 - Australia 0
                Uruguay 0 - Italy 0
                Germany 0 - France 0
                Spain 0 - Brazil 0
                Mexico 0 - Canada 0""",
                footballWorldCupScoreBoard.getASummaryOfGamesByTotalScore()
        );
    }

    private void startGames(FootballWorldCupScoreBoard gameBoard, List<Map.Entry<String, String>> gamesToStart) {
        for (final var game : gamesToStart) {
            gameBoard.startGame(game.getKey(), game.getValue());
        }
    }
}
