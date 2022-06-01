package it.polimi.ingsw;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.view.cli.Cli;


public class CliClientApp {

    public static void client(){
        boolean cliParam = true; // default value
        if (cliParam) {
            Cli cli = new Cli();
            ClientController clientcontroller = new ClientController(cli);
            cli.addObserver(clientcontroller);
            cli.init();
        }
    }
}
