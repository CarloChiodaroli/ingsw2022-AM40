package it.polimi.ingsw.network.Client;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.Message.ErrorMessage;
import it.polimi.ingsw.network.Message.Message;
import it.polimi.ingsw.network.Message.MessageType;
import it.polimi.ingsw.network.Message.PingMessage;
import it.polimi.ingsw.network.Server.Server;

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

    private PrintWriter output;
    private BufferedReader input;

    private final GsonBuilder gsonBuilder;
    private final Gson gson;

    private static final int SOCKET_TIMEOUT = 10000;

    public SocketClient(String address, int port) throws IOException {
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(address, port), SOCKET_TIMEOUT);
        this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.output = new PrintWriter(this.socket.getOutputStream(), true);
        this.readExecutionQueue = Executors.newSingleThreadExecutor();
        this.pinger = Executors.newSingleThreadScheduledExecutor();
        this.gsonBuilder = new GsonBuilder().setPrettyPrinting();
        this.gson = gsonBuilder.create();
    }

    @Override
    public void readMessage() {
        readExecutionQueue.execute(() -> {

            while (!readExecutionQueue.isShutdown()) {
                Message message;
                try {
                    String line;
                    String rawGson = "";
                    line = input.readLine();
                    while (line != null) {
                        rawGson += line;
                        line = input.readLine();
                    }
                    message = gson.fromJson(rawGson, Message.class);

                    String goat = rawGson;

                    Server.LOGGER.info(() -> "Received: " + goat);
                    //message = (Message) inputStm.readObject();
                    //LOGGER.info("Received: " + message);
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
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();
        String rawGson = gson.toJson(message);
        output.println(rawGson);
        if(!message.getMessageType().equals(MessageType.PING)) Server.LOGGER.info(() -> "Sent: " + message);
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
            pinger.scheduleAtFixedRate(() -> sendMessage(new PingMessage()), 0, 1000, TimeUnit.MILLISECONDS);
        } else {
            pinger.shutdownNow();
        }
    }
}
