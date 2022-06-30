package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.outer.GameManager;
import it.polimi.ingsw.server.network.Server;
import it.polimi.ingsw.server.network.SocketServer;

import java.util.Arrays;
import java.util.List;

/**
 * Server loader class, Eriantys class calls this class to launch the server.
 */
public class ServerApp {

    private final static String port = "--port";

    /**
     * Server loader method, called when
     */
    public static void server() {
        int serverPort = 16847; // default value
        GameManager gameManager = new GameManager();
        Server server = new Server(gameManager);
        SocketServer socketServer = new SocketServer(server, serverPort);
        Thread thread = new Thread(socketServer, "socketserver_");
        thread.start();
    }

    public static void server(String[] args) {
        int serverPort = 16847; // default value
        if (args.length > 1) {
            List<String> argos = Arrays.stream(args).toList();
            if (argos.contains(port)) {
                String userPort = argos.get(argos.indexOf(port) + 1);
                try {
                    serverPort = Integer.parseInt(userPort);
                } catch (NumberFormatException e) {
                    System.out.println("Error while reading port number");
                    System.exit(1);
                }
            }
        }
        GameManager gameManager = new GameManager();
        Server server = new Server(gameManager);
        SocketServer socketServer = new SocketServer(server, serverPort);
        Thread thread = new Thread(socketServer, "socketserver_");
        thread.start();
    }
}
