package it.polimi.ingsw.server.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.commons.message.ErrorMessage;
import it.polimi.ingsw.commons.message.Message;
import it.polimi.ingsw.commons.message.MessageType;
import it.polimi.ingsw.commons.message.PingMessage;

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

    private boolean receivedPing;

    /**
     * Default constructor
     */
    public SocketClientHandler(SocketServer socketServer, Socket client) {
        this.socketServer = socketServer;
        this.client = client;
        this.connected = true;
        // GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting(); // needed while testing with netcat
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

        receivedPing = false;
    }

    @Override
    public void run() {
        try {
            pingKeepAlive();
            handleClientConnection();
        } catch (IOException e) {
            Server.LOGGER.severe("Client " + client.getInetAddress() + " connection dropped.");
            disconnect();
        }
    }

    /**
     * Handles the connection of a new client and keep listening to the socket for new messages
     *
     * @throws IOException any of the usual Input/Output related exceptions
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
                        if (message != null) {
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
                    if (message != null)
                        if (message.getMessageType() != MessageType.PING) {
                            if (message.getMessageType() == MessageType.LOGIN) {
                                socketServer.addClient(message.getSenderName(), this);
                            } else {
                                socketServer.onMessageReceived(message);
                            }
                        } else {
                            receivedPing = true;
                        }
                }
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
            Server.LOGGER.severe("Invalid stream from client");
        }
        client.close();
    }

    /**
     * Get status of connection
     *
     * @return true if the connection is active
     */
    @Override
    public boolean isConnected() {
        return connected;
    }

    /**
     * Disconnect the socket
     */
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

    /**
     * Sends a message to the client through socket
     *
     * @param message message
     */
    @Override
    public void sendMessage(Message message) {
        synchronized (outputLock) {
            String rawGson = gson.toJson(message);
            output.println(rawGson);
            if (!message.getMessageType().equals(MessageType.PING))
                Server.LOGGER.info(() -> "Sent: " + rawGson);
        }
    }

    private void pingKeepAlive() {
        new Thread(new Runnable() {
            int misses = 0;

            @Override
            public void run() {
                while (true) {
                    if (!receivedPing) {
                        System.out.println("MISSED ping from " + client.getInetAddress() + ":" + client.getPort());
                        System.out.println(misses);
                        misses++;
                    } else {
                        System.out.println("received ping from " + client.getInetAddress() + ":" + client.getPort());
                        misses = 0;
                        receivedPing = false;
                    }
                    if (misses >= 5) disconnect();
                    sendMessage(new PingMessage("server"));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
