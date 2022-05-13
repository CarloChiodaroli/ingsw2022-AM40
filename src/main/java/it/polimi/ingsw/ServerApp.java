package it.polimi.ingsw;

import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.network.Server;
import it.polimi.ingsw.server.network.SocketServer;

public class ServerApp {

    public static void main(String[] args) {
        int serverPort = 16847; // default value
        GameManager gameManager = new GameManager();
        Server server = new Server(gameManager);
        SocketServer socketServer = new SocketServer(server, serverPort);
        Thread thread = new Thread(socketServer, "socketserver_");
        thread.start();
    }
}
