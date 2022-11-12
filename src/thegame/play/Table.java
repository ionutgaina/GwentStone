package thegame.play;

import lombok.Data;
import thegame.cards.Minion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class Table {
    private ArrayList<Minion> firstRow = new ArrayList<>();
    private ArrayList<Minion> secondRow = new ArrayList<>();

    private ArrayList<Minion> thirdRow = new ArrayList<>();
    private ArrayList<Minion> fourthRow = new ArrayList<>();

    public ArrayList<Minion> getRow(int index) {
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

    public void setRow(int index, ArrayList<Minion> row) {
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

    public Minion getCard(int x, int y) {
        ArrayList<Minion> row = getRow(x);
        if (row.size() <= y) return null;
        return row.get(y);
    }

    public boolean putCardOnTable(Minion card, int playerIdx) {
        ArrayList<Minion> row;
        String[] backRowMinions = {"Sentinel", "Berserker", "The Cursed One", "Disciple"};
        List<String> list = Arrays.asList(backRowMinions);

        if (list.contains(card.getName())) row = getBackRow(playerIdx);
        else row = getFirstRow(playerIdx);

        if (isFull(row)) return false;
        row.add(card);
        return true;
    }

    public boolean isEnemyRow(int affectedRow, int playerIdx) {
        if (playerIdx == 1) {
            return affectedRow == 0 || affectedRow == 1;
        }
        return affectedRow == 2 || affectedRow == 3;
    }

    public ArrayList<Minion> getBackRow(int playerIdx) {
        if (playerIdx == 1) return getRow(3);
        return getRow(0);
    }

    public ArrayList<Minion> getFirstRow(int playerIdx) {
        if (playerIdx == 1) return getRow(2);
        return getRow(1);
    }

    public boolean isFull(ArrayList<Minion> row) {
        return row.size() >= 5;
    }

    public ArrayList<Minion> getReflectedRow(int index) {
        switch (index) {
            case 0:
                return getRow(3);
            case 1:
                return getRow(2);
            case 2:
                return getRow(1);
            case 3:
                return getRow(0);
            default:
                return new ArrayList<>();
        }
    }

    public ArrayList<Minion> getFrozenMinionRow(int rowIdx) {
        ArrayList<Minion> row = getRow(rowIdx);
        ArrayList<Minion> frozenRow = new ArrayList<>();

        row.forEach(minion -> {
            if (minion.isFrozen()) frozenRow.add(minion);
        });
        return frozenRow;
    }

    public ArrayList<Minion> getFrozenMinionTable() {
        final int MAXROW = 3;
        ArrayList<Minion> allFrozenCards = new ArrayList<>();
        for (int i = 0 ; i <= MAXROW; i++) {
            Arrays.fill(new ArrayList[]{allFrozenCards}, getFrozenMinionRow(i));
        }
        return allFrozenCards;
    }

    public boolean enemyIsHavingTank(int activePlayerIdx) {
        ArrayList<Minion> row;
        int enemyPlayerIdx = 1;

        if(activePlayerIdx == 1) {
            enemyPlayerIdx = 2;
        }

        //! Verify if firstRow of enemey is having Tank Minion
        row = getFirstRow(enemyPlayerIdx);
        for (Minion minion: row){
            if(minion.isTank())
                return true;
        }

        //! Verify if backRow of enemy is having Tank Minion
        row = getBackRow(enemyPlayerIdx);
        for (Minion minion: row){
            if(minion.isTank())
                return true;
        }
        return false;
    }

}
