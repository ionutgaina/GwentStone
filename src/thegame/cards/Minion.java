package thegame.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CardInput;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Minion extends CardInput {

    //! effects
    @JsonIgnore
    private boolean frozen = false;

    public Minion(CardInput card) {
        super(card);
    }


    public void unfrozen() {
        frozen = false;
    }

    @Override
    public String whatTypeCardIAm() {
        return "Minion";
    }
}
