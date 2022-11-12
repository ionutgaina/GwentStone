package thegame.play;

import lombok.Data;
import thegame.cards.Minion;

import java.util.ArrayList;

@Data
public class Round {
    private static final int FIRST_TURN = 0;
    private static final int LAST_TURN = 1;
    private static final int FIRST_PLAYER = 1;
    private static final int SECOND_PLAYER = 2;

    private int playerStart;
    private int nrOfTurn = FIRST_TURN;

    public Round(int playerStart) {
        this.playerStart = playerStart;
    }

    public int getPlayerActive() {
        if (nrOfTurn == FIRST_TURN)
            return playerStart;
        return (playerStart == 1 ? SECOND_PLAYER : FIRST_PLAYER);
    }

    public int getNrOfTurn() {
        return nrOfTurn;
    }

    public void nextTurn() {
        Game game = Game.getInstance();
        ArrayList<Minion> row;
        int currentPlayerIdx = getPlayerActive();

        //! reset current player firstRow
        row = game.getTable().getFirstRow(currentPlayerIdx);
        row.forEach(Minion::resetStates);

        //! reset current player backRow
        row = game.getTable().getBackRow(currentPlayerIdx);
        row.forEach(Minion::resetStates);

        nrOfTurn++;
        nrOfTurn %= 2;
    }
}
