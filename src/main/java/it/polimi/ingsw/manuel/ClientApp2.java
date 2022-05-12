package it.polimi.ingsw.manuel;

import it.polimi.ingsw.manuel.controller.ClientController;
import it.polimi.ingsw.manuel.view.cli.Cli;



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
