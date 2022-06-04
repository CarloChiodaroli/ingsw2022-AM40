package it.polimi.ingsw.client.view.cli;

/**
 * This class contains all colors used in Cli.
 */
public enum ColorCli {

    CLEAR("\033[2J"),
    BLUE("\u001B[34m"),
    PINK("\u001B[95m"),
    YELLOW("\u001B[93m"),
    RED("\u001B[91m"),
    GREEN("\u001B[92m"),
    DEFAULT("\u001B[0m");

    private final String code;

    ColorCli(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}