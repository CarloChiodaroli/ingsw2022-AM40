package it.polimi.ingsw;

import it.polimi.ingsw.client.view.gui.JavaFXGui;

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

    public static void main(String[] args) {
        System.out.println(BIG_ERIANTYS);
        if (args.length < 1) {
            System.out.println("Got wrong argument, please retry.\nCorrect arguments:\n" +
                    "\teriantys --s - to launch a server\n" +
                    "\teriantys --s --port <number> - to launch a server on a custom port number\n" +
                    "\teriantys --c - to launch a cli client\n" +
                    "\teriantys --g - to launch a gui client\n");
        } else if (args[0].equals("--s")) {
            ServerApp.server(args);
        } else if (args[0].equals("--c")) {
            CliClientApp.client();
        } else if (args[0].equals("--g")) {
            JavaFXGui.loader(args);
        } else if (args[0].equals("--test")) {
            App.main(args);
        } else {
            System.out.println("Got wrong argument, please retry.\nCorrect arguments:\n" +
                    "\teriantys s - to launch a server \n" +
                    "\teriantys s -port <number> - to launch a server on a custom port number\n" +
                    "\teriantys c - to launch a cli client \n" +
                    "\teriantys g - to launch a gui client (for now launches cli client) \n");
        }
    }
}
