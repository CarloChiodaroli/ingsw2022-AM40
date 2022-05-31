package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.model.PlayMessageController;
import it.polimi.ingsw.client.network.Client;
import it.polimi.ingsw.client.network.SocketClient;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.observer.ViewObservable;
import it.polimi.ingsw.server.model.enums.Characters;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


/**
 * This class offers a User Interface to the user via terminal. It is an implementation of the {@link View}.
 */
public class Cli extends ViewObservable implements View {

    private final PrintStream out;
    private final ClientInputStream inputStream;
    private Object userInputLock;
    private PlayMessageController playMessageController;
    private boolean connected;
    private StatePrinter statePrinter;
    private Map<String, String> serverInfo = new HashMap<>();

    private static final String STR_INPUT_CANCELED = "User input canceled.";

    public Cli() {
        out = System.out;
        this.inputStream = new ClientInputStream(this);
        this.userInputLock = new Object();
        connected = false;
    }

    public void setStatePrinter(PlayMessageController playMessageController) {
        this.playMessageController = playMessageController;
        this.statePrinter = new StatePrinter(playMessageController.getState());
    }

    @Override
    public void update() {
        try {
            out.println(statePrinter.getState());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // command reader
    public void receivedCommand(String command) throws IllegalStateException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        synchronized (userInputLock) {
            List<String> tiledCommand = Arrays.stream(command.split(" ")).toList();
            tiledCommand = new ArrayList<>(tiledCommand);
            String head = tiledCommand.get(0);
            tiledCommand.remove(head);
            head = head.toLowerCase();
            this.getClass().getMethod(head, List.class).invoke(this, tiledCommand);
        }
    }

    // User commands

    /**
     * Command which lists the help
     *
     * @param args should be present but empty
     */
    public void help(List<String> args) {
        out.println("""
                List of all possible commands:\s
                \tConnection commands:\s
                \t\tip <address> \t: sets the ip of the server to connect\s
                \t\tport <number> \t: sets the server's listening port\s
                \tLobby commands:\s
                \t\tname <name> \t-\t: sets personal player name
                \t\tnickname <name> \t: same as name
                \t\tplayers <number> \t: sets the number of players\s
                \t\texpert \t-\t-\t-\t: switches the game to and from expert variant\s
                \tPlay commands: abbreviation command <arg> <arg> <arg>\s
                \tas\tassistant <weight> \t-\t-\t-\t-\t-\t-\t-\t: command used to play an assistant card
                \tsm\tstudentmove <student color> <from id> <to id> \t: command to move a student
                \tmnm\tmnmove <hops> \t-\t-\t-\t-\t-\t-\t-\t-\t: command to move mother nature of x hops
                \tim\tinfluence \t-\t-\t-\t-\t-\t-\t-\t-\t-\t: command to calc influence
                \tch\tchoose <cloud id> \t-\t-\t-\t-\t-\t-\t-\t: command to choose a cloud
                To send a valid command please write the command with arguments as for example:
                \tstudentmove BLUE Entrance Room
                Or use the abbreviation in place of the command as for example:
                \tsm BLUE Entrance Room
                """);
    }

    public void ip(List<String> args) throws IllegalStateException {
        if (connected) throw new IllegalStateException("Connection already established");
        if (args.isEmpty()) {
            serverInfo.put("address", SocketClient.getDefaultAddress());
            askForServerPort();
        } else if (SocketClient.isValidIpAddress(args.get(0))) {
            serverInfo.put("address", args.get(0));
            askForServerPort();
        } else {
            out.println("Invalid address!");
            clearCli();
            askForServerIp();
        }
    }

    public void port(List<String> args) throws IllegalStateException {
        if (connected) throw new IllegalStateException("Connection already established");
        if (args.isEmpty()) {
            serverInfo.put("port", SocketClient.getDefaultPort());
        } else {
            if (SocketClient.isValidPort(args.get(0))) {
                serverInfo.put("port", args.get(0));
            } else {
                out.println("Invalid port!");
                clearCli();
                askForServerPort();
                return;
            }
        }
        notifyObserver(obs -> obs.onUpdateServerInfo(serverInfo));
        connected = true;
    }

    public void players(List<String> args) {
        int number = Integer.parseInt(args.get(0));
        notifyObserver(obs -> obs.onUpdatePlayersNumber(number));
    }

    public void name(List<String> args) {
        if (args.isEmpty()) throw new IllegalStateException();
        notifyObserver(obs -> obs.onUpdateNickname(args.get(0)));
    }

    public void nickname(List<String> args) {
        try {
            name(args);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void as(List<String> args) {
        assistant(args);
    }

    public void assistant(List<String> args) {
        if (args.isEmpty()) throw new IllegalStateException();
        int weight = Integer.parseInt(args.get(0));
        playMessageController.playAssistantCard(playMessageController.getNickname(), weight);
    }

    public void sm(List<String> args) throws IllegalAccessException {
        try {
            studentmove(args);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void studentmove(List<String> args) throws IllegalAccessException {
        if (args.size() < 3) throw new IllegalStateException();
        TeacherColor toMove = TeacherColor.valueOf(args.get(0).toUpperCase());
        String toPlace = verifyId(args.get(2));
        Optional<String> secondArg = Arrays.stream(TeacherColor.values())
                .map(Enum::toString)
                .filter(x -> x.equalsIgnoreCase(args.get(1)))
                .findFirst();
        if (secondArg.isPresent()) {
            TeacherColor secondToMove = TeacherColor.valueOf(secondArg.get());
            playMessageController.moveStudent(playMessageController.getNickname(), toMove, secondToMove, toPlace);
        } else {
            String fromPlace = verifyId(args.get(1));
            playMessageController.moveStudent(playMessageController.getNickname(), toMove, fromPlace, toPlace);
        }
    }

    private String verifyId(String id) throws IllegalAccessException {
        return playMessageController.getPlaceIds().stream()
                .filter(x -> x.equalsIgnoreCase(id))
                .findFirst()
                .orElseThrow(() -> new IllegalAccessException("place " + id + " not found"));
    }

    public void mnm(List<String> args) {
        mnmove(args);
    }

    public void mnmove(List<String> args) {
        if (args.isEmpty()) throw new IllegalStateException();
        int hops = Integer.parseInt(args.get(0));
        playMessageController.moveMotherNature(playMessageController.getNickname(), hops);
    }

    public void in(List<String> args) {
        influence(args);
    }

    public void influence(List<String> args) {
        playMessageController.calcInfluence(playMessageController.getNickname());
    }

    public void ch(List<String> args) throws IllegalAccessException {
        try {
            choose(args);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void choose(List<String> args) throws IllegalAccessException {
        if (args.isEmpty()) throw new IllegalStateException();
        playMessageController.chooseCloud(playMessageController.getNickname(), verifyId(args.get(0)));
    }

    public void character(List<String> args) {
        if (args.isEmpty()) throw new IllegalStateException();
        Characters character = Characters.valueOf(args.get(0).toUpperCase());
        if (args.size() == 1) {
            playMessageController.playCharacterCard(playMessageController.getNickname(), character);
            return;
        }
        Optional<String> secondArg = Arrays.stream(TeacherColor.values())
                .map(Enum::toString)
                .filter(x -> x.equalsIgnoreCase(args.get(1)))
                .findFirst();
        if (secondArg.isPresent()) {
            playMessageController.playCharacterCard(playMessageController.getNickname(), character, TeacherColor.valueOf(args.get(1).toUpperCase()));
        } else {
            playMessageController.playCharacterCard(playMessageController.getNickname(), character, args.get(1).toLowerCase());
        }
    }

    @Override
    public void showActualState() {
        out.println(statePrinter.roundState());
    }

    @Override
    public void showMainPlayerName(String mainPlayerName) {
        out.println("This play's main player is " + mainPlayerName);
    }

    public void init() {
        out.println("Welcome to Eriantys!");
        askServerInfo();
    }

    public void askForServerIp() {
        out.println("Please enter server's ip address [default is " + SocketClient.getDefaultAddress() + "].");
        out.println("ip <address>");
    }

    public void askForServerPort() {
        out.println("Please now enter game's listening port [default is " + SocketClient.getDefaultAddress() + "]");
        out.println("port <port number>");
    }

    public void askServerInfo() {
        out.println("Please specify the following settings. The default value is shown between brackets.");
        askForServerIp();
    }

    @Override
    public void askNickname() {
        out.println("To start the game, set your nickname with the nickname command:\nnickname <nickname>    or\nname <nickname>");
    }

    @Override
    public void askPlayersNumber() {
        out.println("You are the first player to connect to the server, please define the play details with these commands:");
        out.println("players <num of players>\t// To set the number of players. acceptable numbers are 2 or 3");
        out.println("expert\t\t\t// To set the game to expert variant or vice-versa");
    }

    @Override
    public void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname) {
        clearCli();
        if (nicknameAccepted && connectionSuccessful) {
            out.println("Hi, " + nickname + "! You connected to the server.");
        } else if (connectionSuccessful) {
            out.println("Hi, seems like someone has already used your username... Please chose an other one");
            askNickname();
        } else if (nicknameAccepted) {
            out.println("Max players reached. Connection refused.");
            out.println("EXIT.");
            System.exit(1);
        } else {
            showErrorAndExit("Could not contact server.");
            System.exit(1);
        }
    }

    @Override
    public void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful) {

    }

    public void showError(String errorMessage) {
        out.println(errorMessage);
    }

    @Override
    public void showGenericMessage(String genericMessage) {
        out.println(genericMessage);
    }

    @Override
    public void showDisconnectionMessage(String nicknameDisconnected, String text) {
        out.println("\n" + nicknameDisconnected + text);
        Client.LOGGER.info("Closing client for disconnection");
        System.exit(1);
    }

    public void showOtherDisconnectionMessage(String nicknameDisconnected, String text) {
        out.println("\n" + nicknameDisconnected + text);
    }

    @Override
    public void showErrorAndExit(String error) {
        inputStream.stopReading();
        out.println("\nERROR: " + error);
        out.println("EXIT.");
        Client.LOGGER.info("Closing client for error: error");
        System.exit(1);
    }

    @Override
    public void showLobby(List<String> nicknameList, int numPlayers) {
        out.println("LOBBY:");
        for (String nick : nicknameList) {
            out.println(nick + "\n");
        }
        out.println("Current players in lobby: " + nicknameList.size() + " / " + numPlayers);
    }

    public void clearCli() {
        out.print(ColorCli.CLEAR);
        out.flush();
    }
}
