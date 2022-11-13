package thegame.play;

import fileio.CardInput;
import fileio.DecksInput;
import lombok.Data;
import thegame.cards.Environment;
import thegame.cards.Hero;
import thegame.cards.Minion;

import java.util.ArrayList;

@Data
public class Player {
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

    public Player(int playerIdx) {
        id = playerIdx;
    }

    public int getId() {
        return id;
    }

    public Deck getPlayingDeck() {
        return playingDeck;
    }

    public Hero getPlayingHero() { return playingHero; }

    public Hand getPlayingHand() { return playingHand; }

    public int getMana() { return mana; }

    public void newGame(int deckIdx, int shuffleSeed, CardInput hero) {
        playingDeck = new Deck(inputDecks.getDecks().get(deckIdx));
        playingDeck.shuffleCards(shuffleSeed);

        mana = 0;

        playingHand = new Hand();
        playingHero = new Hero(hero);
        playingHero.setHealth(HERO_MAX_HEALTH);
        nrOfGame++;
    }

    public void setDecks(DecksInput input) {
        inputDecks.setDecks(new ArrayList<>(input.getDecks()));
        inputDecks.setNrDecks(input.getNrDecks());
        inputDecks.setNrCardsInDeck(input.getNrCardsInDeck());
    }

    public void drawCard() {
        CardInput drawnCard = playingDeck.getFirstCard();
        if(drawnCard == null)
            return;

        if (drawnCard.getCardType().equals("Environment"))
            playingHand.addCard(new Environment(drawnCard));
        else
            playingHand.addCard(new Minion(drawnCard));
    }

    public void increaseMana(int incMana) {
        mana += incMana;
    }

    public void decreaseMana(int decMana) {
        mana -= decMana;
    }

    public void increaseWinnings() { nrOfWin++; }
}
