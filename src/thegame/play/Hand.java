package thegame.play;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CardInput;
import lombok.Data;
import thegame.cards.Environment;

import java.util.ArrayList;

@Data
public final class Hand {
    private ArrayList<CardInput> cards;

    @JsonIgnore
    private int nrOfCards = 0;

    public Hand() {
        this.cards = new ArrayList<>();
    }

    public Hand(final Hand hand) {
        cards = new ArrayList<>(hand.getCards());
        nrOfCards = cards.size();
    }

    /**
     * add a card in hand
     * @param card card to add
     */
    public void addCard(final CardInput card) {
        cards.add(card);
        nrOfCards++;
    }

    /**
     * remove a card from hand
     * @param cardIdx index card to discard
     */
    public void removeCard(final int cardIdx) {
        cards.remove(cardIdx);
        nrOfCards--;
    }

    /**
     * get environment cards from hand
     * @return a list of environments card
     */
    public ArrayList<Environment> getEnvironmentCards() {
        ArrayList<Environment> environmentCards = new ArrayList<>();

        cards.forEach(card -> {
            if (card.getCardType().equals("Environment")) {
                environmentCards.add(new Environment(card));
            }
        });

        return environmentCards;
    }
}
