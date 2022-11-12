package thegame.play;

import fileio.Coordinates;
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

    private ArrayList<Minion> getRow(int index) {
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

    private Minion getCard(Coordinates coordinates) {
        ArrayList<Minion> row = getRow(coordinates.getY());
        return row.get(coordinates.getX());
    }

    public boolean putCardOnTable(Minion card, int playerIdx) {
        ArrayList<Minion> row;
        String[] backRowMinions = {"Sentinel", "Berserker", "The Cursed One", "Disciple"};
        List<String> list = Arrays.asList(backRowMinions);

        if (list.contains(card.getName())) {
            row = getBackRow(playerIdx);
            if (row.size() >= 5)
                return false;
            row.add(card);
        } else {
            row = getFirstRow(playerIdx);
            if (row.size() >= 5)
                return false;
            row.add(card);
        }
        return true;
    }

    public ArrayList<Minion> getBackRow(int playerIdx) {
        if (playerIdx == 1)
            return getRow(3);
        return getRow(0);
    }

    public ArrayList<Minion> getFirstRow(int playerIdx) {
        if (playerIdx == 1)
            return getRow(2);
        return getRow(1);
    }
}
