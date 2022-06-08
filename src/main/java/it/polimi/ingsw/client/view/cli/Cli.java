package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.model.PlayMessageController;
import it.polimi.ingsw.client.network.Client;
import it.polimi.ingsw.client.network.SocketClient;
import it.polimi.ingsw.client.observer.ViewObservable;
import it.polimi.ingsw.client.observer.ViewObserver;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.Wizard;
import it.polimi.ingsw.commons.enums.Characters;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


/**
 * This class offers a Command Line User Interface. It is an implementation of the {@link View} interface.
 *
 */
public class Cli extends ViewObservable implements View {

    private final PrintStream out;
    private final ClientInputStream inputStream;
    private Object userInputLock;
    private PlayMessageController playMessageController;
    private boolean connected;
    private StatePrinter statePrinter;
    private Map<String, String> serverInfo = new HashMap<>();
    private boolean expert;

    /**
     * Constructor
     */
    public Cli() {
        out = System.out;
        this.inputStream = new ClientInputStream(this);
        this.userInputLock = new Object();
        connected = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStatePrinter(PlayMessageController playMessageController) {
        this.playMessageController = playMessageController;
        this.statePrinter = new StatePrinter(playMessageController.getState());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        out.println(statePrinter.getState());
    }

    // command reader

    /**
     * Called from the Client Input Stream class which manages the input stream of commands.
     * This method divides the received command argument in parts and calls the correct method to run the user desired command.
     *
     * The Accepted commands are:
     * <pre>
     *  Connection commands:
     *     {@link #command(List) command}  arg      : description
     *     {@link #ip(List) ip}       address  : sets the ip of the server to connect
     *     {@link #port(List) port}     number   : sets the server's listening port
     *
     *  Lobby commands:
     *     {@link #command(List) command}  arg      : description
     *     {@link #name(List) name}     name     : sets personal player name
     *     {@link #nickname(List) nickname} name     : same as name
     *     {@link #players(List) players}   number  : sets the number of players
     *     {@link #expert(List) expert}            : switches the game to and from expert variant
     *     {@link #wizard(List) wizard}   which    : allows to choose which wizard to be between: King, Fairy, Magician and Bamboo_Guy
     *     {@link #start(List) start}             : starts the game
     *
     *  Play commands:
     *     {@link #abbreviation(List) abbreviation}     {@link #command(List) command}      arg             arg     arg      : description
     *     {@link #as(List) as}              {@link #assistant(List) assistant}    weight                           : command used to play an assistant card
     *     {@link #sm(List) sm}              {@link #studentmove(List) studentmove}  student color   from id to id    : command to move a student
     *     {@link #mnm(List) mnm}             {@link #mnmove(List) mnmove}       hops                             : command to move mother nature of x hops
     *     {@link #in(List) im}              {@link #influence(List) influence}                                     : command to calc influence
     *     {@link #ch(List) ch}              {@link #choose(List) choose}       cloud id                         : command to choose a cloud
     *
     *  Expert Play commands:
     *     abbreviation    command                      : description
     * </pre>
     *
     * @param command the raw command received from the user.
     * @throws IllegalStateException     exception launched when the user writes wrongly a message - wrong arguments
     * @throws NoSuchMethodException     exception launched when the user writes wrongly a message - wrong command {@link Class#getMethod(String, Class[])}
     * @throws InvocationTargetException exception launched when the user writes wrongly a message - wrong arguments or state {@link Method#invoke(Object, Object...)}
     * @throws IllegalAccessException    exception launched when the user writes wrongly a message - wrong command {@link Method#invoke(Object, Object...)}
     */
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
     * @param args {@link #receivedCommand(String)}
     */
    public void help(List<String> args) {
        out.println("""
                List of all possible commands:
                    Connection commands:
                        command <arg>   : description
                        ip <address>    : sets the ip of the server to connect
                        port <number>   : sets the server's listening port
                        
                    Lobby commands:
                        command <arg>       : description
                        name <name>         : sets personal player name
                        nickname <name>     : same as name
                        players <number>    : sets the number of players
                        expert              : switches the game to and from expert variant
                        wizard <which>      : allows to choose which wizard to be between: King, Fairy, Magician and Bamboo_Guy
                        start               : starts the game
                        
                    Play commands: 
                        abbreviation    command <arg> <arg> <arg>                       : description
                        as              assistant <weight>  -   -   -   -   -   -   -   : command used to play an assistant card
                        sm              studentmove <student color> <from id> <to id>   : command to move a student
                        mnm             mnmove <hops>   -   -   -   -   -   -   -   -   : command to move mother nature of x hops
                        im              influence   -   -   -   -   -   -   -   -   -   : command to calc influence
                        ch              choose <cloud id>   -   -   -   -   -   -   -   : command to choose a cloud
                        
                    Expert Play commands:
                        abbreviation    command <arg> <arg> <arg>                       : description
                    
                                
                To send a valid command please write the command with arguments as for example:
                    studentmove BLUE Entrance Room
                    
                Or use the abbreviation in place of the command as for example:
                    sm BLUE Entrance Room
                    
                Commands are not case sensitive so you can write:
                    sm blue entrance room
                                
                These three example commands do the same thing: move a blue student from the entrance to the room
                """);
    }

    /**
     * Command which sets the ip to connect to the server.
     *
     * @param args {@link #receivedCommand(String)}
     * @throws IllegalStateException thrown when the connection has been already established.
     */
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

    /**
     * Command which sets the listening port of the server.
     *
     * @param args {@link #receivedCommand(String)}
     * @throws IllegalStateException thrown when the connection has been already established.
     */
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

    // Lobby commands

    /**
     * Command which sets the number of players.
     *
     * @param args {@link #receivedCommand(String)}
     * @throws IllegalStateException thrown when the command has no arguments.
     */
    public void players(List<String> args) throws IllegalStateException {
        if (args.isEmpty()) throw new IllegalStateException();
        int number = Integer.parseInt(args.get(0));
        notifyObserver(obs -> obs.onUpdatePlayersNumber(number));
    }

    /**
     * Command which sets the client's nickname.
     *
     * @param args {@link #receivedCommand(String)}
     * @throws IllegalStateException thrown when the command receives no arguments.
     */
    public void name(List<String> args) throws IllegalStateException {
        if (args.isEmpty()) throw new IllegalStateException();
        notifyObserver(obs -> obs.onUpdateNickname(args.get(0)));
    }

    /**
     * {@link #name(List)}
     */
    public void nickname(List<String> args) throws IllegalStateException {
        try {
            name(args);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    /**
     * Command which sets and sends the user's wizard preference.
     *
     * @param args {@link #receivedCommand(String)}
     * @throws IllegalStateException thrown when the command receives no arguments.
     */
    public void wizard(List<String> args) throws IllegalStateException {
        if (args.isEmpty()) throw new IllegalStateException();
        Wizard wizard = Wizard.valueOf(args.get(0).toUpperCase());
        notifyObserver(obs -> obs.onUpdateWizard(wizard));
    }

    /**
     * Command which starts the game;
     *
     * @param args {@link #receivedCommand(String)}
     */
    public void start(List<String> args) {
        notifyObserver(ViewObserver::onUpdateStart);
    }

    /**
     * Command which switches the expert variant selection.
     *
     * @param args {@link #receivedCommand(String)}
     */
    public void expert(List<String> args) {
        notifyObserver(obs -> obs.onUpdateExpert(!expert));
    }

    // Play commands

    /**
     * {@link #abbreviation(List)}
     * {@link #assistant(List)}
     */
    public void as(List<String> args) throws IllegalStateException {
        assistant(args);
    }

    /**
     * Command which sets and sends the user's wizard preference.
     *
     * @param args {@link #receivedCommand(String)}
     * @throws IllegalStateException thrown when the command receives no arguments.
     */
    public void assistant(List<String> args) throws IllegalStateException {
        if (args.isEmpty()) throw new IllegalStateException();
        int weight = Integer.parseInt(args.get(0));
        if (!playMessageController.getState().getAssistantCards().contains(weight)) throw new IllegalStateException();
        playMessageController.playAssistantCard(playMessageController.getNickname(), weight);
    }

    /**
     * {@link #abbreviation(List) abbreviation} of
     * {@link #assistant(List) assistant}
     */
    public void sm(List<String> args) throws IllegalAccessException {
        try {
            studentmove(args);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    /**
     * Command used to move students
     *
     * @param args {@link #receivedCommand(String)}
     * @throws IllegalAccessException thrown when there are not enough arguments.
     */
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

    /**
     * Method used by the studentmove command to check if the id arguments on the command are valid ids or not.
     *
     * @param id the id to verify.
     * @return the case corrected verified id.
     * @throws IllegalAccessException thrown when the gotten id does not exist in the Play State class.
     */
    private String verifyId(String id) throws IllegalAccessException {
        return playMessageController.getPlaceIds().stream()
                .filter(x -> x.equalsIgnoreCase(id))
                .findFirst()
                .orElseThrow(() -> new IllegalAccessException("place " + id + " not found"));
    }

    /**
     * {@link #abbreviation(List) abbreviation} of
     * {@link #mnmove(List) mnmove}
     */
    public void mnm(List<String> args) throws IllegalStateException {
        mnmove(args);
    }

    /**
     * Command to move mother nature.
     *
     * @param args {@link #receivedCommand(String)}
     * @throws IllegalStateException thrown when there are no arguments.
     */
    public void mnmove(List<String> args) throws IllegalStateException {
        if (args.isEmpty()) throw new IllegalStateException();
        int hops = Integer.parseInt(args.get(0));
        playMessageController.moveMotherNature(playMessageController.getNickname(), hops);
    }

    /**
     * {@link #abbreviation(List) abbreviation} of
     * {@link #influence(List) influence}
     */
    public void in(List<String> args) {
        influence(args);
    }

    /**
     * Command to calc influence.
     *
     * @param args {@link #receivedCommand(String)}
     */
    public void influence(List<String> args) {
        playMessageController.calcInfluence(playMessageController.getNickname());
    }

    /**
     * {@link #abbreviation(List) abbreviation} of
     * {@link #choose(List) choose}
     */
    public void ch(List<String> args) throws IllegalAccessException {
        try {
            choose(args);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    /**
     * Command to choose a cloud.
     *
     * @param args {@link #receivedCommand(String)}
     * @throws IllegalAccessException thrown when there are no arguments.
     */
    public void choose(List<String> args) throws IllegalAccessException {
        if (args.isEmpty()) throw new IllegalStateException();
        playMessageController.chooseCloud(playMessageController.getNickname(), verifyId(args.get(0)));
    }

    /**
     * Command to choose a character card to use.
     *
     * @param args {@link #receivedCommand(String)}
     * @throws IllegalStateException thrown when there are no arguments.
     */
    public void character(List<String> args) throws IllegalStateException {
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

    /**
     * A shortcut used to call a {@link #command(List) command}
     */
    public void abbreviation(List<String> args) {
        command(args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showMainPlayerName(String mainPlayerName) {
        out.println("This play's main player is " + mainPlayerName);
    }

    /**
     * Shows the first message when CLI client starts
     */
    public void init() {
        out.println("Welcome to Eriantys!");
        askServerInfo();
    }

    /**
     * Example of the interface of a command method. It has no practical use.
     */
    public void command(List<String> args) {
        out.println("hahahahaha... Very clever!!");
    }

    /**
     * Asks the user to write server ip.
     */
    public void askForServerIp() {
        out.println("Please enter server's ip address [default is " + SocketClient.getDefaultAddress() + "].");
        out.println("ip <address>");
    }

    /**
     * Asks the user to write server port.
     */
    public void askForServerPort() {
        out.println("Please now enter game's listening port [default is " + SocketClient.getDefaultPort() + "]");
        out.println("port <port number>");
    }

    /**
     * Asks the user to write server connection infos.
     */
    public void askServerInfo() {
        out.println("Please specify the following settings. The default value is shown between brackets.");
        askForServerIp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void askNickname() {
        out.println("To start the game, set your nickname with the nickname command:\nnickname <nickname>    or\nname <nickname>");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void askPlaySettings() {
        out.println("\nYou are the first player to connect to the server, please define the play details with these commands:");
        out.println("players <num of players>\t// To set the number of players. acceptable numbers are 2 or 3");
        out.println("expert\t\t\t// To set the game to expert variant or vice-versa");
        askPlayCustomization();
        out.println("\nThan when you are finished use the command 'start' to start the game");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void askPlayCustomization() {
        out.println("\nYou are connected and the server has been correctly configured, now you can define your customizations with these commands:");
        out.println("wizard <MAGICIAN, KING, FAIRY, BAMBOO_GUY> // To set the number of players. acceptable numbers are 2 or 3");
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    public void showError(String errorMessage) {
        out.println(EscapeCli.RED + errorMessage + EscapeCli.DEFAULT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showWizard() {
        String wizard = statePrinter.wizard();
        if (wizard != null) {
            out.println("Your Wizard is " + wizard);
        } else {
            out.println("Your choice was not accepted, please choose an other one!");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showExpert(boolean expertStatus) {
        expert = expertStatus;
        if (expertStatus) {
            out.println("The game was put in expert variant");
        } else {
            out.println("The game was put back in normal variant");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showChosenNumOfPlayers(int maxPlayers) {
        out.println("Main player has set the game as a " + maxPlayers + " players game");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showGenericMessage(String genericMessage) {
        out.println(genericMessage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showDisconnectionMessage(String nicknameDisconnected, String text) {
        out.println("\n" + nicknameDisconnected + text);
    }

    /**
     * {@inheritDoc}
     */
    public void showOtherDisconnectionMessage(String nicknameDisconnected, String text) {
        out.println("\n" + nicknameDisconnected + text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showEndGame() {
        out.println(statePrinter.getWinner() + " won the game.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showErrorAndExit(String error) {
        inputStream.stopReading();
        out.println("\nERROR: " + error);
        out.println("EXIT.");
        Client.LOGGER.info("Closing client for error: error");
        System.exit(1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showLobby(List<String> nicknameList, int numPlayers) {
        out.println("LOBBY:");
        for (String nick : nicknameList) {
            out.println(nick + "\n");
        }
        out.println("Current players in lobby: " + nicknameList.size() + " / " + numPlayers);
    }

    /**
     * Clears the cli.
     */
    public void clearCli() {
        out.print(EscapeCli.CLEAR);
        out.flush();
    }
}
