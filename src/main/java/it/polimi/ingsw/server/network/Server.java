package it.polimi.ingsw.server.network;


import it.polimi.ingsw.server.controller.outer.GameManager;
import it.polimi.ingsw.commons.message.Message;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Main server class that starts a socket server.
 * It can handle different types of connections.
 */
public class Server {

    private final GameManager gameManager;
    private final Map<String, ClientHandler> clientHandlerMap;
    public static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private final Object lock;

    public Server(GameManager gameManager) {
        this.gameManager = gameManager;
        this.clientHandlerMap = Collections.synchronizedMap(new HashMap<>());
        this.lock = new Object();
    }

    public void addClient(String nickname, ClientHandler clientHandler) {
        VirtualView vv = new VirtualView(clientHandler, "server");

        if(gameManager.checkLoginNickname(nickname, vv)){
            clientHandlerMap.put(nickname, clientHandler);
            gameManager.loginHandler(nickname, vv);
        }
    }

    public void removeClient(String nickname, boolean notifyEnabled) {
        clientHandlerMap.remove(nickname);
        if(
        gameManager.removeVirtualView(nickname, notifyEnabled)){
            exit();
        }
        LOGGER.info(() -> "Removed " + nickname + " from the client list.");
    }
    //SERVER'S MESSAGE RECEIVED
    public void onMessageReceived(Message message) {
        gameManager.onMessageReceived(message);
    }

    /**
     * Disconnect Client
     * @param clientHandler the one to disconnect
     */
    public void onDisconnect(ClientHandler clientHandler) {
        synchronized (lock) {
            String nickname = getNicknameFromClientHandler(clientHandler);

            if (nickname != null) {

                boolean gameStarted = gameManager.isGameStarted();
                removeClient(nickname, !gameStarted); // enable lobby notifications only if the game didn't start yet.

                // Resets server status only if the game was already started.
                // Otherwise the server will wait for a new player to connect.
                //if (gameStarted) {
                    //gameManager.broadcastDisconnectionMessage(nickname, " disconnected from the server. GAME ENDED.");
                    //gameManager.endGame();
                    //clientHandlerMap.clear();
                //}
            }
        }
    }

    private String getNicknameFromClientHandler(ClientHandler clientHandler) {
        return clientHandlerMap.entrySet()
                .stream()
                .filter(entry -> clientHandler.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    public void exit(){
        System.exit(0);
    }
}
