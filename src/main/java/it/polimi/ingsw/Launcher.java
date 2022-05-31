package it.polimi.ingsw;

public class Launcher {

    public static void main(String[] args) {
        if(args.length < 1){
            System.out.println("Got wrong argument, please retry.\nCorrect arguments:\n" +
                    "\teriantys s - to launch a server \n" +
                    "\teriantys c - to launch a cli client \n" +
                    "\teriantys g - to launch a gui client (for now launches cli client) \n");
        }
        else if(args[0].equals("s")){
            ServerApp.main(args);
        } else if (args[0].equals("c")){
            ClientApp.main(args);
        } else if (args[0].equals("g")){
            ClientApp.main(args);
        } else {
            System.out.println("Got wrong argument, please retry.\nCorrect arguments:\n" +
                    "\teriantys s - to launch a server \n" +
                    "\teriantys c - to launch a cli client \n" +
                    "\teriantys g - to launch a gui client (for now launches cli client) \n");
        }
    }
}
