package thegame.play;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CardInput;
import lombok.Data;
import thegame.cards.Environment;

import java.util.ArrayList;

@Data
public class Hand {
    private ArrayList<CardInput> cards;

    @JsonIgnore
    private int nrOfCards = 0;

    public Hand() {
        this.cards = new ArrayList<>();
    }
    public Hand(Hand hand) {
        cards = new ArrayList<>(hand.getCards());
        nrOfCards = cards.size();
    }

    public ArrayList<CardInput> getCards() {
        return cards;
    }

    public void addCard(CardInput card) {
        cards.add(card);
        nrOfCards++;
    }

    public void removeCard(int cardIdx) {
        cards.remove(cardIdx);
        nrOfCards--;
    }

    public ArrayList<Environment> getEnvironmentCards() {
        ArrayList<Environment> environmentCards = new ArrayList<>();

        cards.forEach( card -> {
            if (card.getCardType().equals("Environment"))
                environmentCards.add(new Environment(card));
        });

        return environmentCards;
    }
}
