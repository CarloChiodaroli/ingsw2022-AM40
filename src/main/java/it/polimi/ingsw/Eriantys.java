package it.polimi.ingsw;

import it.polimi.ingsw.client.view.gui.App;

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
            App.main(args); // https://github.com/ingconti/SampleJavaFx.git
        } else {
            System.out.println("Got wrong argument, please retry.\nCorrect arguments:\n" +
                    "\teriantys s - to launch a server \n" +
                    "\teriantys c - to launch a cli client \n" +
                    "\teriantys g - to launch a gui client (for now launches cli client) \n");
        }
    }
}
