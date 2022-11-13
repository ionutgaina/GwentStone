package thegame.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CardInput;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Hero extends CardInput {

    @JsonIgnore
    private int attackDamage;

    public Hero(CardInput card) {
        super(card);
    }

   @JsonIgnore
    public boolean isDead() {
        return this.getHealth() <= 0;
    }
}
