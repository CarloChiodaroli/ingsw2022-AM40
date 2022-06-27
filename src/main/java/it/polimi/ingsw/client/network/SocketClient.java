package it.polimi.ingsw.client.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.commons.message.ErrorMessage;
import it.polimi.ingsw.commons.message.Message;
import it.polimi.ingsw.commons.message.PingMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

    private final static String defaultAddress = "localhost";
    private final static String defaultPort = "16847";

    /**
     * Getter
     */
    public static String getDefaultAddress() {
        return defaultAddress;
    }

    /**
     * Getter
     */
    public static String getDefaultPort() {
        return defaultPort;
    }

    /**
     * Check the port is valid
     *
     * @param portStr port
     * @return true if is valid
     */
    public static boolean isValidPort(String portStr) {
        try {
            int port = Integer.parseInt(portStr);
            return port >= 1 && port <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check the address is valid
     *
     * @param ip address
     * @return true if is valid
     */
    public static boolean isValidIpAddress(String ip) {
        String regex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        return ip.matches(regex);
    }

    /**
     * Constructor
     */
    public SocketClient(String address, int port) throws IOException {
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(address, port)/*, /*SOCKET_TIMEOUT*/);
        this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.output = new PrintWriter(this.socket.getOutputStream(), true);
        this.readExecutionQueue = Executors.newSingleThreadExecutor();
        this.pinger = Executors.newSingleThreadScheduledExecutor();
        GsonBuilder gsonBuilder = new GsonBuilder();
        this.gson = gsonBuilder.create();
    }

    /**
     * Read a message from the server with socket and notifies the ClientController
     */
    @Override
    public void readMessage() {
        readExecutionQueue.execute(() -> {
            while (!readExecutionQueue.isShutdown()) {
                Message message;
                try {
                    String rawGson;
                    do {
                        rawGson = input.readLine();
                    } while (rawGson == null);
                    message = gson.fromJson(rawGson, Message.class);
                    message = (Message) gson.fromJson(rawGson, message.getMessageType().getImplementingClass());
                    String forLambda = rawGson;
                    //Client.LOGGER.info(() -> "Received: " + forLambda);
                } catch (IOException e) {
                    message = new ErrorMessage(null, "Connection lost with the server.");
                    disconnect();
                    readExecutionQueue.shutdownNow();
                }
                notifyObserver(message);
            }
        });
    }

    /**
     * Sends a message to the server with socket
     *
     * @param message message to be sent
     */
    @Override
    public void sendMessage(Message message) {
        String rawGson = gson.toJson(message);
        output.println(rawGson);
        //if(!message.getMessageType().equals(MessageType.PING)) Client.LOGGER.info(() -> "Sent: " + rawGson);
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
     * Enable a ping messages between client and server sockets to keep the connection alive
     */
    public void enablePinger(boolean enabled) {
        if (enabled) {
            pinger.scheduleAtFixedRate(() -> sendMessage(new PingMessage("Client")), 0, 1000, TimeUnit.MILLISECONDS);
        } else {
            pinger.shutdownNow();
        }
    }
}
