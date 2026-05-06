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

    @Test
    public void scoreBoardHasShowedUpdatedScoreCorrectly() {
        // Arrange
        var footballWorldCupScoreBoard = new FootballWorldCupScoreBoard();
        startGames(footballWorldCupScoreBoard, List.of(
            Map.entry("Mexico", "Canada"),
            Map.entry("Spain", "Brazil"),
            Map.entry("Germany", "France"),
            Map.entry("Uruguay", "Italy"),
            Map.entry("Argentina", "Australia"),
            Map.entry("Japan", "Romania")
        ));

        // Act
        footballWorldCupScoreBoard.updateScore(
            Map.entry("Spain", 12),
            Map.entry("France", 4),
            Map.entry("Germany", 9),
            Map.entry("Romania", 8),
            Map.entry("Argentina", 4),
            Map.entry("Australia", 3),
            Map.entry("Mexico", 7)
        );

        // Assert
        assertEquals(
                """
                Germany 9 - France 4
                Spain 12 - Brazil 0
                Japan 0 - Romania 8
                Argentina 4 - Australia 3
                Mexico 7 - Canada 0
                Uruguay 0 - Italy 0""",
                footballWorldCupScoreBoard.getASummaryOfGamesByTotalScore()
        );
    }

    @Test
    public void scoreBoardWasUpdatedSuccessfullyAfterRemovingSomeGames() {
        // Arrange
        var footballWorldCupScoreBoard = new FootballWorldCupScoreBoard();
        startGames(footballWorldCupScoreBoard, List.of(
            Map.entry("Mexico", "Canada"),
            Map.entry("Spain", "Brazil"),
            Map.entry("Germany", "France"),
            Map.entry("Uruguay", "Italy"),
            Map.entry("Argentina", "Australia"),
            Map.entry("Japan", "Romania")
        ));
        footballWorldCupScoreBoard.updateScore(
            Map.entry("Spain", 12),
            Map.entry("France", 4),
            Map.entry("Germany", 9),
            Map.entry("Romania", 8),
            Map.entry("Argentina", 4),
            Map.entry("Australia", 3),
            Map.entry("Mexico", 7)
        );

        // Act
        footballWorldCupScoreBoard.finishGame("Argentina", "Australia");
        footballWorldCupScoreBoard.finishGame("Spain", "Brazil");
        footballWorldCupScoreBoard.finishGame("Uruguay", "Italy");

        // Assert
        assertEquals(
                """
                Germany 9 - France 4
                Japan 0 - Romania 8
                Mexico 7 - Canada 0""",
                footballWorldCupScoreBoard.getASummaryOfGamesByTotalScore()
        );
    }

    private void startGames(FootballWorldCupScoreBoard gameBoard, List<Map.Entry<String, String>> gamesToStart) {
        for (final var game : gamesToStart) {
            gameBoard.startGame(game.getKey(), game.getValue());
        }
    }
}
