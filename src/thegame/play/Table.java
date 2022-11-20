package thegame.play;

import lombok.Data;
import thegame.cards.Minion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public final class Table {
    private static final int BACKROW_PLAYERONE = 3;
    private static final int MAX_MINIONS = 5;

    private ArrayList<Minion> firstRow = new ArrayList<>();
    private ArrayList<Minion> secondRow = new ArrayList<>();

    private ArrayList<Minion> thirdRow = new ArrayList<>();
    private ArrayList<Minion> fourthRow = new ArrayList<>();

    /**
     * get Row with a index
     * @param index index of row
     * @return the row
     */
    public ArrayList<Minion> getRow(final int index) {
        switch (index) {
            case 0:
                return firstRow;
            case 1:
                return secondRow;
            case 2:
                return thirdRow;
            default:
                return fourthRow;
        }
    }

    /**
     * set Row with another row
     * @param index index of the row
     * @param row row
     */
    public void setRow(final int index, final ArrayList<Minion> row) {
        switch (index) {
            case 0:
                firstRow = row;
                return;
            case 1:
                secondRow = row;
                return;
            case 2:
                thirdRow = row;
                return;
            default:
                fourthRow = row;
        }
    }

    /**
     * get Card with a x and y
     * @param x row
     * @param y column
     * @return minion card
     */
    public Minion getCard(final int x, final int y) {
        ArrayList<Minion> row = getRow(x);
        if (row.size() <= y) {
            return null;
        }
        return row.get(y);
    }

    /**
     * put a minion on table
     * @param card minion card
     * @param playerIdx active player index
     * @return true if card is putted, otherwise false
     */
    public boolean putCardOnTable(final Minion card, final int playerIdx) {
        ArrayList<Minion> row;
        String[] backRowMinions = {"Sentinel", "Berserker", "The Cursed One", "Disciple"};
        List<String> list = Arrays.asList(backRowMinions);

        if (list.contains(card.getName())) {
            row = getBackRow(playerIdx);
        } else {
            row = getFirstRow(playerIdx);
        }

        if (isFull(row)) {
            return false;
        }
        row.add(card);
        return true;
    }

    /**
     * verify if the row is enemy for active Player
     * @param affectedRow index of the target row
     * @param playerIdx index of the active player
     * @return get a boolean response if is enemy Row
     */
    public boolean isEnemyRow(final int affectedRow, final int playerIdx) {
        if (playerIdx == 1) {
            return affectedRow == 0 || affectedRow == 1;
        }
        return affectedRow == 2 || affectedRow == BACKROW_PLAYERONE;
    }

    /**
     * get the back row of the index player
     * @param playerIdx index of the player
     * @return the row
     */
    public ArrayList<Minion> getBackRow(final int playerIdx) {
        if (playerIdx == 1) {
            return getRow(BACKROW_PLAYERONE);
        }
        return getRow(0);
    }

    /**
     * get the first row of the index player
     * @param playerIdx index of the player
     * @return the row
     */
    public ArrayList<Minion> getFirstRow(final int playerIdx) {
        if (playerIdx == 1) {
            return getRow(2);
        }
        return getRow(1);
    }

    /**
     * verify if the row is full of minions
     * @param row the row with minions
     * @return true or false if the row is full
     */
    public boolean isFull(final ArrayList<Minion> row) {
        return row.size() >= MAX_MINIONS;
    }

    /**
     * get reflected row
     * @param index index of the row
     * @return the reflex of the row
     */
    public ArrayList<Minion> getReflectedRow(final int index) {
        switch (index) {
            case 0:
                return getRow(BACKROW_PLAYERONE);
            case 1:
                return getRow(2);
            case 2:
                return getRow(1);
            case BACKROW_PLAYERONE:
                return getRow(0);
            default:
                return new ArrayList<>();
        }
    }

    /**
     * get frozen minion from row
     * @param rowIdx index of the row
     * @return row of frozen minions
     */
    public ArrayList<Minion> getFrozenMinionRow(final int rowIdx) {
        ArrayList<Minion> row = getRow(rowIdx);
        ArrayList<Minion> frozenRow = new ArrayList<>();

        row.forEach(minion -> {
            if (minion.isFrozen()) {
                frozenRow.add(minion);
            }
        });
        return frozenRow;
    }

    /**
     * get all minions from the table
     * @return all minions from the table
     */
    public ArrayList<Minion> getFrozenMinionTable() {
        final int maxRow = 3;
        ArrayList<Minion> allFrozenCards = new ArrayList<>();
        for (int i = 0; i <= maxRow; i++) {
            Arrays.fill(new ArrayList[]{allFrozenCards}, getFrozenMinionRow(i));
        }
        return allFrozenCards;
    }

    /**
     * verify if the enemy is having a tank minion
     * @param activePlayerIdx active player index
     * @return boolean if the enemy is having a tank minion
     */
    public boolean enemyIsHavingTank(final int activePlayerIdx) {
        ArrayList<Minion> row;
        int enemyPlayerIdx = 1;

        if (activePlayerIdx == 1) {
            enemyPlayerIdx = 2;
        }

        //! Verify if firstRow of enemey is having Tank Minion
        row = getFirstRow(enemyPlayerIdx);
        for (final Minion minion : row) {
            if (minion.isTank()) {
                return true;
            }
        }

        //! Verify if backRow of enemy is having Tank Minion
        row = getBackRow(enemyPlayerIdx);
        for (final Minion minion : row) {
            if (minion.isTank()) {
                return true;
            }
        }
        return false;
    }

}
