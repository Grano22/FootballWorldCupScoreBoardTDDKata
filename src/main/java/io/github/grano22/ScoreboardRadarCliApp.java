package io.github.grano22;

import static java.lang.IO.println;
import static java.lang.IO.readln;

public class ScoreboardRadarCliApp {
    private final static FootballWorldCupScoreBoard scoreBoard = new FootballWorldCupScoreBoard();

    static void main() {
        println("--- Football World Cup Score Board CLI interface ---");
        println("Type 'exit' to quit, 'help' to see available commands.");

        while (true) {
            String input = readln("> ");

            if (input == null || input.equalsIgnoreCase("exit")) {
                println("Exiting...");
                break;
            }

            try {
                handleCommand(input.trim());
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    static void handleCommand(String command) {
        switch (command) {
            case "startGame" -> scoreBoard.startGame(readln("Home team name: "), readln("Away team name: "), "Unknown", "Unknown");
            case "updateScore" -> scoreBoard.updateScore(readln("Team name: "), Integer.parseInt(readln("Score: ")));
            case "finishGame" -> scoreBoard.finishGame(readln("Home team name: "), readln("Away team name: "));
            case "summary" -> println(scoreBoard.getASummaryOfGamesByTotalScore());
            case "help" -> println("Available commands: startGame, updateScore, finishGame, summary, help");
            default -> println("Invalid command");
        }
    }
}
