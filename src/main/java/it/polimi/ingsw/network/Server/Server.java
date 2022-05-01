package it.polimi.ingsw.network.Server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.GameModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Main server class (Application Logic)
 */
public class Server {

    private final GameController gameController;
    private final Map<String,ClientHandler> clientHandlerMap;

    private final Object lock;

    public Server(GameController gameController)
    {
        this.gameController=gameController;
        this.clientHandlerMap= Collections.synchronizedMap(new HashMap<>());
        this.lock=new Object();
    }

    public void addClient(String playerName, ClientHandler clientHandler)
    {
        if(!gameController.isStartGame())
            if(!gameController.checkLoginUser(playerName))
            {
                clientHandlerMap.put(playerName,clientHandler);
                gameController.addPlayer(playerName);
            }
            else
            {
                clientHandler.disconnect();
            }
    }

    public void removeClient(String playerName)
    {
        clientHandlerMap.remove(playerName);
        // gamecontroller.remove (To do method) <---------------------------------------
    }

    public void onMessageReceived()
    {
        gameController.onMessageReceived();
    }

    public void onDisconnect(ClientHandler clientHandler)
    {
        synchronized (lock)
        {
            String playerName = getplayerNameFromClientHandler(clientHandler);
            if(playerName!=null)
            {
                removeClient(playerName);
            }
            if(gameController.isStartGame())
            {
                //BroadCast Message to all Client
                clientHandlerMap.clear();
            }
        }
    }

    private String getplayerNameFromClientHandler(ClientHandler clientHandler) {
        return clientHandlerMap.entrySet()
                .stream()
                .filter(entry -> clientHandler.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

}
