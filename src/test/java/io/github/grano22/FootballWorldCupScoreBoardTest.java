package io.github.grano22;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public class FootballWorldCupScoreBoardTest {
    @Test
    public void scoreBoardIsEmptyWhenNoGamesStarted() {
        // Arrange
        var footballWorldCupScoreBoard = new FootballWorldCupScoreBoard();

        // Assert & Act
        assertSame("", footballWorldCupScoreBoard.getASummaryOfGamesByTotalScore());
    }
}
