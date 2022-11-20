package fileio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;

@Data
public class CardInput {
    private int mana;
    private int attackDamage;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;

    @JsonIgnore
    private String cardType = "CardInput";

    public CardInput() {
    }

    public CardInput(final CardInput card) {
        mana = card.getMana();
        attackDamage = card.getAttackDamage();
        health = card.getHealth();
        description = card.getDescription();
        colors = new ArrayList<>(card.getColors());
        name = card.getName();
        cardType = card.getCardType();
    }
}
