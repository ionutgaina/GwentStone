package thegame.play;

import lombok.Data;

@Data
public final class Game {
    private static final int MAX_ROUNDS = 10;

    //! Singleton pattern
    private static Game instance = null;
    private Player playerOne;
    private Player playerTwo;
    private int nrOfRounds = 0;
    private Round round;
    private Table table;

    private Game() {
    }

    /**
     * Singleton instance
     * @return instance of the game
     */
    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    /**
     * get Player with index
     * @param playerIdx index of the Player
     * @return the Player instance
     */
    public Player getPlayer(final int playerIdx) {
        return (playerIdx == 1 ? playerOne : playerTwo);
    }

    /**
     * get enemy Player with index
     * @param playerIdx index of the Player
     * @return the enemy Player instance
     */
    public Player getEnemyPlayer(final int playerIdx) {
        return (playerIdx == 1 ? playerTwo : playerOne);
    }

    /**
     * next turn method
     */
    public void nextTurn() {
        round.nextTurn();
        if (round.getNrOfTurn() == 0) {
            nextRound();
        }
    }

    /**
     * next round method
     */
    public void nextRound() {
        nrOfRounds++;
        playerOne.drawCard();
        playerOne.increaseMana(Math.min(nrOfRounds, MAX_ROUNDS));

        playerTwo.drawCard();
        playerTwo.increaseMana(Math.min(nrOfRounds, MAX_ROUNDS));
    }

    /**
     * start a new game
     * @param startingPlayer index of the starting player
     */
    public void newGame(final int startingPlayer) {
        round = new Round(startingPlayer);
        nrOfRounds = 0;
        nextRound();

        table = new Table();
    }

    /**
     * method when a player won
     * @param playerIdx player index which won
     */
    public void winGame(final int playerIdx) {
        Player winner = getPlayer(playerIdx);
        winner.increaseWinnings();
    }
}
