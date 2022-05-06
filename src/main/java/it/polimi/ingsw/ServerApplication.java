package it.polimi.ingsw;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.Server.Server;
import it.polimi.ingsw.network.Server.SocketServer;

/**
 * Server Application
 */
public class ServerApplication {

    public static void main(String[] args) {
        int serverPort = 16847;

        GameController gameController = new GameController();

        Server server = new Server(gameController);
        SocketServer socketServer = new SocketServer(server, serverPort);
        Thread thread = new Thread(socketServer,"Main Thread");
        thread.start();
    }
}