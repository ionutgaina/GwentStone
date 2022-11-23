package thegame.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CardInput;
import fileio.Coordinates;
import lombok.Data;
import thegame.play.Game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

@Data
public final class Minion extends CardInput {

    //! states
    @JsonIgnore
    private boolean frozen = false;
    @JsonIgnore
    private boolean fought = false;

    //! pasive stats
    @JsonIgnore
    private boolean tank = false;


    public Minion(final CardInput card) {
        super(card);
        if (this.getName().equals("Goliath") || this.getName().equals("Warden")) {
            this.tank = true;
        }
    }

    /**
     * Selecting correct ability for the minion
     *
     * @param target coordinates in x and y for the target card from playground
     */
    public void useAbilityCard(final Coordinates target) {
        Game game = Game.getInstance();
        Minion targetMinion = game.getTable().getCard(target.getX(), target.getY());
        switch (this.getName()) {
            case "Disciple":
                abilityGodsPlan(targetMinion);
                break;
            case "The Ripper":
                abilityWeakKnees(targetMinion);
                break;
            case "Miraj":
                abilitySkyJack(targetMinion);
                break;

            case "The Cursed One":
                abilityShapeshift(targetMinion, game.getTable().getRow(target.getX()));
                break;
            default:
        }
        this.setFought(true);
    }

    private void abilityGodsPlan(final Minion targetMinion) {
        targetMinion.setHealth(targetMinion.getHealth() + 2);
    }

    private void abilityWeakKnees(final Minion targetMinion) {
        targetMinion.setAttackDamage(Math.max(targetMinion.getAttackDamage() - 2, 0));
    }

    private void abilitySkyJack(final Minion targetMinion) {
        int swapHealth = targetMinion.getHealth();
        targetMinion.setHealth(this.getHealth());
        this.setHealth(swapHealth);
    }

    private void abilityShapeshift(final Minion targetMinion, final ArrayList<Minion> row) {
        int swapHealth = targetMinion.getHealth();
        targetMinion.setHealth(targetMinion.getAttackDamage());
        targetMinion.setAttackDamage(swapHealth);
        if (targetMinion.getHealth() <= 0) {
            row.remove(targetMinion);
        }
    }

    /**
     * Attack method for hero
     *
     * @param heroTarget which hero the minion targeted
     */
    public void attackHero(final Hero heroTarget) {
        heroTarget.setHealth(heroTarget.getHealth() - this.getAttackDamage());
        this.setFought(true);
    }

    /**
     * Attack method for the minion
     *
     * @param target coordinates in x and y for target from the board
     */
    public void attackCard(final Coordinates target) {
        Game game = Game.getInstance();
        Minion targetCard = game.getTable().getCard(target.getX(), target.getY());
        targetCard.setHealth(targetCard.getHealth() - this.getAttackDamage());
        if (targetCard.getHealth() <= 0) {
            ArrayList<Minion> targetCardRow = game.getTable().getRow(target.getX());
            System.out.println(targetCardRow);
            targetCardRow.remove(targetCard);
            System.out.println(targetCardRow);
        }
        this.setFought(true);
    }

    private void unfrozen() {
        frozen = false;
    }

    private void relax() {
        fought = false;
    }

    /**
     * Reseting states for the minion
     */
    public void resetStates() {
        unfrozen();
        relax();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), frozen, fought, tank);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Minion minion = (Minion) o;
        return frozen == minion.frozen
                && fought == minion.fought
                && tank == minion.tank
                && this.getName().equals(minion.getName())
                && this.getHealth() == minion.getHealth()
                && this.getAttackDamage() == minion.getAttackDamage();
    }

    /**
     * Comparator for minion health
     */
    protected static final class HealthComparator implements Comparator<Minion> {
        @Override
        public int compare(final Minion o1, final Minion o2) {
            return o1.getHealth() - o2.getHealth();
        }
    }

    /**
     * Comparator for minion AttackDamage
     */
    protected static final class AttackComparator implements Comparator<Minion> {
        @Override
        public int compare(final Minion o1, final Minion o2) {
            return o1.getAttackDamage() - o2.getAttackDamage();
        }
    }
}
