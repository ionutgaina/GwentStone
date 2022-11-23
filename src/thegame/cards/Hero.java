package thegame.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CardInput;
import lombok.Data;
import lombok.EqualsAndHashCode;
import thegame.play.Table;

import java.util.ArrayList;
import java.util.Collections;

@Data
@EqualsAndHashCode(callSuper = false)
public final class Hero extends CardInput {

    @JsonIgnore
    private int attackDamage;

    //! states
    @JsonIgnore
    private boolean fought = false;

    public Hero(final CardInput card) {
        super(card);
    }

    /**
     * Method which is checking the hero dead state
     *
     * @return returning true if Hero is dead, otherwise false
     */
    @JsonIgnore
    public boolean isDead() {
        return this.getHealth() <= 0;
    }

    /**
     * method where is reseting the fought state
     */
    public void relax() {
        this.fought = false;
    }

    /**
     * Selecting the correct ability for the hero card
     *
     * @param affectedRow targeted row for ability
     * @param table       playground of the game
     */
    public void useAbilityHero(final int affectedRow, final Table table) {
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

    private void abilitySubZero(final int affectedRow, final Table table) {
        ArrayList<Minion> row = table.getRow(affectedRow);
        Minion bigAttackMinion = Collections.max(row, new Minion.AttackComparator());

        bigAttackMinion.setFrozen(true);
    }

    private void abilityLowBlow(final int affectedRow, final Table table) {
        ArrayList<Minion> row = table.getRow(affectedRow);

        Minion bigHealthMinion = Collections.max(row, new Minion.HealthComparator());
        row.remove(bigHealthMinion);
    }

    private void abilityEarthBorn(final int affectedRow, final Table table) {
        ArrayList<Minion> row = table.getRow(affectedRow);
        row.forEach(minion -> minion.setHealth(minion.getHealth() + 1));
    }

    private void abilityBloodThirst(final int affectedRow, final Table table) {
        ArrayList<Minion> row = table.getRow(affectedRow);
        row.forEach(minion -> minion.setAttackDamage(minion.getAttackDamage() + 1));
    }

}
