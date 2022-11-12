package thegame.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CardInput;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

    @Override
    public String whatTypeCardIAm() {
        return "Environment";
    }
}
