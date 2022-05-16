package it.polimi.ingsw.server.network;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.commons.message.ErrorMessage;
import it.polimi.ingsw.commons.message.Message;
import it.polimi.ingsw.commons.message.MessageType;

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

    private PrintWriter output;
    private BufferedReader input;

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
        GsonBuilder gsonBuilder = new GsonBuilder();
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
                    Message message = null;
                    try {
                        String rawGson;
                        do {
                            rawGson = input.readLine();
                        } while (rawGson == null);
                        message = gson.fromJson(rawGson, Message.class);
                        if(message != null) {
                            message = (Message) gson.fromJson(rawGson, message.getMessageType().getImplementingClass());
                            String forLambda = rawGson;
                            if (!message.getMessageType().equals(MessageType.PING))
                                Server.LOGGER.info(() -> "Received: " + forLambda);
                        }
                    } catch (JsonSyntaxException e) {
                        message = new ErrorMessage(null, "Message got wrongly");
                        sendMessage(message);
                    } catch (IOException e) {
                        Server.LOGGER.info(() -> "Error in input stream " + e.getMessage());
                        disconnect();
                    }
                    if (message != null && message.getMessageType() != MessageType.PING) {
                        if (message.getMessageType() == MessageType.LOGIN) {
                            socketServer.addClient(message.getSenderName(), this);
                        } else {
                            Message forLambda = message;
                            Server.LOGGER.info(() -> "Received: " + forLambda);
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
            String rawGson = gson.toJson(message);
            output.println(rawGson);
            if (!message.getMessageType().equals(MessageType.PING)) Server.LOGGER.info(() -> "Sent: " + rawGson);
        }
    }

}
