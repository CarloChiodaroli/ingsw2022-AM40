package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.commons.message.Message;
import it.polimi.ingsw.commons.view.View;

import java.security.InvalidParameterException;

public class InputController {

    private final PlayMessagesReader reader;

    public InputController(PlayMessagesReader reader) {
        this.reader = reader;
    }

    public void addPlayer(String player) {
        if (reader.getPlayerNames().contains(player))
            throw new InvalidParameterException("Player name already present");
        if (reader.getPlayerNames().size() >= 3) throw new IllegalStateException("There are already 3 players");
    }

    public void removePlayer(String player) {
        if (!reader.getPlayerNames().contains(player)) throw new InvalidParameterException("Player name not found");
    }

    public void controlActualPlayer(String actualPlayer) throws InvalidParameterException {
        if (!reader.getPlayerNames().contains(actualPlayer))
            throw new InvalidParameterException("Player is not playing the game");
        if (!actualPlayer.equals(reader.getActualPlayer()))
            throw new InvalidParameterException("Player is not the actual player");
    }

    public void controlExpertVariant() throws IllegalStateException {
        if (!reader.isExpertVariant()) throw new IllegalStateException("Game is not in Expert variant");
    }

    public void controlGameState(GameState required) throws IllegalStateException {
        if (!reader.getState().equals(required))
            throw new IllegalStateException("Actual state is " + reader.getState() + " when " + required + " is required");
    }

    public void excludeGameState(GameState exclude) throws IllegalStateException {
        if (reader.getState().equals(exclude))
            throw new IllegalStateException("Actual state is " + reader.getState() + " and it's illegal for this action");
    }

    public void playCharacterCardPermit(String playerName) {
        controlExpertVariant();
        controlGameState(GameState.ACTION);
        controlActualPlayer(playerName);
        if (reader.isCharacterActive()) throw new IllegalStateException("Character card has been already played");
    }

    /* @Deprecated
    public boolean verifyReceivedData(Message message) {
        switch (message.getMessageType()) {
            case GENERIC: // server doesn't receive a GENERIC_MESSAGE.
                return false;
            /*case LOGIN_REPLY: // server doesn't receive a LOGIN_REPLY.
                return false;
            /*case PLAYER_NUMBER_REPLY:
                return playerNumberReplyCheck(message);
            /*case PLAYER_NUMBER_REQUEST: // server doesn't receive a GenericErrorMessage.
                return false;
            default: // Never should reach this statement.
                return false;
        }
    }*/

    public boolean checkLoginNickname(String nickname, View view) {
        if (nickname.isEmpty() || nickname.equalsIgnoreCase("server")) {
            view.showGenericMessage("Forbidden name.");
            view.showLoginResult(false, true, null);
            return false;
        } else if (reader.getPlayerNames().contains(nickname)) {
            view.showGenericMessage("Nickname already taken.");
            view.showLoginResult(false, true, null);
            return false;
        }
        return true;
    }

    /* @Deprecated
    private boolean playerNumberReplyCheck(Message message) {
        PlayerNumberReply playerNumberReply = (PlayerNumberReply) message;
        if (playerNumberReply.getPlayerNumber() < 4 && playerNumberReply.getPlayerNumber() > 1) {
            return true;
        } else {
            VirtualView virtualView = virtualViewMap.get(message.getSenderName());
            virtualView.askPlayersNumber();
            return false;
        }
    }*/

    public boolean checkUser(Message receivedMessage) {
        return receivedMessage.getSenderName().equals(reader.getTurnController().getActivePlayer());
    }

}
