package it.polimi.ingsw.controller;

import it.polimi.ingsw.Observer.Observer;
import it.polimi.ingsw.Observer.ViewObserver;
import it.polimi.ingsw.network.Client.Client;
import it.polimi.ingsw.network.Client.SocketClient;
import it.polimi.ingsw.network.Message.*;
import it.polimi.ingsw.view.cli.View;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientController implements ViewObserver, Observer {

    private final View view;

    private Client client;
    private String playerName;

    private final ExecutorService taskQueue;

    public static final int UNDO_TIME = 5000;

    /**
     * Constructs Client Controller.
     *
     * @param view the view to be controlled.
     */
    public ClientController(View view) {
        this.view = view;
        taskQueue = Executors.newSingleThreadExecutor();
    }

    /**
     * Create a new Socket Connection to the server with the updated info.
     * An error view is shown if connection cannot be established.
     *
     * @param serverInfo a map of server address and server port.
     */
    @Override
    public void onUpdateServerInfo(Map<String, String> serverInfo) {
        try {
            client = new SocketClient(serverInfo.get("address"), Integer.parseInt(serverInfo.get("port")));
            client.addObserver(this);
            client.readMessage(); // Starts an asynchronous reading from the server.
            client.enablePinger(true);
            taskQueue.execute(view::askPlayerName);
        } catch (IOException e) {
            taskQueue.execute(() -> view.showLoginResult(false, false, this.playerName));
        }
        System.out.println("Finish onUpdateServerInfo");
    }

    /**
     * Sends a message to the server with the updated nickname.
     * The nickname is also stored locally for later usages.
     *
     * @param playerName the nickname to be sent.
     */
    @Override
    public void onUpdateNickname(String playerName) {
        this.playerName = playerName;
        client.sendMessage(new LoginRequest(this.playerName));
    }

    /**
     * Sends a message to the server with the player number chosen by the user.
     *
     * @param playersNumber the number of players.
     */
    @Override
    public void onUpdatePlayersNumber(int playersNumber) {
        client.sendMessage(new PlayerNumberReply(this.playerName, playersNumber));
    }

    public void update(Message message)
    {
        switch (message.getMessageType()) {

            case LOGIN_REPLY:
                LoginReply loginReply = (LoginReply) message;
                taskQueue.execute(() -> view.showLoginResult(loginReply.isPlayerNameAccepted(), loginReply.isConnectionSuccessful(), this.playerName));
                break;
            case PLAYERNUMBER_REQUEST:
                taskQueue.execute(view::askPlayerName);
                break;
            case DISCONNECTION:
                DisconnectionMessage dm = (DisconnectionMessage) message;
                client.disconnect();
                view.showDisconnectionMessage(dm.getPlayerNameDisconnectedDisconnected(), dm.getMessageStr());
                break;
        }

    }

    }