package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.Observer.ViewObservable;
import it.polimi.ingsw.view.cli.View;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Cli extends ViewObservable implements View {

    private final PrintStream out;
    private Thread inputThread;

    public Cli()
    {
        out = System.out;
    }

    public void init()
    {
        out.println("Eryantis");
        askServerInfo();
    }

    private void askServerInfo() {
        Map<String, String> serverInfo = new HashMap<>();
        String defaultAddress = "localhost";
        String defaultPort = "16847";
        boolean validInput;
        String address=null;
        String port=null;

        out.println("Please specify the following settings. The default value is shown between brackets.");

        do {
            out.print("Enter the server address [" + defaultAddress + "]: ");
            try
            {
                address = readCommand();
            }
            catch (ExecutionException e)
            {
            out.println("Errore");
            }


            if (address.equals("localhost"))
            {
                serverInfo.put("address", defaultAddress);
                validInput = true;
            }
                else
            {
                out.println("Invalid address!");
                validInput = false;
            }


        } while (!validInput);

        do {
            out.print("Enter the server port [" + defaultPort + "]: ");
            try
            {
                port = readCommand();
            }
            catch (ExecutionException e)
            {
                out.println("Errore");
            }
            if (port.equals("16847"))
            {
                serverInfo.put("port", defaultPort);
                validInput = true;
            } else {
                out.println("Invalid port!");
                validInput = false;
            }
        } while (!validInput);

    notifyObserver(obs->obs.onUpdateServerInfo(serverInfo));
    }

    public String readCommand() throws ExecutionException
    {
        FutureTask<String> futureTask = new FutureTask<>(new InputReadTask());
        inputThread = new Thread(futureTask);
        inputThread.start();
        String input = null;
        try {
            input = futureTask.get();
        } catch (InterruptedException e) {
            futureTask.cancel(true);
            Thread.currentThread().interrupt();
        }
        return input;
    }

    public void askPlayerName()
    {
        out.print("Enter your player name: ");
        try
        {
            String playerName = readCommand();
            notifyObserver(obs -> obs.onUpdateNickname(playerName));
        }
        catch (ExecutionException e)
        {
            out.println("Errore");
        }
    }

    public void askPlayersNumber()
    {
        int playerNumber;
        String question = "How many players are going to play? (You can choose between 2 or 3 players): ";
        try
        {
            playerNumber = numberInput(2, 3, null, question);
            notifyObserver(obs -> obs.onUpdatePlayersNumber(playerNumber));
        }
        catch (ExecutionException e)
        {
        System.out.println("Errore");
        }

    }

    public void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname) {

        if (nicknameAccepted && connectionSuccessful) {
            out.println("Hi, " + nickname + "! You connected to the server.");
        } else if (connectionSuccessful) {
            askPlayerName();
        } else if (nicknameAccepted) {
            out.println("Max players reached. Connection refused.");
            out.println("EXIT.");

            System.exit(1);
        } else {
            out.println("Could not contact server.");
        }
    }

    public void showDisconnectionMessage(String playerNameDisconnected, String text) {
        inputThread.interrupt();
        out.println("\n" + playerNameDisconnected + text);
        System.exit(1);
    }

    private int numberInput(int minValue, int maxValue, List<Integer> jumpList, String question) throws ExecutionException {
        int number = minValue - 1;

        // A null jumpList will be transformed in a empty list.
        if (jumpList == null) {
            jumpList = List.of();
        }

        do {
            try {
                out.print(question);
                number = Integer.parseInt(readCommand());

                if (number < minValue || number > maxValue) {
                    out.println("Invalid number! Please try again.\n");
                } else if (jumpList.contains(number)) {
                    out.println("This number cannot be selected! Please try again.\n");
                }
            } catch (NumberFormatException e) {
                out.println("Invalid input! Please try again.\n");
            }
        } while (number < minValue || number > maxValue || jumpList.contains(number));

        return number;
    }

    public void askFirstPlayer(List<String> nicknameQueue) {
        out.println("You're the Challenger, choose the first player: ");
        out.print("Online players: " + String.join(", ", nicknameQueue));
        try {

            String nickname;
            do {
                out.print("\nType the exact name of the player: ");

                nickname = readCommand();
                if (!nicknameQueue.contains(nickname)) {
                    out.println("You have selected an invalid player! Please try again.");
                }
            } while (!nicknameQueue.contains(nickname));

            String finalNickname = nickname;
            notifyObserver(obs -> obs.onUpdateFirstPlayer(finalNickname));
        } catch (ExecutionException e) {
            out.println("Errore");
        }
    }

    public void showMatchInfo(List<String> players, String activePlayer)
    {
//Errore
     }
}
