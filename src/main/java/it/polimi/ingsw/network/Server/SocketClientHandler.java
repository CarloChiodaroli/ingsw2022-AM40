package it.polimi.ingsw.network.Server;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.Message.Message;
import it.polimi.ingsw.network.Message.MessageType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Socket implementation of the {@link ClientHandler} interface.
 */
public class SocketClientHandler implements ClientHandler, Runnable {
    private final Socket client;
    private final SocketServer socketServer;

    private boolean connected;

    private final Object inputLock;
    private final Object outputLock;

    //private ObjectOutputStream output;
    //private ObjectInputStream input;

    private PrintWriter output;
    private BufferedReader input;

    private final GsonBuilder gsonBuilder;
    private final Gson gson;


    /**
     * Default constructor.
     *
     * @param socketServer the socket of the server.
     * @param client       the client connecting.
     */
    public SocketClientHandler(SocketServer socketServer, Socket client) {
        this.socketServer = socketServer;
        this.client = client;
        this.connected = true;
        this.gsonBuilder = new GsonBuilder().setPrettyPrinting();
        this.gson = gsonBuilder.create();

        this.inputLock = new Object();
        this.outputLock = new Object();

        try {
            this.output = new PrintWriter(client.getOutputStream(), true); // Output to client
            this.input = new BufferedReader(new InputStreamReader(client.getInputStream())); // Input from client
        } catch (IOException e) {
            Server.LOGGER.severe(e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            handleClientConnection();
        } catch (IOException e) {
            Server.LOGGER.severe("Client " + client.getInetAddress() + " connection dropped.");
            disconnect();
        }
    }

    /**
     * Handles the connection of a new client and keep listening to the socket for new messages.
     *
     * @throws IOException any of the usual Input/Output related exceptions.
     */
    private void handleClientConnection() throws IOException {
        Server.LOGGER.info("Client connected from " + client.getInetAddress());

        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (inputLock) {
                    String line;
                    String rawGson = "";
                    line = input.readLine();
                    while (line != null) {
                        rawGson += line;
                        line = input.readLine();
                    }
                    Message message = gson.fromJson(rawGson, Message.class);

                    String goat = rawGson;

                    Server.LOGGER.info(() -> "Received: " + goat);

                    if (message != null && message.getMessageType() != MessageType.PING) {
                        if (message.getMessageType() == MessageType.LOGIN_REQUEST) {
                            socketServer.addClient(message.getSenderName(), this);
                        } else {
                            Server.LOGGER.info(() -> "Received: " + message);
                            socketServer.onMessageReceived(message);
                        }
                    }
                }
            }
        } catch (ClassCastException e) {
            Server.LOGGER.severe("Invalid stream from client");
        }
        client.close();
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void disconnect() {
        if (connected) {
            try {
                if (!client.isClosed()) {
                    client.close();
                }
            } catch (IOException e) {
                Server.LOGGER.severe(e.getMessage());
            }
            connected = false;
            Thread.currentThread().interrupt();

            socketServer.onDisconnect(this);
        }
    }

    @Override
    public void sendMessage(Message message) {
        synchronized (outputLock) {
            GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
            Gson gson = builder.create();
            String rawGson = gson.toJson(message);
            output.println(rawGson);
            Server.LOGGER.info(() -> "Sent: " + message);
        }
    }

}
