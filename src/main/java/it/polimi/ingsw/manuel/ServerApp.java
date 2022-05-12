package it.polimi.ingsw.manuel;

import it.polimi.ingsw.manuel.controller.GameController;
import it.polimi.ingsw.manuel.network.server.Server;
import it.polimi.ingsw.manuel.network.server.SocketServer;

public class ServerApp {

    public static void main(String[] args) {
        int serverPort = 16847; // default value
        GameController gameController = new GameController();
        Server server = new Server(gameController);
        SocketServer socketServer = new SocketServer(server, serverPort);
        Thread thread = new Thread(socketServer, "socketserver_");
        thread.start();
    }
}
