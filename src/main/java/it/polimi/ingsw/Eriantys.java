package it.polimi.ingsw;

import it.polimi.ingsw.client.view.gui.App;
import it.polimi.ingsw.client.view.gui.JavaFXGui;
import javafx.application.Application;

public class Eriantys {

    public static void main(String[] args) {
        if(args.length < 1){
            System.out.println("Got wrong argument, please retry.\nCorrect arguments:\n" +
                    "\teriantys s - to launch a server \n" +
                    "\teriantys c - to launch a cli client \n" +
                    "\teriantys g - to launch a gui client (for now launches cli client) \n");
        }
        else if(args[0].equals("s")){
            ServerApp.server();
        } else if (args[0].equals("c")){
            ClientApp.client();
        } else if (args[0].equals("g")){
            ClientApp.client();
        } else if (args[0].equals("conti")){
            Application.launch(JavaFXGui.class);

        } else {
            System.out.println("Got wrong argument, please retry.\nCorrect arguments:\n" +
                    "\teriantys s - to launch a server \n" +
                    "\teriantys c - to launch a cli client \n" +
                    "\teriantys g - to launch a gui client (for now launches cli client) \n");
        }
    }
}
