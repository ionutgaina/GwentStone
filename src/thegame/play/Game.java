package thegame.play;

import fileio.CardInput;
import lombok.Data;

@Data
public class Game {

    private Player playerOne;
    private Player playerTwo;

    private int nrOfRounds = 0;
    private Round round;
    private Table table;

    //! Singleton pattern
    private static Game instance = null;
    private Game() {}
    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public void setPlayers(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public Player getPlayer(int playerIdx) {
        return (playerIdx == 1 ? playerOne : playerTwo);
    }
    public Player getEnemyPlayer(int playerIdx) { return (playerIdx == 1 ? playerTwo : playerOne); }

    public Round getRound() {
        return round;
    }

    public Table getTable() {
        return table;
    }

    public void nextTurn() {
        round.nextTurn();
        if (round.getNrOfTurn() == 0 )
        {
            nextRound();
        }
    }

    public void nextRound() {
        nrOfRounds++;
        playerOne.drawCard();
        playerOne.increaseMana(Math.min(nrOfRounds, 10));

        playerTwo.drawCard();
        playerTwo.increaseMana(Math.min(nrOfRounds, 10));
    }

    public void newGame(int startingPlayer) {
        round = new Round(startingPlayer);
        nrOfRounds = 0;
        nextRound();

        table = new Table();
    }

    public void winGame(int playerIdx) {
        Player winner = getPlayer(playerIdx);
        winner.increaseWinnings();
    }
}
