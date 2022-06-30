package it.polimi.ingsw.client;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.view.cli.Cli;

/**
 * This class is the Client Cli launcher.
 */
public class CliClientApp {

    /**
     * Main load method of cli client.
     */
    public static void client() {
        boolean cliParam = true; // default value
        if (cliParam) {
            Cli cli = new Cli();
            ClientController clientcontroller = new ClientController(cli);
            cli.addObserver(clientcontroller);
            cli.init();
        }
    }
}
