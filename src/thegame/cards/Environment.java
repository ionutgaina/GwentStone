package thegame.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CardInput;
import lombok.Data;
import lombok.EqualsAndHashCode;
import thegame.Comparators;
import thegame.play.Table;

import java.util.ArrayList;
import java.util.Collections;

@Data
@EqualsAndHashCode(callSuper = false)
public final class Environment extends CardInput {

    @JsonIgnore
    private int health;
    @JsonIgnore
    private int attackDamage;

    public Environment(final CardInput card) {
        super(card);
    }

    /**
     * Selecting the correct effect for the environment card
     * @param table       Playground of the game
     * @param affectedRow target row for ability
     */
    public void useCardEffect(final Table table, final int affectedRow) {
        switch (this.getName()) {
            case "Firestorm" -> {
                effectFirestorm(table, affectedRow);
            }
            case "Winterfell" -> {
                effectWinterfell(table, affectedRow);
            }
            case "Heart Hound" -> {
                effectHeartHound(table, affectedRow);
            }
            default -> {
            }
        }
    }

    private void effectFirestorm(final Table table, final int affectedRow) {
        ArrayList<Minion> row = table.getRow(affectedRow);
        ArrayList<Minion> aliveRow = new ArrayList<>();
        row.forEach(minion -> {
            minion.setHealth(minion.getHealth() - 1);
            if (minion.getHealth() > 0) {
                aliveRow.add(minion);
            }
        });

        table.setRow(affectedRow, aliveRow);
    }

    private void effectWinterfell(final Table table, final int affectedRow) {
        ArrayList<Minion> row = table.getRow(affectedRow);
        row.forEach(minion -> minion.setFrozen(true));
    }

    private void effectHeartHound(final Table table, final int affectedRow) {
        ArrayList<Minion> enemyRow = table.getRow(affectedRow);
        if (enemyRow != null) {
            Minion bigHealthMinion = Collections.max(enemyRow, new Comparators.HealthComparator());

            ArrayList<Minion> friendlyRow = table.getReflectedRow(affectedRow);
            friendlyRow.add(bigHealthMinion);
            enemyRow.remove(bigHealthMinion);
        }
    }
}
