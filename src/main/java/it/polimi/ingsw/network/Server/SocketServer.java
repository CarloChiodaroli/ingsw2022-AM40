package it.polimi.ingsw.network.Server;

import it.polimi.ingsw.network.Message.Message;

import javax.swing.plaf.TableHeaderUI;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * It handles all the new socket connection.
 * It is a socket and there is installed the server
 */
public class SocketServer  implements  Runnable{
    private final int port;
    private final Server server;
    ServerSocket serverSocket;

    public SocketServer(Server server,int port)
    {
        this.server=server;
        this.port=port;
    }

    public void run()
    {
        try
        {
        serverSocket = new ServerSocket(port);
        }catch (IOException e) {
            return;
        }
        while (!Thread.currentThread().isInterrupted())
        {
            try {
                Socket client = serverSocket.accept();
                client.setSoTimeout(5000); // Timer that lunch error if the method doesn't finish
                SocketClientHandler clientHandler = new SocketClientHandler(this,client);

                Thread thread = new Thread(clientHandler,"Handler : "+client.getInetAddress());
                thread.start();

            }catch (IOException e)
            {

            }
        }
    }


    public void addClient(String playerName,ClientHandler clientHandler)
    {
        server.addClient(playerName,clientHandler);
    }

    public void onMessageReceived(Message message)
    {
        server.onMessageReceived();
    }

    public void onDisconnect(ClientHandler clientHandler)
    {
        server.onDisconnect(clientHandler);
    }
}
