package it.polimi.ingsw.server.network;

import it.polimi.ingsw.commons.message.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Socket server that handles all the new socket connection.
 */
public class SocketServer implements Runnable {
    private final Server server;
    private final int port;
    ServerSocket serverSocket;

    /**
     * Constructor
     */
    public SocketServer(Server server, int port) {
        this.server = server;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            Server.LOGGER.info(() -> "Socket server started on port " + port + ".");
        } catch (IOException e) {
            Server.LOGGER.severe("Server could not start!");
            return;
        }

        while (!Thread.currentThread().isInterrupted()) {
            try {

                Socket client = serverSocket.accept();

                client.setSoTimeout(5000); // Commented for testing with Netcat

                SocketClientHandler clientHandler = new SocketClientHandler(this, client);
                Thread thread = new Thread(clientHandler, "ss_handler" + client.getInetAddress());
                thread.start();
            } catch (IOException e) {
                Server.LOGGER.severe("Connection dropped");
            }
        }
    }

    /**
     * Handles the add of a new client
     *
     * @param nickname nickname of the client
     * @param clientHandler ClientHandler of the client
     */
    public void addClient(String nickname, ClientHandler clientHandler) {
        server.addClient(nickname, clientHandler);
    }

    /**
     * Forwards a received message from client to Server
     *
     * @param message message
     */
    public void onMessageReceived(Message message) {
        server.onMessageReceived(message);
    }

    /**
     * Handles a client disconnection
     *
     * @param clientHandler ClientHandler of disconnect client
     */
    public void onDisconnect(ClientHandler clientHandler) {
        server.onDisconnect(clientHandler);
    }
}
