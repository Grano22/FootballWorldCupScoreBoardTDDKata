package io.github.grano22;

import org.junit.jupiter.api.Test;

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
}
