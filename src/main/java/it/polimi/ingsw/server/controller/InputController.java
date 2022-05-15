package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.commons.message.Message;
import it.polimi.ingsw.commons.view.View;

import java.security.InvalidParameterException;

public class InputController {

    public InputController() {
    }

    public static void addPlayer(GameController gameController, String player){
        if (gameController.getPlayerNames().contains(player)) throw new InvalidParameterException("Player name already present");
        if (gameController.getPlayerNames().size() >= 3) throw new IllegalStateException("There are already 3 players");
    }

    public static void removePlayer(GameController gameController, String player){
        if (!gameController.getPlayerNames().contains(player)) throw new InvalidParameterException("Player name not found");
    }

    public static void controlActualPlayer(GameController gameController, String actualPlayer) throws InvalidParameterException {
        if (!gameController.getPlayerNames().contains(actualPlayer))
            throw new InvalidParameterException("Player is not playing the game");
        if (!actualPlayer.equals(gameController.getActualPlayer()))
            throw new InvalidParameterException("Player is not the actual player");
    }

    public static void controlExpertVariant(GameController gameController) throws IllegalStateException {
        if (!gameController.isExpertVariant()) throw new IllegalStateException("Game is not in Expert variant");
    }

    public static void controlGameState(GameController gameController, GameState required) throws IllegalStateException {
        if (!gameController.getState().equals(required))
            throw new IllegalStateException("Actual state is " + gameController.getState() + " when " + required + " is required");
    }

    public static void excludeGameState(GameController gameController, GameState exclude) throws IllegalStateException {
        if (gameController.getState().equals(exclude))
            throw new IllegalStateException("Actual state is " + gameController.getState() + " and it's illegal for this action");
    }

    public static  void playCharacterCardPermit(GameController gameController, String playerName) {
        controlExpertVariant(gameController);
        controlGameState(gameController, GameState.ACTION);
        controlActualPlayer(gameController, playerName);
        if(gameController.isCharacterActive()) throw new IllegalStateException("Character card has been already played");
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

    public static boolean checkLoginNickname(GameController gameController, String nickname, View view) {
        if (nickname.isEmpty() || nickname.equalsIgnoreCase("server")) {
            view.showGenericMessage("Forbidden name.");
            view.showLoginResult(false, true, null);
            return false;
        } else if (gameController.getPlayerNames().contains(nickname)) {
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

    public static boolean checkUser(GameController gameController, Message receivedMessage) {
        return receivedMessage.getSenderName().equals(gameController.getTurnController().getActivePlayer());
    }

}
