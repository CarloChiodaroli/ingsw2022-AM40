package it.polimi.ingsw;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.view.cli.Cli;


public class ClientApp {

    /*public static void main(String[] args) {

        boolean cliParam = true; // default value
        if (cliParam) {
            Cli view = new Cli();
            ClientController clientcontroller = new ClientController(view);
            view.addObserver(clientcontroller);
            view.init();
            System.out.println("Finish client");
        }
    }*/

    /*public static void main(String[] args) {

        boolean cliParam = true; // default value
        if (cliParam) {
            Cli cli = new Cli();
            ClientController clientcontroller = new ClientController(cli);
            cli.addObserver(clientcontroller);
            cli.init();
        }
    }*/

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
