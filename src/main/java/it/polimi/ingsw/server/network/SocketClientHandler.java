package it.polimi.ingsw.server.network;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.client.network.Client;
import it.polimi.ingsw.commons.message.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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
    private final Map<MessageType, Class<?>> instance;


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

        this.instance = instanceSetter();
    }

    private Map<MessageType, Class<?>> instanceSetter(){
        Map<MessageType, Class<?>> map = new HashMap<>();
        map.put(MessageType.LOGIN_REQUEST, LoginRequest.class);
        //map.put(MessageType.LOGIN_REPLY, LoginReply.class);
        map.put(MessageType.DISCONNECTION, DisconnectionMessage.class);
        map.put(MessageType.PLAYER_NUMBER_REQUEST, PlayerNumberRequest.class);
        map.put(MessageType.PLAYER_NUMBER_REPLY, PlayerNumberReply.class);
        // map.put(MessageType.PICK_FIRST_PLAYER, no class);

        map.put(MessageType.LOGIN, LoginMessage.class);
        map.put(MessageType.LOBBY, LobbyMessage.class);
        map.put(MessageType.PLAY, PlayMessage.class);
        map.put(MessageType.PING, PingMessage.class);
        map.put(MessageType.ERROR, ErrorMessage.class);
        map.put(MessageType.GENERIC, GenericMessage.class);
        return map;
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
                    Message message;
                    try{
                        String rawGson;
                        do {
                            rawGson = input.readLine();
                        } while(rawGson == null);
                        message = gson.fromJson(rawGson, Message.class);
                        message = (Message) gson.fromJson(rawGson, instance.get(message.getMessageType()));
                        String forLambda = rawGson;
                        Client.LOGGER.info(() -> "Received: " + forLambda);
                    } catch(IOException e){
                        message = new ErrorMessage(null, "Message got wrongly");
                        sendMessage(message);
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
            Server.LOGGER.info(() -> "Sent: " + message);
        }
    }

}
