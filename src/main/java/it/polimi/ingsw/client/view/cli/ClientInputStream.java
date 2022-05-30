package it.polimi.ingsw.client.view.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientInputStream {

    private ExecutorService readExecutionQueue;
    private final Cli cli;
    private boolean stop;

    public ClientInputStream(Cli cli) {
        this.cli = cli;
        stop = false;
        buildClient();
        readInput();
    }

    public void readInput() {
        this.readExecutionQueue.execute(() -> {
            while (!stop) {
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                try {
                    line = reader.readLine();
                    if (!line.isBlank()) readReceivedCommand(line);
                } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e){
                    inputError("I didn't understand, Please repeat!!");
                } catch (IOException e) {
                    inputError("Well something went wrong I didn't understand, Please repeat!!");
                }
            }
        });
    }

    public void stopReading() {
        this.stop = true;
        this.readExecutionQueue.shutdownNow();
    }

    private void readReceivedCommand(String line) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        cli.receivedCommand(line);
    }

    private void inputError(String message) {
        cli.showError(message);
    }

    private void buildClient() {
        this.readExecutionQueue = Executors.newSingleThreadExecutor();
    }
}
