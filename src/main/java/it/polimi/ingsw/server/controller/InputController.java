package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.manuel.model.Game;
import it.polimi.ingsw.commons.message.Message;
import it.polimi.ingsw.commons.message.PlayerNumberReply;
import it.polimi.ingsw.commons.view.View;
import it.polimi.ingsw.server.view.VirtualView;

import java.util.Map;

public class InputController {

    private final Game game;
    private transient Map<String, VirtualView> virtualViewMap;
    private final GameManager gameManager;

    public InputController(Map<String, VirtualView> virtualViewMap, GameManager gameManager) {
        this.game = Game.getInstance();
        this.virtualViewMap = virtualViewMap;
        this.gameManager = gameManager;
    }

    public boolean verifyReceivedData(Message message) {
        switch (message.getMessageType()) {
            case GENERIC: // server doesn't receive a GENERIC_MESSAGE.
                return false;
            case LOGIN_REPLY: // server doesn't receive a LOGIN_REPLY.
                return false;
            case PLAYER_NUMBER_REPLY:
                return playerNumberReplyCheck(message);
            case PLAYER_NUMBER_REQUEST: // server doesn't receive a GenericErrorMessage.
                return false;
            default: // Never should reach this statement.
                return false;
        }
    }

    public boolean checkLoginNickname(String nickname, View view) {
        if (nickname.isEmpty() || nickname.equalsIgnoreCase(Game.SERVER_NICKNAME)) {
            view.showGenericMessage("Forbidden name.");
            view.showLoginResult(false, true, null);
            return false;
        } else if (game.isNicknameTaken(nickname)) {
            view.showGenericMessage("Nickname already taken.");
            view.showLoginResult(false, true, null);
            return false;
        }
        return true;
    }

    private boolean playerNumberReplyCheck(Message message) {
        PlayerNumberReply playerNumberReply = (PlayerNumberReply) message;
        if (playerNumberReply.getPlayerNumber() < 4 && playerNumberReply.getPlayerNumber() > 1) {
            return true;
        } else {
            VirtualView virtualView = virtualViewMap.get(message.getSenderName());
            virtualView.askPlayersNumber();
            return false;
        }
    }

    public boolean checkUser(Message receivedMessage) {
        return receivedMessage.getSenderName().equals(gameManager.getTurnController().getActivePlayer());
    }


}
