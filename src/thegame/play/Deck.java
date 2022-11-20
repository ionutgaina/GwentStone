package thegame.play;

import fileio.CardInput;
import lombok.Data;
import thegame.cards.Environment;
import thegame.cards.Minion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


@Data
public final class Deck {
    private ArrayList<CardInput> cards;
    private int nrOfCards;

    public Deck() {
    }

    public Deck(final List<CardInput> cards) {
        this.cards = new ArrayList<>();
        String[] minions = {"Sentinel", "Berserker", "Goliath", "Warden", "The Ripper", "Miraj",
                "The Cursed" + " One", "Disciple"};
        String[] environments = {"Firestorm", "Winterfell", "Heart Hound"};
        List<String> minionsList = Arrays.asList(minions);
        List<String> environmentsList = Arrays.asList(environments);

        for (CardInput card : cards) {
            if (minionsList.contains(card.getName())) {
                card.setCardType("Minion");
                this.cards.add(new Minion(card));
            } else if (environmentsList.contains(card.getName())) {
                card.setCardType("Environment");
                this.cards.add(new Environment(card));
            }
        }
        this.nrOfCards = cards.size();
    }

    /**
     * get first card from deck
     * @return first card from deck
     */
    public CardInput getFirstCard() {
        if (isEmpty()) {
            return null;
        }
        CardInput card = cards.get(0);
        cards.remove(0);
        nrOfCards--;
        return card;
    }

    /**
     * shuffle cards from deck
     * @param shuffleSeed seed from input game
     */
    public void shuffleCards(final int shuffleSeed) {
        Random seed = new Random(shuffleSeed);
        Collections.shuffle(cards, seed);
    }

    private boolean isEmpty() {
        return nrOfCards == 0;
    }

}
