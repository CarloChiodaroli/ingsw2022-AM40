package it.polimi.ingsw.client.network;

import it.polimi.ingsw.commons.message.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class represents a socket client implementation.
 */

public class SocketClient extends Client {

    private final Socket socket;

    private final ExecutorService readExecutionQueue;
    private final ScheduledExecutorService pinger;

    private final PrintWriter output;
    private final BufferedReader input;

    private final Gson gson;
    private final Map<MessageType, Class<?>> instance;

    private static final int SOCKET_TIMEOUT = 10000;

    public SocketClient(String address, int port) throws IOException {
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(address, port), SOCKET_TIMEOUT);
        this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.output = new PrintWriter(this.socket.getOutputStream(), true);
        this.readExecutionQueue = Executors.newSingleThreadExecutor();
        this.pinger = Executors.newSingleThreadScheduledExecutor();
        GsonBuilder gsonBuilder = new GsonBuilder();
        this.gson = gsonBuilder.create();
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
    public void readMessage() {
        readExecutionQueue.execute(() -> {

            while (!readExecutionQueue.isShutdown()) {
                Message message;
                try {
                    String rawGson;
                    do {
                        rawGson = input.readLine();
                    } while(rawGson == null);
                    message = gson.fromJson(rawGson, Message.class);
                    message = (Message) gson.fromJson(rawGson, instance.get(message.getMessageType()));
                    String forLambda = rawGson;
                    Client.LOGGER.info(() -> "Received: " + forLambda);
                } catch (IOException e) {
                    message = new ErrorMessage(null, "Connection lost with the server.");
                    disconnect();
                    readExecutionQueue.shutdownNow();
                }
                notifyObserver(message);
            }
        });
    }

    @Override
    public void sendMessage(Message message) {
        String rawGson = gson.toJson(message);
        output.println(rawGson);
        if(!message.getMessageType().equals(MessageType.PING)) Client.LOGGER.info(() -> "Sent: " + rawGson);
    }

    /**
     * Disconnect the socket from the server.
     */
    @Override
    public void disconnect() {
        try {
            if (!socket.isClosed()) {
                readExecutionQueue.shutdownNow();
                enablePinger(false);
                socket.close();
            }
        } catch (IOException e) {
            notifyObserver(new ErrorMessage(null, "Could not disconnect."));
        }
    }

    /**
     * Enable a heartbeat (ping messages) between client and server sockets to keep the connection alive.
     */
    public void enablePinger(boolean enabled) {
        if (enabled) {
            pinger.scheduleAtFixedRate(() -> sendMessage(new PingMessage("Client")), 0, 1000, TimeUnit.MILLISECONDS);
        } else {
            pinger.shutdownNow();
        }
    }
}
