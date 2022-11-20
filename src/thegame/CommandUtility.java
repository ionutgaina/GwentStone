package thegame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.CardInput;
import fileio.Coordinates;
import thegame.cards.Environment;
import thegame.cards.Hero;
import thegame.cards.Minion;
import thegame.play.Game;
import thegame.play.Round;
import thegame.play.Hand;
import thegame.play.Player;
import thegame.play.Table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CommandUtility {
    private static final int BACKROW = 3;

    //! This is an utility class
    private CommandUtility() {
    }

    /**
     * Utility method for commands
     *
     * @param action data for command action
     * @return node with data for output
     */
    public static ObjectNode commandAction(final ActionsInput action) {
        Game game = Game.getInstance();
        switch (action.getCommand()) {
            case "getPlayerTwoWins":
                return getPlayerWins(2);
            case "getPlayerOneWins":
                return getPlayerWins(1);
            case "getTotalGamesPlayed":
                return getTotalGamesPlayed();
            case "useHeroAbility":
                return useHeroAbility(action.getAffectedRow());
            case "useAttackHero":
                return useAttackHero(action.getCardAttacker());
            case "cardUsesAbility":
                return cardUsesAbility(action.getCardAttacker(), action.getCardAttacked(),
                        game.getTable());
            case "cardUsesAttack":
                return cardUsesAttack(action.getCardAttacker(), action.getCardAttacked(),
                        game.getTable());
            case "getFrozenCardsOnTable":
                return getFrozenCardsOnTable(game.getTable());
            case "getEnvironmentCardsInHand":
                return getEnvironmentCardsInHand(game.getPlayer(action.getPlayerIdx()));
            case "getCardAtPosition":
                return getCardAtPosition(action.getX(), action.getY(), game.getTable());
            case "useEnvironmentCard":
                return useEnvironmentCard(action.getHandIdx(), action.getAffectedRow(),
                        game.getPlayer(game.getRound().getPlayerActive()));
            case "getCardsOnTable":
                return getCardsOnTable(game.getTable());
            case "placeCard":
                return placeCard(action.getHandIdx(),
                        game.getPlayer(game.getRound().getPlayerActive()));
            case "endPlayerTurn":
                game.nextTurn();
                return null;
            case "getCardsInHand":
                return getCardsInHand(game.getPlayer(action.getPlayerIdx()));
            case "getPlayerMana":
                return getPlayerMana(game.getPlayer(action.getPlayerIdx()));
            case "getPlayerDeck":
                return getPlayerDeck(game.getPlayer(action.getPlayerIdx()));
            case "getPlayerTurn":
                return getPlayerTurn(game.getRound());
            case "getPlayerHero":
                return getPlayerHero(game.getPlayer(action.getPlayerIdx()));
            default:
                return null;
        }
    }

    private static ObjectNode getPlayerWins(final int playerIdx) {
        Game game = Game.getInstance();
        int totalWin = game.getPlayer(playerIdx).getNrOfWin();
        String command = playerIdx == 1 ? "getPlayerOneWins" : "getPlayerTwoWins";
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode output = objectMapper.createObjectNode();
        output.put("command", command);
        output.put("output", totalWin);
        return output;
    }

    private static ObjectNode getTotalGamesPlayed() {
        Game game = Game.getInstance();
        int totalGamesPlayed = game.getPlayerOne().getNrOfGame();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode output = objectMapper.createObjectNode();
        output.put("command", "getTotalGamesPlayed");
        output.put("output", totalGamesPlayed);
        return output;
    }

    private static ObjectNode useHeroAbility(final int affectected) {
        Game game = Game.getInstance();
        Table table = game.getTable();
        int activePlayerIdx = game.getRound().getPlayerActive();
        Player currentPlayer = game.getPlayer(activePlayerIdx);
        Hero playingHero = currentPlayer.getPlayingHero();

        if (currentPlayer.getMana() < playingHero.getMana()) {
            return userHeroAbilityError(affectected, "Not enough mana to use hero's ability.");
        }
        if (playingHero.isFought()) {
            return userHeroAbilityError(affectected, "Hero has already attacked this turn.");
        }
        String heroName = playingHero.getName();
        if (heroName.equals("Lord Royce") || heroName.equals("Empress Thorina")) {
            //! if isn't enemy then is friendly row
            if (!table.isEnemyRow(affectected, activePlayerIdx)) {
                String message = "Selected row does not belong to the enemy.";
                return userHeroAbilityError(affectected, message);
            }
        }
        if (heroName.equals("General Kocioraw") || heroName.equals("King Mudface")) {
            //! if is enemy then isn't friendly row
            if (table.isEnemyRow(affectected, activePlayerIdx)) {
                String message = "Selected row does not belong to the current player.";
                return userHeroAbilityError(affectected, message);
            }
        }

        currentPlayer.decreaseMana(playingHero.getMana());
        playingHero.useAbilityHero(affectected, table);
        return null;
    }

    private static ObjectNode userHeroAbilityError(final int affectedRow, final String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode output = objectMapper.createObjectNode();
        output.put("command", "useHeroAbility");
        output.putPOJO("affectedRow", affectedRow);
        output.put("error", message);
        return output;
    }

    private static ObjectNode useAttackHero(final Coordinates attacker) {
        Game game = Game.getInstance();
        Table table = game.getTable();
        Minion attackerMinion = table.getCard(attacker.getX(), attacker.getY());

        if (attackerMinion.isFrozen()) {
            return useAttackHeroError(attacker, "Attacker card is frozen.");
        }

        if (attackerMinion.isFought()) {
            return useAttackHeroError(attacker, "Attacker card has already attacked this turn.");
        }

        int activePlayer = game.getRound().getPlayerActive();
        if (table.enemyIsHavingTank(activePlayer)) {
            return useAttackHeroError(attacker, "Attacked card is not of type 'Tank'.");
        }

        Hero attackedHero = game.getEnemyPlayer(activePlayer).getPlayingHero();
        attackerMinion.attackHero(attackedHero);
        if (attackedHero.isDead()) {
            game.winGame(activePlayer);
            return gameEnded(activePlayer);
        }

        return null;
    }

    private static ObjectNode gameEnded(final int activePlayer) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode output = objectMapper.createObjectNode();
        output.put("gameEnded", (activePlayer == 1 ? "Player one" : "Player two") + " killed the "
                + "enemy hero.");
        return output;
    }

    private static ObjectNode useAttackHeroError(final Coordinates attacker, final String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode output = objectMapper.createObjectNode();
        output.put("command", "useAttackHero");
        output.putPOJO("cardAttacker", attacker);
        output.put("error", message);
        return output;
    }

    private static ObjectNode cardUsesAbility(final Coordinates attacker,
                                              final Coordinates attacked, final Table table) {
        Game game = Game.getInstance();
        Minion attackerMinion = table.getCard(attacker.getX(), attacker.getY());
        Minion attackedMinion = table.getCard(attacked.getX(), attacked.getY());

        if (attacker.equals(attacked)) {
            return null;
        }

        String command = "cardUsesAbility";
        assert attackerMinion != null;
        ObjectNode output = cardAttackerIsOk(attacker, attacked, attackerMinion, command);
        if (output != null) {
            return output;
        }

        int activePlayer = game.getRound().getPlayerActive();

        if (attackerMinion.getName().equals("Disciple") && table.isEnemyRow(attacked.getX(),
                activePlayer)) {
            //! If card is on enemy row then isn't a friendly card
            String message = "Attacked card does not belong to the current player.";
            return cardAttackError(attacker, attacked, message, command);
        }

        //! Verify if the card is special with ability to target enemey minion
        String[] specialMinions = {"The Ripper", "Miraj", "The Cursed One"};
        List<String> minionsList = Arrays.asList(specialMinions);
        if (minionsList.contains(attackerMinion.getName())) {
            output = cardAttackedIsOk(attacker, attacked, attackedMinion, command);
            if (output != null) {
                return output;
            }
        }

        attackerMinion.useAbilityCard(attacked);
        return null;
    }

    private static ObjectNode cardUsesAttack(final Coordinates attacker,
                                             final Coordinates attacked, final Table table) {
        Minion attackerMinion = table.getCard(attacker.getX(), attacker.getY());
        Minion attackedMinion = table.getCard(attacked.getX(), attacked.getY());

        String command = "cardUsesAttack";
        assert attackerMinion != null;
        ObjectNode output = cardAttackerIsOk(attacker, attacked, attackerMinion, command);
        if (output != null) {
            return output;
        }

        output = cardAttackedIsOk(attacker, attacked, attackedMinion, command);
        if (output != null) {
            return output;
        }

        attackerMinion.attackCard(attacked);
        return null;
    }

    private static ObjectNode cardAttackerIsOk(final Coordinates attacker,
                                               final Coordinates attacked,
                                               final Minion attackerMinion, final String command) {
        if (attackerMinion.isFrozen()) {
            return cardAttackError(attacker, attacked, "Attacker card is frozen.", command);
        }

        if (attackerMinion.isFought()) {
            return cardAttackError(attacker, attacked,
                    "Attacker card has already attacked this " + "turn.", command);
        }
        return null;
    }

    private static ObjectNode cardAttackedIsOk(final Coordinates attacker,
                                               final Coordinates attacked,
                                               final Minion attackedMinion, final String command) {
        Game game = Game.getInstance();
        Table table = game.getTable();
        int activePlayer = game.getRound().getPlayerActive();

        if (!table.isEnemyRow(attacked.getX(), activePlayer)) {
            return cardAttackError(attacker, attacked, "Attacked card does not belong to the "
                    + "enemy.", command);
        }

        if (table.enemyIsHavingTank(activePlayer) && !attackedMinion.isTank()) {
            return cardAttackError(attacker, attacked, "Attacked card is not of type 'Tank'.",
                    command);
        }
        return null;
    }

    private static ObjectNode cardAttackError(final Coordinates attacker,
                                              final Coordinates attacked, final String message,
                                              final String command) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode output = objectMapper.createObjectNode();
        output.put("command", command);
        output.putPOJO("cardAttacker", attacker);
        output.putPOJO("cardAttacked", attacked);
        output.put("error", message);
        return output;
    }

    private static ObjectNode getFrozenCardsOnTable(final Table table) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode output = objectMapper.createObjectNode();
        output.put("command", "getFrozenCardsOnTable");
        output.putPOJO("output", new ArrayList<>(table.getFrozenMinionTable()));
        return output;
    }

    private static ObjectNode getEnvironmentCardsInHand(final Player player) {
        ArrayList<Environment> environmentCards = player.getPlayingHand().getEnvironmentCards();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode output = objectMapper.createObjectNode();
        output.put("command", "getEnvironmentCardsInHand");
        output.put("playerIdx", player.getId());
        output.putPOJO("output", new ArrayList<>(environmentCards));
        return output;
    }

    private static ObjectNode getCardAtPosition(final int x, final int y, final Table table) {
        Minion minionCard = table.getCard(x, y);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode output = objectMapper.createObjectNode();
        output.put("command", "getCardAtPosition");
        if (minionCard == null) {
            output.put("output", "No card available at that position.");
        } else {
            output.putPOJO("output", new Minion(minionCard));
        }
        output.put("x", x);
        output.put("y", y);
        return output;
    }

    private static ObjectNode useEnvironmentCard(final int handIdx, final int affectedRow,
                                                 final Player player) {
        Table table = Game.getInstance().getTable();
        int playerIdx = player.getId();
        Hand hand = player.getPlayingHand();
        CardInput card = hand.getCards().get(handIdx);

        if (card.getCardType().equals("Minion")) {
            return useEnvironmentError("Chosen card is not of type environment.", handIdx,
                    affectedRow);
        }
        Environment environmentCard = new Environment(card);

        if (player.getMana() < environmentCard.getMana()) {
            return useEnvironmentError("Not enough mana to use environment card.", handIdx,
                    affectedRow);
        }

        if (!table.isEnemyRow(affectedRow, playerIdx)) {
            return useEnvironmentError("Chosen row does not belong to the enemy.", handIdx,
                    affectedRow);
        }

        if (environmentCard.getName().equals("Heart Hound")) {
            ArrayList<Minion> row;
            //! Verify the reflected row (firstRow or backRow)
            if (affectedRow == 0 || affectedRow == BACKROW) {
                row = table.getBackRow(playerIdx);
            } else {
                row = table.getFirstRow(playerIdx);
            }

            if (table.isFull(row)) {
                String message = "Cannot steal enemy card since the player's row is full.";
                return useEnvironmentError(message, handIdx, affectedRow);
            }
        }

        environmentCard.useCardEffect(table, affectedRow);
        player.getPlayingHand().removeCard(handIdx);
        player.decreaseMana(environmentCard.getMana());
        return null;
    }

    private static ObjectNode useEnvironmentError(final String message, final int handIdx,
                                                  final int affectedRow) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode output = objectMapper.createObjectNode();
        output.put("command", "useEnvironmentCard");
        output.put("handIdx", handIdx);
        output.put("affectedRow", affectedRow);
        output.put("error", message);
        return output;
    }

    private static ObjectNode getCardsOnTable(final Table table) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode output = objectMapper.createObjectNode();
        output.put("command", "getCardsOnTable");
        ArrayList<ArrayList> matrix = new ArrayList<>();
        matrix.add(new ArrayList<>(table.getFirstRow()));
        matrix.add(new ArrayList<>(table.getSecondRow()));
        matrix.add(new ArrayList<>(table.getThirdRow()));
        matrix.add(new ArrayList<>(table.getFourthRow()));
        output.putPOJO("output", matrix);
        return output;
    }

    private static ObjectNode getCardsInHand(final Player player) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode output = objectMapper.createObjectNode();
        output.put("command", "getCardsInHand");
        output.put("playerIdx", player.getId());
        output.putPOJO("output", new ArrayList<>(player.getPlayingHand().getCards()));

        return output;
    }

    private static ObjectNode placeCard(final int handIdx, final Player player) {
        Table table = Game.getInstance().getTable();
        int playerIdx = player.getId();
        Hand hand = player.getPlayingHand();
        CardInput card = hand.getCards().get(handIdx);

        if (card.getCardType().equals("Environment")) {
            return placeCardError("Cannot place environment card on table.", handIdx);
        }
        Minion minionCard = new Minion(card);

        if (player.getMana() < minionCard.getMana()) {
            return placeCardError("Not enough mana to place card on table.", handIdx);
        }
        //! If succes then card is added, else is return an error
        if (!table.putCardOnTable(minionCard, playerIdx)) {
            return placeCardError("Cannot place card on table since row is full.", handIdx);
        }
        player.decreaseMana(minionCard.getMana());
        hand.removeCard(handIdx);
        return null;
    }

    private static ObjectNode placeCardError(final String message, final int handIdx) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode output = objectMapper.createObjectNode();
        output.put("command", "placeCard");
        output.put("handIdx", handIdx);
        output.put("error", message);
        return output;
    }

    private static ObjectNode getPlayerMana(final Player player) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode output = objectMapper.createObjectNode();
        output.put("command", "getPlayerMana");
        output.put("playerIdx", player.getId());
        output.put("output", player.getMana());

        return output;
    }

    private static ObjectNode getPlayerHero(final Player player) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode output = objectMapper.createObjectNode();
        output.put("command", "getPlayerHero");
        output.put("playerIdx", player.getId());
        output.putPOJO("output", new Hero(player.getPlayingHero()));

        return output;
    }

    private static ObjectNode getPlayerTurn(final Round round) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode output = objectMapper.createObjectNode();
        output.put("command", "getPlayerTurn");
        output.put("output", round.getPlayerActive());

        return output;
    }

    private static ObjectNode getPlayerDeck(final Player player) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode output = objectMapper.createObjectNode();
        output.put("command", "getPlayerDeck");
        output.put("playerIdx", player.getId());
        output.putPOJO("output", new ArrayList<>(player.getPlayingDeck().getCards()));

        return output;
    }
}
