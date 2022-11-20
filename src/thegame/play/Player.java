package thegame.play;

import fileio.CardInput;
import fileio.DecksInput;
import lombok.Data;
import thegame.cards.Environment;
import thegame.cards.Hero;
import thegame.cards.Minion;

import java.util.ArrayList;

@Data
public final class Player {
    private static final int MAX_MANA = 10;
    private static final int HERO_MAX_HEALTH = 30;

    //! Statistics
    private final int id;
    private int nrOfGame = 0;
    private int nrOfWin = 0;

    //! Cards
    private DecksInput inputDecks = new DecksInput();
    private Hand playingHand;
    private Deck playingDeck;
    private Hero playingHero;

    //! Stats
    private int mana;

    public Player(final int playerIdx) {
        id = playerIdx;
    }

    /**
     * Reseting the game for a new game
     *
     * @param deckIdx     deckIdx from inputGame
     * @param shuffleSeed Seed for shuffling the deck
     * @param hero        Hero from server
     */
    public void newGame(final int deckIdx, final int shuffleSeed, final CardInput hero) {
        playingDeck = new Deck(inputDecks.getDecks().get(deckIdx));
        playingDeck.shuffleCards(shuffleSeed);

        mana = 0;

        playingHand = new Hand();
        playingHero = new Hero(hero);
        playingHero.setHealth(HERO_MAX_HEALTH);
        nrOfGame++;
    }

    /**
     * setDeck
     *
     * @param input input from server for setting the deck
     */
    public void setDecks(final DecksInput input) {
        inputDecks.setDecks(new ArrayList<>(input.getDecks()));
        inputDecks.setNrDecks(input.getNrDecks());
        inputDecks.setNrCardsInDeck(input.getNrCardsInDeck());
    }

    /**
     * Method for drawing a card
     */
    public void drawCard() {
        CardInput drawnCard = playingDeck.getFirstCard();
        if (drawnCard == null) {
            return;
        }

        if (drawnCard.getCardType().equals("Environment")) {
            playingHand.addCard(new Environment(drawnCard));
        } else {
            playingHand.addCard(new Minion(drawnCard));
        }
    }

    /**
     * method where we increase mana for the player
     *
     * @param incMana amount of mana to increase
     */
    public void increaseMana(final int incMana) {
        mana += incMana;
    }

    /**
     * Decrease mana method for the player
     *
     * @param decMana amount of mana to decrease
     */
    public void decreaseMana(final int decMana) {
        mana -= decMana;
    }

    /**
     * method if the player won
     */
    public void increaseWinnings() {
        nrOfWin++;
    }
}
