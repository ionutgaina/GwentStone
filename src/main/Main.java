package main;

import checker.Checker;
import checker.CheckerConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.GameInput;
import fileio.Input;
import fileio.StartGameInput;
import thegame.CommandUtility;
import thegame.play.Game;
import thegame.play.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     *
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Input inputData = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + filePath1),
                Input.class);

        ArrayNode output = objectMapper.createArrayNode();

        Player playerOne = new Player(1);
        playerOne.setDecks(inputData.getPlayerOneDecks());

        Player playerTwo = new Player(2);
        playerTwo.setDecks(inputData.getPlayerTwoDecks());

        Game theGame = Game.getInstance();
        theGame.setPlayerOne(playerOne);
        theGame.setPlayerTwo(playerTwo);

        for (GameInput game : inputData.getGames()) {
            StartGameInput startGame = game.getStartGame();

            playerOne.newGame(
                    startGame.getPlayerOneDeckIdx(),
                    startGame.getShuffleSeed(),
                    startGame.getPlayerOneHero());
            playerTwo.newGame(
                    startGame.getPlayerTwoDeckIdx(),
                    startGame.getShuffleSeed(),
                    startGame.getPlayerTwoHero());

            theGame.newGame(startGame.getStartingPlayer());

            for (ActionsInput action : game.getActions()) {
                ObjectNode actionOutput = CommandUtility.commandAction(action);
                if (actionOutput != null) {
                    output.add(actionOutput);
                }
            }
        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }
}
