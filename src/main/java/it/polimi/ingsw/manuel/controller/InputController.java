package it.polimi.ingsw.manuel.controller;

import it.polimi.ingsw.manuel.model.Game;
import it.polimi.ingsw.manuel.network.message.Message;
import it.polimi.ingsw.manuel.network.message.PlayerNumberReply;
import it.polimi.ingsw.manuel.view.View;
import it.polimi.ingsw.manuel.view.VirtualView;

import java.util.Map;

public class InputController {

    private final Game game;
    private transient Map<String, VirtualView> virtualViewMap;
    private final GameController gameController;

    public InputController(Map<String, VirtualView> virtualViewMap, GameController gameController) {
        this.game = Game.getInstance();
        this.virtualViewMap = virtualViewMap;
        this.gameController = gameController;
    }

    public boolean verifyReceivedData(Message message) {
        switch (message.getMessageType()) {
            case GENERIC_MESSAGE: // server doesn't receive a GENERIC_MESSAGE.
                return false;
            case LOGIN_REPLY: // server doesn't receive a LOGIN_REPLY.
                return false;
            case PLAYERNUMBER_REPLY:
                return playerNumberReplyCheck(message);
            case PLAYERNUMBER_REQUEST: // server doesn't receive a GenericErrorMessage.
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
            VirtualView virtualView = virtualViewMap.get(message.getNickname());
            virtualView.askPlayersNumber();
            return false;
        }
    }

    public boolean checkUser(Message receivedMessage) {
        return receivedMessage.getNickname().equals(gameController.getTurnController().getActivePlayer());
    }


}
