package it.polimi.ingsw.client.view.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class manages CLI's input stream in a separate thread from the out stream, so that the out stream and the input
 * stream can work independently without waiting for each other.
 */
public class ClientInputStream {

    private ExecutorService readExecutionQueue;
    private final Cli cli;
    private boolean stop;

    /**
     * Constructor.
     *
     * @param cli the cli where to give received commands.
     */
    public ClientInputStream(Cli cli) {
        this.cli = cli;
        stop = false;
        buildClient();
        readInput();
    }

    /**
     * Here the separate thread is created and perpetually waits for suer input.
     */
    public void readInput() {
        this.readExecutionQueue.execute(() -> {
            while (!stop) {
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                try {
                    line = reader.readLine();
                    if (!line.isBlank()) readReceivedCommand(line);
                } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                    inputError("I didn't understand, Please repeat!!");
                } catch (IOException e) {
                    inputError("Well something went wrong I didn't understand, Please repeat!!");
                }
            }
        });
    }

    /**
     * Called to kill input stream reader thread.
     */
    public void stopReading() {
        this.stop = true;
        this.readExecutionQueue.shutdownNow();
    }

    /**
     * Gives the received command toi the {@link Cli} class calling the {@link Cli#receivedCommand(String)}.
     *
     * @param line {@link Cli#receivedCommand(String)}'s command arg.
     * @throws InvocationTargetException {@link Cli#receivedCommand(String)}'s throw.
     * @throws NoSuchMethodException     {@link Cli#receivedCommand(String)}'s throw.
     * @throws IllegalAccessException    {@link Cli#receivedCommand(String)}'s throw.
     */
    private void readReceivedCommand(String line) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        cli.receivedCommand(line);
    }

    /**
     * Shows to the user that he has received a wrongly written command.
     *
     * @param message the description of the error.
     */
    private void inputError(String message) {
        cli.showError(message);
    }

    /**
     * Initializes the input thread creation.
     */
    private void buildClient() {
        this.readExecutionQueue = Executors.newSingleThreadExecutor();
    }
}
