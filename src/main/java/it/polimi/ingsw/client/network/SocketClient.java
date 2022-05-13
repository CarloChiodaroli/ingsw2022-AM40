package it.polimi.ingsw.client.network;

import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.commons.message.ErrorMessage;
import it.polimi.ingsw.commons.message.Message;
import it.polimi.ingsw.commons.message.MessageType;
import it.polimi.ingsw.commons.message.PingMessage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
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
    }

    @Override
    public void readMessage() {
        readExecutionQueue.execute(() -> {

            while (!readExecutionQueue.isShutdown()) {
                Message message;
                try {
                    String rawGson = input.readLine();
                    message = gson.fromJson(rawGson, Message.class);
                    Client.LOGGER.info(() -> "Received: " + rawGson);
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
