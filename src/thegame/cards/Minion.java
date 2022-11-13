package thegame.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CardInput;
import fileio.Coordinates;
import lombok.Data;
import lombok.EqualsAndHashCode;
import thegame.play.Game;
import thegame.play.Table;

import java.util.ArrayList;

@Data
@EqualsAndHashCode(callSuper = false)
public class Minion extends CardInput {

    //! states
    @JsonIgnore
    private boolean frozen = false;
    @JsonIgnore
    private boolean fought = false;

    //! pasive stats
    @JsonIgnore
    private boolean tank = false;


    public Minion(CardInput card) {
        super(card);
        if(this.getName().equals("Goliath") || this.getName().equals("Warden"))
            this.tank = true;
    }

    public void useAbilityCard(Coordinates target) {
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
    private void abilityGodsPlan(Minion targetMinion) {
        targetMinion.setHealth(targetMinion.getHealth() + 2);
    }

    private void abilityWeakKnees(Minion targetMinion) {
        targetMinion.setAttackDamage(Math.max(targetMinion.getAttackDamage() - 2, 0));
    }

    private void abilitySkyJack(Minion targetMinion) {
        int swapHealth = targetMinion.getHealth();
        targetMinion.setHealth(this.getHealth());
        this.setHealth(swapHealth);
    }

    private void abilityShapeshift(Minion targetMinion, ArrayList<Minion> row) {
        int swapHealth = targetMinion.getHealth();
        targetMinion.setHealth(targetMinion.getAttackDamage());
        targetMinion.setAttackDamage(swapHealth);
        if (targetMinion.getHealth() <= 0)
            row.remove(targetMinion);
    }
    public void attackHero(Hero heroTarget){
        heroTarget.setHealth(heroTarget.getHealth() - this.getAttackDamage());
        this.setFought(true);
    }

    public void attackCard(Coordinates target){
        Game game = Game.getInstance();
        Minion targetCard = game.getTable().getCard(target.getX(), target.getY());
        targetCard.setHealth(targetCard.getHealth() - this.getAttackDamage());
        if(targetCard.getHealth() <= 0)
        {
            ArrayList<Minion> targetCardRow = game.getTable().getRow(target.getX());
            targetCardRow.remove(targetCard);
        }
        this.setFought(true);
    }

    public void unfrozen() {
        frozen = false;
    }

    public void relax() { fought = false; }

    public void resetStates() {
        unfrozen();
        relax();
    }
}
