package thegame.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CardInput;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Hero extends CardInput {
    private static final int HERO_MAX_HEALTH = 30;

    @JsonIgnore
    private int attackDamage;

    public Hero(CardInput card) {
        super(card);
        setHealth(HERO_MAX_HEALTH);
    }

    @Override
    public String whatTypeCardIAm() {
        return "Hero";
    }
}
