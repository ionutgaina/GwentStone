package thegame.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CardInput;
import lombok.Data;
import lombok.EqualsAndHashCode;
import thegame.play.Table;

import java.util.ArrayList;
import java.util.Comparator;

@Data
@EqualsAndHashCode(callSuper = false)
public class Environment extends CardInput {

    @JsonIgnore
    private int health;
    @JsonIgnore
    private int attackDamage;

    public Environment(CardInput card) {
        super(card);
    }

    public void useCardEffect(Table table, int affectedRow) {
        switch (this.getName()) {
            case "Firestorm":
                effectFirestorm(table, affectedRow);
                return;
            case "Winterfell":
                effectWinterfell(table, affectedRow);
                return;
            case "Heart Hound":
                effectHeartHound(table, affectedRow);
                return;
            default:
        }
    }

    private void effectFirestorm(Table table, int affectedRow) {
        ArrayList<Minion> row = table.getRow(affectedRow);
        row.forEach(minion -> minion.setHealth(minion.getHealth() - 2));
    }

    private void effectWinterfell(Table table, int affectedRow) {
        ArrayList<Minion> row = table.getRow(affectedRow);
        row.forEach(minion -> minion.setFrozen(true));
    }

    private void effectHeartHound(Table table, int affectedRow) {
        ArrayList<Minion> enemyRow = table.getRow(affectedRow);
        Minion bigHealthMinion = enemyRow.stream().max(Comparator.comparing(minion -> minion.getHealth())).get();

        ArrayList<Minion> friendlyRow = table.getReflectedRow(affectedRow);
        friendlyRow.add(bigHealthMinion);
        enemyRow.remove(bigHealthMinion);
    }

    @Override
    public String whatTypeCardIAm() {
        return "Environment";
    }
}
