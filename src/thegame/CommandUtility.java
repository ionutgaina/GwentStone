package thegame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.CardInput;
import thegame.cards.Environment;
import thegame.cards.Hero;
import thegame.cards.Minion;
import thegame.play.*;

import java.sql.Array;
import java.util.ArrayList;

public final class CommandUtility {
    //! This is an utility class
    private CommandUtility() { }

    public static ObjectNode commandAction(ActionsInput action) {
        Game game = Game.getInstance();
        switch (action.getCommand()) {
            case "useEnvironmentCard":
            {
                Player activePlayer = game.getPlayer(game.getRound().getPlayerActive());
                return useEnvironmentCard(action.getHandIdx(), action.getAffectedRow(), activePlayer);
            }
            case "getCardsOnTable":
                return getCardsOnTable(game.getTable());
            case "placeCard":
                return placeCard(action.getHandIdx(), game.getPlayer(game.getRound().getPlayerActive()));
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
    private static ObjectNode useEnvironmentCard(int handIdx, int affectedRow, Player player) {
        Table table = Game.getInstance().getTable();
        int playerIdx = player.getId();
        Hand hand = player.getPlayingHand();
        CardInput card = hand.getCards().get(handIdx);

        if(card.getCardType().equals("Minion")) {
            return useEnvironmentError("Chosen card is not of type environment.", handIdx, affectedRow);
        }
        Environment environmentCard = new Environment(card);

        if (player.getMana() < environmentCard.getMana()) {
            return useEnvironmentError("Not enough mana to use environment card.", handIdx, affectedRow);
        }

        if(!table.isEnemyRow(affectedRow, playerIdx)){
            return useEnvironmentError("Chosen row does not belong to the enemy.", handIdx, affectedRow);
        }

        if(environmentCard.getName().equals("Heart Hound"))
        {
            ArrayList<Minion> row;
            //! Verify the reflected row (firstRow or backRow)
            if( affectedRow == 0 || affectedRow == 3)
                row = table.getBackRow(playerIdx);
            else
                row = table.getFirstRow(playerIdx);

            if(table.isFull(row)) {
                String message = "Chosen row does not belong to the enemy.";
                return useEnvironmentError(message, handIdx, affectedRow);
            }
        }

        environmentCard.useCardEffect(table, affectedRow);
        player.getPlayingHand().removeCard(handIdx);
        player.decreaseMana(environmentCard.getMana());
        return null;
    }

    private static ObjectNode useEnvironmentError(String message, int handIdx, int affectedRow) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode output = objectMapper.createObjectNode();
        output.put("command", "placeCarduseEnvironmentCard");
        output.put("handIdx", handIdx);
        output.put("affectedRow", affectedRow);
        output.put("error", message);
        return output;
    }

    private static ObjectNode getCardsOnTable(Table table) {
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

    private static ObjectNode getCardsInHand(Player player) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode output = objectMapper.createObjectNode();
        output.put("command", "getCardsInHand");
        output.put("playerIdx", player.getId());
        output.putPOJO("output", new ArrayList<>(player.getPlayingHand().getCards()));

        return output;
    }

    private static ObjectNode placeCard(int handIdx, Player player){
        Table table = Game.getInstance().getTable();
        int playerIdx = player.getId();
        Hand hand = player.getPlayingHand();
        CardInput card = hand.getCards().get(handIdx);

        if(card.getCardType().equals("Environment")) {
            return placeCardError("Cannot place environment card on table.", handIdx);
        }
        Minion minionCard = new Minion(card);

        if (player.getMana() < minionCard.getMana()) {
            return placeCardError("Not enough mana to place card on table.", handIdx);
        }
        //! If succes then card is added, else is return an error
        if(!table.putCardOnTable(minionCard, playerIdx)) {
            return placeCardError("Cannot place card on table since row is full.", handIdx);
        }
        player.decreaseMana(minionCard.getMana());
        hand.removeCard(handIdx);
        return null;
    }

    private static ObjectNode placeCardError(String message, int handIdx) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode output = objectMapper.createObjectNode();
        output.put("command", "placeCard");
        output.put("handIdx", handIdx);
        output.put("error", message);
        return output;
    }

    private static ObjectNode getPlayerMana(Player player) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode output = objectMapper.createObjectNode();
        output.put("command", "getPlayerMana");
        output.put("playerIdx", player.getId());
        output.put("output", player.getMana());

        return output;
    }

    private static ObjectNode getPlayerHero(Player player) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode output = objectMapper.createObjectNode();
        output.put("command", "getPlayerHero");
        output.put("playerIdx", player.getId());
        output.putPOJO("output", new Hero(player.getPlayingHero()));

        return output;
    }

    private static ObjectNode getPlayerTurn(Round round) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode output = objectMapper.createObjectNode();
        output.put("command", "getPlayerTurn");
        output.put("output", round.getPlayerActive());

        return output;
    }

    private static ObjectNode getPlayerDeck(Player player) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode output = objectMapper.createObjectNode();
        output.put("command", "getPlayerDeck");
        output.put("playerIdx", player.getId());
        output.putPOJO("output", new ArrayList<>(player.getPlayingDeck().getCards()));

        return output;
    }
}
