package it.polimi.ingsw.network.Server;

import it.polimi.ingsw.network.Client.ClientHandler;
import it.polimi.ingsw.network.Message.Message;

import java.net.ServerSocket;


/**
 *
 */

/**
 * It handles all the new socket connection.
 * It is a socket and there is installed the server
 */

public class SocketServer {
    //private final Server server;
    private final int port;
    ServerSocket serverSocket;

    public SocketServer(/*Server server,*/ int port) {
        //this.server = server;
        this.port = port;
    }

    public void addClient(String nickname, ClientHandler clientHandler)
    {
    //server aggiunge client
    }

    public void onMessageReceived(Message message)
    {
        //server riceve messaggio
    }

    public void onDisconnect(ClientHandler clientHandler) {
        //server si disconnette
    }
}
