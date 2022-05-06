package it.polimi.ingsw;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.view.cli.Cli;
public class ClientApplication {

    public static void main(String[] args) {
        boolean cliParam  = true; //set true if there is cli , set false when there will be GUI

        if(cliParam)
        {
            Cli view = new Cli();
            ClientController clientcontroller = new ClientController(view);
            view.addObserver(clientcontroller);
            view.init();
        }
    }



}
