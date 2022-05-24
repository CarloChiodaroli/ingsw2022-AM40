package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.controller.ClientController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientInputDecoder {

    private ExecutorService readExecutionQueue;
    private InputReadTask inputReadTask;
    private Cli cli;
    private boolean stop;

    public ClientInputDecoder(Cli cli) {
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
                } catch (InvocationTargetException e){
                    inputError(e.getCause().getMessage());
                } catch (IOException | NoSuchMethodException | IllegalAccessException e) {
                    //e.printStackTrace();
                    inputError("I didn't understand, please repeat!");
                }
            }
        });
    }

    public void stopReading() {
        this.stop = true;
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
