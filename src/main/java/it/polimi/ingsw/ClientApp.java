package it.polimi.ingsw;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.view.cli.Cli;


public class ClientApp {

    public static void main(String[] args) {

        boolean cliParam = true; // default value
        if (cliParam) {
            Cli view = new Cli();
            ClientController clientcontroller = new ClientController(view);
            view.addObserver(clientcontroller);
            view.init();
            System.out.println("Finish client");
        }
    }
}
