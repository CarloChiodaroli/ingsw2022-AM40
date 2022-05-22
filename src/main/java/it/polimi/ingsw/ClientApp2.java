package it.polimi.ingsw;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.view.cli.Cli;



public class ClientApp2 {

    public static void main(String[] args) {

        boolean cliParam = true; // default value
        if (cliParam) {
            Cli view = new Cli();
            ClientController clientcontroller = new ClientController(view);
            view.addObserver(clientcontroller);
            view.init();
        }
    }
}