package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.model.PlayMessageController;
import it.polimi.ingsw.client.network.SocketClient;
import it.polimi.ingsw.client.observer.ViewObserver;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.Wizard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Commands {

    private Object userInputLock;
    private final PlayMessageController playMessageController;


    public Commands(PlayMessageController playMessageController){
        userInputLock = new Object();
        this.playMessageController = playMessageController;
    }

    /**
     * Called from the Client Input Stream class which manages the input stream of commands.
     * This method divides the received command argument in parts and calls the correct method to run the user desired command.
     *
     * The Accepted commands are:
     * <pre>
     *
     *  Play commands:
     *     {@link it.polimi.ingsw.client.view.cli.Cli#abbreviation(List) abbreviation}     {@link it.polimi.ingsw.client.view.cli.Cli#command(List) command}      arg             arg     arg      : description
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

    // Play commands

    /**
     * {@link it.polimi.ingsw.client.view.cli.Cli#abbreviation(List)}
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
     * {@link it.polimi.ingsw.client.view.cli.Cli#abbreviation(List) abbreviation} of
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
     * Method used by the studentmove command to check if the id arguments on the command are valid ids or not,
     * and to correct the cases. The comparison is made ignoring cases, and if successful, the sent ID is the
     * case corrected id.
     *
     * @param id the id to verify.
     * @return the case corrected verified id.
     * @throws IllegalAccessException thrown when the gotten id does not exist in the Play State class.
     */
    private String verifyId(String id) throws IllegalAccessException {
        // In the server the "Card" id indicates the actual card memory id.
        // The client needs to know if the player has called a real character and not casual things.
        // So after having found what place the player wants to send,
        // if the place has a Character Card id, that id is translated to "Card".
        // i.e. "FRIAR" translated in "Card", "entrance" -> "Entrance;
        String result = playMessageController.getPlaceIds().stream()
                .filter(x -> x.equalsIgnoreCase(id))
                .findFirst()
                .orElseThrow(() -> new IllegalAccessException("place " + id + " not found"));
        return Arrays.stream(Characters.values())
                .filter(x -> x.toString().equals(result))
                .findAny()
                .map(x -> "Card")
                .orElse(result);
    }

    /**
     * {@link it.polimi.ingsw.client.view.cli.Cli#abbreviation(List) abbreviation} of
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
     * {@link it.polimi.ingsw.client.view.cli.Cli#abbreviation(List) abbreviation} of
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
     * {@link it.polimi.ingsw.client.view.cli.Cli#abbreviation(List) abbreviation} of
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
     * {@link it.polimi.ingsw.client.view.cli.Cli#abbreviation(List) abbreviation} of
     * {@link #character(List) character}
     */
    public void ca(List<String> args) throws IllegalStateException{
        character(args);
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
}
