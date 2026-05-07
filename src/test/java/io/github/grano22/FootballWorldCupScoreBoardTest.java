package io.github.grano22;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FootballWorldCupScoreBoardTest {
    private static final String TEST_HOME_TEAM_NAME = "HomeTeam";
    private static final String TEST_AWAY_TEAM_NAME = "AwayTeam";

    @Test
    public void scoreBoardIsEmptyWhenNoGamesStarted() {
        // Arrange
        final var footballWorldCupScoreBoard = new FootballWorldCupScoreBoard();

        // Assert & Act
        assertEquals("", footballWorldCupScoreBoard.getASummaryOfGamesByTotalScore());
    }

    @Test
    public void scoreBoardHasShowedRegisteredInitialMatchCorrectly() {
        // Arrange
        final var footballWorldCupScoreBoard = new FootballWorldCupScoreBoard();

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
        final var footballWorldCupScoreBoard = new FootballWorldCupScoreBoard();

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
        final var footballWorldCupScoreBoard = new FootballWorldCupScoreBoard();
        startGames(footballWorldCupScoreBoard, List.of(
            Map.entry("Mexico", "Canada"),
            Map.entry("Spain", "Brazil"),
            Map.entry("Germany", "France"),
            Map.entry("Uruguay", "Italy"),
            Map.entry("Argentina", "Australia"),
            Map.entry("Japan", "Romania")
        ));

        // Act
        updateScores(
            footballWorldCupScoreBoard,
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
        final var footballWorldCupScoreBoard = new FootballWorldCupScoreBoard();
        startGames(footballWorldCupScoreBoard, List.of(
            Map.entry("Mexico", "Canada"),
            Map.entry("Spain", "Brazil"),
            Map.entry("Germany", "France"),
            Map.entry("Uruguay", "Italy"),
            Map.entry("Argentina", "Australia"),
            Map.entry("Japan", "Romania")
        ));
        updateScores(
            footballWorldCupScoreBoard,
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

    @Test
    public void userCannotRegisterSameMatchAtTheSameTimeTwice() {
        // Arrange
        final var footballWorldCupScoreBoard = new FootballWorldCupScoreBoard();

        // Act
        footballWorldCupScoreBoard.startGame("Mexico", "Canada");
        final var exception =  assertThrows(
                IllegalArgumentException.class,
                () -> footballWorldCupScoreBoard.startGame("Mexico", "Canada")
        );

        // Assert
        assertEquals("Match between Mexico and Canada already registered", exception.getMessage());
    }

    @Test
    public void userCannotRegisterTwoDifferentMatchesForTheSameTeam() {
        // Arrange
        final var footballWorldCupScoreBoard = new FootballWorldCupScoreBoard();

        // Act
        footballWorldCupScoreBoard.startGame("Mexico", "Canada");
        final var exception =  assertThrows(
                IllegalArgumentException.class,
                () -> footballWorldCupScoreBoard.startGame("Mexico", "Japan")
        );

        // Assert
        assertEquals("Team Mexico is already registered", exception.getMessage());
    }

    @Test
    public void matchCannotBeRemovedFromTheScoreBoardWhenItIsNotAddedYet() {
        // Arrange
        final var footballWorldCupScoreBoard = new FootballWorldCupScoreBoard();

        // Act
        final var exception =  assertThrows(
                IllegalArgumentException.class,
                () -> footballWorldCupScoreBoard.finishGame("Mexico", "Japan")
        );

        // Assert
        assertEquals("Match between Mexico and Japan is not registered", exception.getMessage());
    }

    @Test
    public void teamCannotHaveUpdatedScoreInTheMatchWhenItDoesNotPlayAnyMatch() {
        // Arrange
        final var footballWorldCupScoreBoard = new FootballWorldCupScoreBoard();

        // Act
        final var exception =  assertThrows(
                IllegalArgumentException.class,
                () -> footballWorldCupScoreBoard.updateScore("Mexico", 11)
        );

        // Assert
        assertEquals("Team Mexico is not registered", exception.getMessage());
    }

    public static Stream<Arguments> provideInvalidTeamNames() {
        return Stream.of(
            Arguments.of(
                    "Home team name is null",
                    "Home team name is required",
                    NullPointerException.class,
                    null,
                    TEST_AWAY_TEAM_NAME
            ),
            Arguments.of(
                    "Away team name is null",
                    "Away team name is required",
                    NullPointerException.class,
                    TEST_HOME_TEAM_NAME,
                    null
            ),
            Arguments.of(
                    "Empty home team name",
                    "Home team name cannot be empty",
                    IllegalArgumentException.class,
                    "",
                    TEST_AWAY_TEAM_NAME
            ),
            Arguments.of(
                    "Empty away team name",
                    "Away team name cannot be empty",
                    IllegalArgumentException.class,
                    TEST_HOME_TEAM_NAME,
                    ""
            )
        );
    }

    @ParameterizedTest(name = "Edge Case {index}: {0}, expected success={1}")
    @MethodSource("provideInvalidTeamNames")
    public void matchCannotBeAddedToTheScoreBoardIfInvalidTeamNamesWereProvided(
            String testName,
            String expectedErrorMessage,
            Class<? extends Exception> expectedExceptionType,
            String homeTeamName,
            String awayTeamName
    ) {
        // Arrange
        final var footballWorldCupScoreBoard = new FootballWorldCupScoreBoard();

        // Act
        final var exception =  assertThrows(
                expectedExceptionType,
                () -> footballWorldCupScoreBoard.startGame(homeTeamName, awayTeamName)
        );

        // Assert
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    public static Stream<Arguments> provideInvalidScoreValues() {
        return Stream.of(
                Arguments.of(
                    "Empty team name",
                        "Team name cannot be empty",
                        IllegalArgumentException.class,
                        "",
                        12
                ),
                Arguments.of(
                        "Negative score",
                        "Score cannot be negative",
                        IllegalArgumentException.class,
                        TEST_HOME_TEAM_NAME,
                        -1
                ),
                Arguments.of(
                        "Negative score",
                        "Score cannot be lower than previous score",
                        IllegalArgumentException.class,
                        TEST_AWAY_TEAM_NAME,
                        2
                )
        );
    }

    @ParameterizedTest(name = "Edge Case {index}: {0}, expected success={1}")
    @MethodSource("provideInvalidScoreValues")
    public void matchScoreCannotBeUpdatedWhenInvalidScoreValuesWereProvided(
            String testName,
            String expectedErrorMessage,
            Class<? extends Exception> expectedExceptionType,
            String teamToUpdateScore,
            int teamScoreToUpdate
    ) {
        // Arrange
        final var footballWorldCupScoreBoard = new FootballWorldCupScoreBoard();

        // Act
        footballWorldCupScoreBoard.startGame(TEST_HOME_TEAM_NAME, TEST_AWAY_TEAM_NAME);
        footballWorldCupScoreBoard.updateScore(TEST_AWAY_TEAM_NAME, 3);
        final var exception =  assertThrows(
                expectedExceptionType,
                () -> footballWorldCupScoreBoard.updateScore(teamToUpdateScore, teamScoreToUpdate)
        );

        // Assert
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @ParameterizedTest(name = "Edge Case {index}: {0}, expected success={1}")
    @MethodSource("provideInvalidTeamNames")
    public void gameCannotBeFinishedWhenInvalidValuesWereProvided(
            String testName,
            String expectedErrorMessage,
            Class<? extends Exception> expectedExceptionType,
            String homeTeamName,
            String awayTeamName
    ) {
        // Arrange
        final var footballWorldCupScoreBoard = new FootballWorldCupScoreBoard();

        // Act
        footballWorldCupScoreBoard.startGame(TEST_HOME_TEAM_NAME, TEST_AWAY_TEAM_NAME);
        footballWorldCupScoreBoard.updateScore(TEST_AWAY_TEAM_NAME, 3);
        final var exception =  assertThrows(
                expectedExceptionType,
                () -> footballWorldCupScoreBoard.finishGame(homeTeamName, awayTeamName)
        );

        // Assert
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    private void startGames(final FootballWorldCupScoreBoard gameBoard, List<Map.Entry<String, String>> gamesToStart) {
        for (final var game : gamesToStart) {
            gameBoard.startGame(game.getKey(), game.getValue());
        }
    }

    @SafeVarargs
    public final void updateScores(final FootballWorldCupScoreBoard gameBoard, Map.Entry<String, Integer>... teamScoresToUpdate) {
        for (final var teamScore : teamScoresToUpdate) {
            gameBoard.updateScore(teamScore.getKey(), Objects.requireNonNull(teamScore.getValue(), "Score cannot be null"));
        }
    }
}
