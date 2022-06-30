package it.polimi.ingsw;

import it.polimi.ingsw.client.CliClientApp;
import it.polimi.ingsw.client.GuiClientApp;
import it.polimi.ingsw.server.ServerApp;

/**
 * Main Game class, called while starting program execution.
 */
public class Eriantys {

    private final static String BIG_ERIANTYS = """
            ╔══════════════════════════════════════════════════════════════════════════╗
            ║   ▄▄▄▄▄▄  ▄▄▄▄▄▄   ▄      ▄      ▄▄       ▄  ▄▄▄▄▄▄▄  ▄     ▄   ▄▄▄▄▄▄   ║
            ║  █▀▀▀▀▀▀  █▀▀▀▀▀█  █     █▀█     ██▄      █  ▀▀▀█▀▀▀  █     █  █▀▀▀▀▀▀█  ║
            ║  █        █     █  █    █▀ ▀█    █ ▀█     █     █     █     █  █         ║
            ║  █        █     █  █   █▀   ▀█   █  ▀█    █     █     ▀█   █▀  █         ║
            ║  ███████  ██████▀  █  █▀     ▀█  █   ▀█   █     █      ▀█ █▀   ▀██████▄  ║
            ║  █        █  █     █  █▀▀███▀▀█  █    ▀█  █     █       ▀█▀           █  ║
            ║  █        █   █    █  █       █  █     ▀█ █     █        █            █  ║
            ║  █▄▄▄▄▄▄  █    █   █  █       █  █      ▀██     █        █     █▄▄▄▄▄▄█  ║
            ║   ▀▀▀▀▀▀  ▀     ▀  ▀  ▀       ▀  ▀       ▀▀     ▀        ▀      ▀▀▀▀▀▀   ║
            ╚══════════════════════════════════════════════════════════════════════════╝
            """;

    /**
     * Simple main which receives arguments to launch the correct program. <br>
     * Argument list: <br>
     * --s - To launch a server <br>
     * --s --port - To launch a server on a specific port <br>
     * --c - To launch a cli client <br>
     * --g - To launch a gui client <br>
     *
     * @param args arguments given at runtime while starting the program
     */
    public static void main(String[] args) {
        System.out.println(BIG_ERIANTYS);
        if (args.length < 1) {
            System.out.println("Got wrong argument, please retry.\nCorrect arguments:\n" +
                    "\tAM40.jar --s - to launch a server\n" +
                    "\tAM40.jar --s --port <number> - to launch a server on a custom port number\n" +
                    "\tAM40.jar --c - to launch a cli client\n" +
                    "\tAM40.jar --g - to launch a gui client\n");
        } else if (args[0].equals("--s")) {
            ServerApp.server(args);
        } else if (args[0].equals("--c")) {
            CliClientApp.client();
        } else if (args[0].equals("--g")) {
            GuiClientApp.loader(args);
        } else {
            System.out.println("Got wrong argument, please retry.\nCorrect arguments:\n" +
                    "\tAM40.jar s - to launch a server \n" +
                    "\tAM40.jar s --port <number> - to launch a server on a custom port number\n" +
                    "\tAM40.jar c - to launch a cli client \n" +
                    "\tAM40.jar g - to launch a gui client (for now launches cli client) \n");
        }
    }
}
