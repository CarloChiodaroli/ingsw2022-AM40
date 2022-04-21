package it.polimi.ingsw.network.Client;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import it.polimi.ingsw.network.Server.SocketServer;
import it.polimi.ingsw.network.Message.Message;
import it.polimi.ingsw.network.Message.*;

public class SocketClientHandler implements ClientHandler,Runnable{

    private final Socket client;
    private final SocketServer socketServer;

    private boolean connected;

    private final Object inputLock;
    private final Object outputLock;

    private ObjectOutputStream output;
    private ObjectInputStream input;

public SocketClientHandler(SocketServer socketServer,Socket client)
{
    this.socketServer = socketServer;
    this.client = client;
    this.connected = true;
    this.inputLock = new Object();
    this.outputLock = new Object();

    try {
        this.output = new ObjectOutputStream(client.getOutputStream());
        this.input = new ObjectInputStream(client.getInputStream());
    } catch (IOException e) {
         new IOException("Stream I/O Error");
    }
}

    public void run() {
        try {
            handleClientConnection();
        } catch (IOException e) {
            System.out.println("Client " + client.getInetAddress() + " connection dropped.");
            disconnect();
        }
    }

    /**
     * Handles the connection of a new client and keep listening to the socket for new messages.
     *
     * @throws IOException any of the usual Input/Output exceptions.
     */
    private void handleClientConnection() throws IOException {
        System.out.println("Client connected from " + client.getInetAddress());

        try {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (inputLock) {
                    Message message = (Message) input.readObject();

                    if (message != null && message.getMessageType() != MessageType.PING) {
                        if (message.getMessageType() == MessageType.LOGIN_REQUEST)
                        {
                            //socketServer.addClient(message.getPlayerName(), this);
                        }
                        else
                        {
                            System.out.println( "Received: " + message);
                            //socketServer.onMessageReceived(message);
                        }
                    }
                }
            }
        } catch (ClassCastException | ClassNotFoundException e) {
            System.out.println("Invalid stream from client");
        }
        client.close();
    }
    /**
     * Returns the current status of the connection.
     *
     * @return {@code true} if the connection is still active, {@code false} otherwise.
     */
    @Override
    public boolean isConnected() {
        return connected;
    }

    /**
     * Disconnect the socket.
     */
    @Override
    public void disconnect() {
        if (connected) {
            try {
                if (!client.isClosed()) {
                    client.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            connected = false;
            Thread.currentThread().interrupt();

            socketServer.onDisconnect(this);
        }
    }

    /**
     * Sends a message to the client via socket.
     *
     * @param message the message to be sent.
     */
    @Override
    public void sendMessage(Message message) {
        try {
            synchronized (outputLock) {
                output.writeObject(message);
                output.reset();
                System.out.println("Sent: " + message);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            disconnect();
        }
    }
}
