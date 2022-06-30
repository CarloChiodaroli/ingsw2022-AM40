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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private boolean receivedMessage;
    private ScheduledExecutorService pinger;
    private Thread baseHandlerThread;
    private int misses = 0;
    private int fromLastMessage = 0;

    private final static int PING_TO_LOSE = 5;
    private final static int TIME_TO_LOSE = 180;

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

        this.pinger = Executors.newSingleThreadScheduledExecutor();

        try {
            this.output = new PrintWriter(client.getOutputStream(), true); // Output to client
            this.input = new BufferedReader(new InputStreamReader(client.getInputStream())); // Input from client
        } catch (IOException e) {
            Server.LOGGER.severe(e.getMessage());
        }

        receivedPing = false;
        receivedMessage = false;
    }

    /**
     * Run method that starts all necessary functions to keep the connection alive and receive and parse messages.
     */
    @Override
    public void run() {
        try {
            pingKeepAlive();
            baseHandlerThread = Thread.currentThread();
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
                    if (message != null) {

                        if (message.getMessageType() != MessageType.PING) {
                            if (message.getMessageType() == MessageType.LOGIN) {
                                socketServer.addClient(message.getSenderName(), this);
                            } else {
                                socketServer.onMessageReceived(message);
                            }
                            receivedMessage = true;
                        } else {
                            receivedPing = true;
                        }
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
            pinger.shutdownNow();
            baseHandlerThread.interrupt();

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

    /**
     * Starts a scheduled executor that handles connection liveliness.
     */
    private void pingKeepAlive() {
        pinger.scheduleAtFixedRate(this::pingerControls, 0, 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * Method called by the scheduled executor that actually manages connection liveliness.
     */
    private void pingerControls() {
        if (!receivedPing) {
            if (misses > (PING_TO_LOSE - 3)) {
                Server.LOGGER.info(() -> "MISSED ping from" + client.getPort() + " for the " + misses + "th time");
            }
            misses++;
        } else {
            misses = 0;
            receivedPing = false;
        }
        if (!receivedMessage) {
            if (fromLastMessage > (TIME_TO_LOSE - 5)) {
                Server.LOGGER.info(() -> "Not Received messages from" + client.getPort() + " from " + fromLastMessage + " seconds");
            }
            fromLastMessage++;
        } else {
            fromLastMessage = 0;
            receivedMessage = false;
        }
        if (misses >= PING_TO_LOSE) disconnect();
        if (fromLastMessage >= TIME_TO_LOSE) disconnect();
        sendMessage(new PingMessage("server"));
    }

}
