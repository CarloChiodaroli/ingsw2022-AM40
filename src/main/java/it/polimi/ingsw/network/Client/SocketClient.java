package it.polimi.ingsw.network.Client;

import it.polimi.ingsw.network.Message.Message;
import it.polimi.ingsw.network.Message.PingMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class represents a socket client implementation.
 * ExecutorService is an API that simplifies running tasks in asynchronous mode.
 */

public class SocketClient extends Client {

    private final Socket socket;

    private final ObjectOutputStream outputStm;
    private final ObjectInputStream inputStm;
    private final ExecutorService readExecutionQueue;   //"Engine"   (Permette di leggere messaggi asincroni)
    private final ScheduledExecutorService pinger;      //Schedule Command to run after a given delay

    private static final int SOCKET_TIMEOUT = 10000;

    public SocketClient(String address, int port) throws IOException {
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(address, port), SOCKET_TIMEOUT);
        this.outputStm = new ObjectOutputStream(socket.getOutputStream());
        this.inputStm = new ObjectInputStream(socket.getInputStream());
        this.readExecutionQueue = Executors.newSingleThreadExecutor();//
        this.pinger = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Asynchronously reads a message from the server via socket and notifies the ClientController.
     */
    @Override
    public void readMessage() {
        readExecutionQueue.execute(() -> {
            while (!readExecutionQueue.isShutdown()) {
                Message message;
                try {
                    message = (Message) inputStm.readObject();
                    System.out.println(message);
                } catch (IOException | ClassNotFoundException e) {
                    disconnect();
                    readExecutionQueue.shutdownNow();
                }
                //Notifica evento (Observer)
            }
        });
    }

    /**
     * Sends a message to the server via socket.
     *
     * @param message the message to be sent.
     */
    @Override
    public void sendMessage(Message message) {
        try {
            outputStm.writeObject(message);
            outputStm.reset();
        } catch (IOException e) {
            disconnect();
            //Notifica evento (Observer) errore
        }
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
            //Notifica evento (Observer) errore

        }
    }

    /**
     * Do ping between client and server
     *
     * @param enabled
     * if true ping is enable
     * if false ping is disable
     */
    public void enablePinger(boolean enabled) {
        if (enabled) {
            pinger.scheduleAtFixedRate(() -> sendMessage(new PingMessage()), 0, 1000, TimeUnit.MILLISECONDS);
        } else {
            pinger.shutdownNow();
        }
    }

}