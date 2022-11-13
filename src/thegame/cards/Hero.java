package thegame.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CardInput;
import lombok.Data;
import lombok.EqualsAndHashCode;
import thegame.Comparators;
import thegame.play.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@Data
@EqualsAndHashCode(callSuper = false)
public class Hero extends CardInput {

    @JsonIgnore
    private int attackDamage;

    //! states
    @JsonIgnore
    private boolean fought = false;

    public Hero(CardInput card) {
        super(card);
    }

   @JsonIgnore
    public boolean isDead() {
        return this.getHealth() <= 0;
    }

    public void relax() {
        this.fought = false;
    }

    public void useAbilityHero(int affectedRow, Table table) {
        switch (this.getName()) {
            case "Lord Royce":
                abilitySubZero(affectedRow, table);
                break;
            case "Empress Thorina":
                abilityLowBlow(affectedRow, table);
                break;
            case "King Mudface":
                abilityEarthBorn(affectedRow, table);
                break;
            case "General Kocioraw":
                abilityBloodThirst(affectedRow, table);
                break;
            default:
                return;
        }
        this.setFought(true);
    }

    private void abilitySubZero(int affectedRow, Table table) {
        ArrayList<Minion> row = table.getRow(affectedRow);
        Minion bigAttackMinion = Collections.max(row, new Comparators.AttackComparator());

        bigAttackMinion.setFrozen(true);
    }

    private void abilityLowBlow(int affectedRow, Table table) {
        ArrayList<Minion> row = table.getRow(affectedRow);

        Minion bigHealthMinion = Collections.max(row, new Comparators.HealthComparator());
        row.remove(bigHealthMinion);
    }

    private void abilityEarthBorn(int affectedRow, Table table) {
        ArrayList<Minion> row = table.getRow(affectedRow);
        row.forEach(minion -> minion.setHealth(minion.getHealth() + 1));
    }

    private void abilityBloodThirst(int affectedRow, Table table) {
        ArrayList<Minion> row = table.getRow(affectedRow);
        row.forEach(minion -> minion.setAttackDamage(minion.getAttackDamage() + 1));
    }

}
