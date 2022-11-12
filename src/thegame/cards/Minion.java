package thegame.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CardInput;
import fileio.Coordinates;
import lombok.Data;
import lombok.EqualsAndHashCode;
import thegame.play.Game;

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
